package com.honvay.cola.cloud.upm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.upm.cache.SysResourceCacheService;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysResourceDTO;
import com.honvay.cola.cloud.upm.model.SysResourceTreeNode;
import com.honvay.cola.cloud.upm.model.SysResourceVO;
import com.honvay.cola.cloud.upm.service.SysAuthorityService;
import com.honvay.cola.cloud.upm.service.SysResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource> implements SysResourceService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private SysAuthorityService sysAuthorityService;

    @Autowired
    private SysResourceCacheService sysResourceCacheService;

    private ThreadLocal<Set<String>> authorizedResourceHolder = new ThreadLocal<>();

    /**
     * 检查配置项是否有重复
     *
     * @param sysResource
     * @return
     */
    private boolean check(SysResource sysResource) {
        EntityWrapper<SysResource> wrapper = new EntityWrapper<SysResource>();
        wrapper.eq("code", sysResource.getCode());
        if (sysResource.getId() != null) {
            wrapper.ne("id", sysResource.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }

    @Override
    public boolean insert(SysResource entity) {
        Assert.isTrue(check(entity), "资源编号已存在");
        boolean result = super.insert(entity);
        return result;
    }

    @Override
    public boolean updateById(SysResource entity) {
        Assert.isTrue(check(entity), "资源编号已存在");
        boolean result = super.updateById(entity);
        return result;
    }

    @Override
    public void delete(Long id) {
        Assert.isTrue(CollectionUtils.isEmpty(this.selectList("parent", id)), "存在子资源无法删除");
        this.deleteById(id);
        this.sysAuthorityService.delete(new EntityWrapper<SysAuthority>().eq("sys_resource_id", id));
    }

    private void preparAuthorizedResource() {
        Collection<String> roles = SecurityUtils.getPrincipal().getRoles();
        //如果不是管理员则加载对应的有权限的资源
        if (!roles.contains(CommonConstant.ROLE_ADMIN)) {
            Set<String> authorizedResource = new HashSet<>();
            for (String role : roles) {
                authorizedResource.addAll(this.sysResourceCacheService.getResourceCodesByRole(role));
            }
            authorizedResourceHolder.set(authorizedResource);
        }
    }

    @Override
    public List<SysResourceTreeNode> getChildren(Long id) {

        EntityWrapper<SysResource> wrapper = this.newEntityWrapper();
        if (id != null) {
            wrapper.eq("parent", id);
        }

        SysResource root = null;
        List<SysResource> resources = this.selectList(wrapper);
        Map<Long, SysResource> resourceMap = new HashMap<>(resources.size());
        for (SysResource resource : resources) {
            if (resource.getParent() == null) {
                root = resource;
            }
            resourceMap.put(resource.getId(), resource);
        }

        Map<Long, List<SysResourceTreeNode>> resourceHierarchyMap = Maps.newHashMap();
        for (Map.Entry<Long, SysResource> entry : resourceMap.entrySet()) {
            SysResource resource = entry.getValue();
            //将子节点放入到父节拥有的子节点集合中
            Long pid = resource.getParent();
            //防止数据错误，自旋
            if (resource.getId().equals(pid)) {
                continue;
            }
            List<SysResourceTreeNode> children = resourceHierarchyMap.computeIfAbsent(pid, k -> new ArrayList<>());
            SysResourceTreeNode sysResourceTreeNode = new SysResourceTreeNode();
            BeanUtils.copyProperties(resource, sysResourceTreeNode);
            children.add(sysResourceTreeNode);
        }

        Assert.notNull(root, "没有找到资源根节点");

        List<SysResourceTreeNode> menus = new ArrayList<>();
        SysResourceTreeNode rootNode = new SysResourceTreeNode();
        BeanUtils.copyProperties(root, rootNode);
        this.getTreeNode(rootNode, resourceHierarchyMap);
        menus.add(rootNode);
        return menus;
    }

    /**
     * 获取资源树的子节点
     *
     * @param parent
     * @param hierarchy
     * @return
     */
    private SysResourceTreeNode getTreeNode(SysResourceTreeNode parent, Map<Long, List<SysResourceTreeNode>> hierarchy) {
        List<SysResourceTreeNode> children = hierarchy.get(parent.getId());
        if (children != null && children.size() > 0) {
            parent.setChildren(children);
            children.forEach((child) -> {
                getTreeNode(child, hierarchy);
            });
        }
        return parent;
    }


    /**
     * 获取已授权的资源树
     *
     * @return
     */
    @Override
    public List<SysResourceTreeNode> getResourceTree() {
        try {
            preparAuthorizedResource();
            List<SysResourceTreeNode> resources = this.getResourceTree(-1L);
            return resources;
        } finally {
            authorizedResourceHolder.remove();
        }
    }

    private List<SysResourceTreeNode> getResourceTree(Long pid) {
        List<SysResourceTreeNode> menus = new ArrayList<>();
        List<SysResource> children = this.sysResourceCacheService.getChildrenCacheByResourceId(pid);
        for (SysResource child : children) {
            //判断是否有权限
            if (isAuthorized(child.getCode())) {
                SysResourceTreeNode menu = new SysResourceTreeNode();
                BeanUtils.copyProperties(child, menu);
                //遍历子节点
                menu.setChildren(this.getResourceTree(child.getId()));
                menus.add(menu);
            }
        }
        menus.sort((o1, o2) -> {
            if (o1.getSort() == null) {
                return 0;
            }
            if (o2.getSort() == null) {
                return -1;
            }
            return o1.getSort() - o2.getSort();
        });
        return menus;
    }

    /**
     * 获取已授权的资源列表
     *
     * @return
     */
    @Override
    public List<SysResourceVO> getResourceList() {
        try {
            List<SysResourceVO> resources = new ArrayList<>();
            Collection<String> roles = SecurityUtils.getPrincipal().getRoles();
            //如果不是管理员则给相对应的权限
            if (!roles.contains(CommonConstant.ROLE_ADMIN)) {
                preparAuthorizedResource();
                Set<String> authorizedResource = authorizedResourceHolder.get();
                for (String code : authorizedResource) {
                    SysResourceVO sysResourceVO = new SysResourceVO();
                    SysResource resource = this.sysResourceCacheService.getResourceByCode(code);
                    if (resource != null) {
                        BeanUtils.copyProperties(resource, sysResourceVO);
                    }
                    resources.add(sysResourceVO);
                }
            } else {
                //如果是管理员则给所有权限
                List<SysResource> resourcesList = this.selectList();
                for (SysResource resource : resourcesList) {
                    SysResourceVO sysResourceVO = new SysResourceVO();
                    BeanUtils.copyProperties(resource, sysResourceVO);
                    resources.add(sysResourceVO);
                }
            }
            return resources;
        } finally {
            authorizedResourceHolder.remove();
        }
    }

    @Override
    public void update(SysResourceDTO sysResourceDTO) {
        SysResource sysResource = this.selectById(sysResourceDTO.getId());
        BeanUtils.copyProperties(sysResourceDTO, sysResource);
        this.updateById(sysResource);
    }

    @Override
    public void insert(SysResourceDTO sysResourceDTO) {
        SysResource sysResource = new SysResource();
        BeanUtils.copyProperties(sysResourceDTO, sysResource);
        this.insert(sysResource);
        sysResourceDTO.setId(sysResource.getId());
    }

    /**
     * 判断是否有授权
     *
     * @param resource
     * @return
     */
    private boolean isAuthorized(String resource) {
        Collection<String> roles = SecurityUtils.getPrincipal().getRoles();
        //如果是管理员则直接返回授权
        if (roles.contains(CommonConstant.ROLE_ADMIN)) {
            return true;
        }
        return authorizedResourceHolder.get().contains(resource);
    }
}
