package com.honvay.cola.cloud.organization.service.impl;

import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.framework.base.mapper.CommonMapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseSerivceImpl;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.organization.entity.SysEmployee;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.entity.SysPost;
import com.honvay.cola.cloud.organization.mapper.SysEmployeeMapper;
import com.honvay.cola.cloud.organization.mapper.SysOrganizationMapper;
import com.honvay.cola.cloud.organization.mapper.SysPostMapper;
import com.honvay.cola.cloud.organization.model.SysEmployeeAddDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeVO;
import com.honvay.cola.cloud.organization.service.SysEmployeeService;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
@Service
public class SysEmployeeServiceImpl extends BaseSerivceImpl<SysEmployee> implements SysEmployeeService {

    @Autowired
    private UcClient ucClient;

   /* @Autowired
    private SysMemberMapper sysMemberMapper;*/

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    private CommonMapper commonMapper;


    /**
     * 获取员工详细信息
     *
     * @param id
     * @return
     */
    @Override
    public SysEmployeeVO getEmployeeById(Long id) {

        org.springframework.util.Assert.notNull(id, "ID不能为空");

        String sql = "select ase.id,asu.name,asu.username,asu.status,asu.email," +
                "asu.phone_number as phoneNumber," +
                "asp.name as postName," +
                "           ase.sys_org_id as orgId" +
                "       from cola_sys_employee ase" +
                "      left join cola_sys_user asu on asu.id = ase.sys_user_id" +
                "      left join cola_sys_post asp on asp.id = ase.sys_post_id" +
                " where ase.id = {0}";
        Map<String, Object> result = this.commonMapper.selectOne(sql, id);
        SysEmployeeVO sysEmployeeVO = new SysEmployeeVO();
        try {
            org.apache.commons.beanutils.BeanUtils.populate(sysEmployeeVO, result);
            return sysEmployeeVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Page<List<SysEmployeeVO>> getEmployeeListByOrgId(Page page, String name, String username, Integer status) {
        SysEmployeeMapper mapper = this.getMapper(SysEmployeeMapper.class);
        name = StringUtils.concatLikeAll(name);
        username = StringUtils.concatLikeAll(username);
        List<SysEmployeeVO> list = mapper.getEmployeeListByOrgId(page, name, username, status);
        page.setRecords(list);
        return page;
    }

    /**
     * 验证岗位是否是租户下的数据
     *
     * @param tenantId
     * @param postId
     */
    private void validatePost(Long tenantId, Long postId) {
        SysPost post = this.sysPostMapper.selectById(postId);
        org.springframework.util.Assert.notNull(post, "岗位不存在");
        if (tenantId != null) {
            org.springframework.util.Assert.notNull(post.getTenantId(), "岗位所属租户错误");
            org.springframework.util.Assert.isTrue(Objects.equals(post.getTenantId(), tenantId), "岗位所属租户错误");
        }
    }

    /**
     * 验证岗位是否是租户下的数据
     *
     * @param tenantId
     * @param orgId
     */
    private void validateOrganization(Long tenantId, Long orgId) {
        SysOrganization sysOrganization = this.sysOrganizationMapper.selectById(orgId);
        org.springframework.util.Assert.notNull(sysOrganization, "部门不存在");
        if (tenantId != null) {
            org.springframework.util.Assert.notNull(sysOrganization.getTenantId(), "岗位所属租户错误");
            org.springframework.util.Assert.isTrue(Objects.equals(sysOrganization.getTenantId(), tenantId), "岗位所属租户错误");
        }
    }

    /**
     * 验证员工是否已经在该部门中
     *
     * @param orgId
     * @param userId
     */
    private void assertEmployeeExists(Long orgId, Long userId) {
        EntityWrapper<SysEmployee> wrapper = new EntityWrapper<>();
        wrapper.eq("sys_org_id", orgId);
        wrapper.eq("sys_user_id", userId);
        org.springframework.util.Assert.isTrue(this.selectList(wrapper).size() == 0, "用户已经在该部门中");
    }

    /**
     * 判断用户已经在租户下
     *
     * @param tenantId
     * @param userId
     */
    private boolean assetMemberNoExists(Long tenantId, Long userId) {
        if (tenantId != null) {
           /*EntityWrapper<SysMember> wrapper = new EntityWrapper<>();
           wrapper.eq("tenant_id",tenantId);
           wrapper.eq("sys_user_id",userId);
           return this.sysMemberMapper.selectList(wrapper).size() == 0;*/
        }
        return false;
    }

    /**
     * 添加员工到部门
     *
     * @param sysEmployeeAddDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysEmployeeAddDTO sysEmployeeAddDTO) {
        //Long tenantId = SecurityUtils.getTenantId();

        this.assertEmployeeExists(sysEmployeeAddDTO.getOrgId(), sysEmployeeAddDTO.getUserId());

        //检查组织架构数据
        //this.validateOrganization(tenantId, sysEmployeeAddDTO.getOrgId());

        //检查岗位数据
        if (sysEmployeeAddDTO.getPostId() != null) {
            // this.validatePost(tenantId, sysEmployeeAddDTO.getPostId());
        }

        //如果是租户登录则新增租户成员记录
        /*if(tenantId != null && assetMemberNoExists(tenantId, sysEmployeeAddDTO.getUserId())){
            SysMember member = new SysMember();
            member.setSysUserId(sysEmployeeAddDTO.getUserId());
            member.setTenantId(tenantId);
            this.sysMemberMapper.insert(member);
        }*/

        //新增员工记录
        SysEmployee employee = new SysEmployee();
        employee.setSysOrgId(sysEmployeeAddDTO.getOrgId());
        employee.setSysUserId(sysEmployeeAddDTO.getUserId());
        employee.setSysPostId(sysEmployeeAddDTO.getPostId());
        this.insert(employee);
    }


    /**
     * 新增员工信息
     *
     * @param sysEmployeeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SysEmployeeDTO sysEmployeeDTO) {
        Long tenantId = SecurityUtils.getTenantId();

        //检查组织架构数据
        this.validateOrganization(tenantId, sysEmployeeDTO.getOrgId());

        //检查岗位数据
        if (sysEmployeeDTO.getPostId() != null) {
            //this.validatePost(tenantId, sysEmployeeDTO.getPostId());
        }

        //新增用户记录
        UserVO user = new UserVO();
        BeanUtils.copyProperties(sysEmployeeDTO, user);
        Result rs = this.ucClient.save(user);

        Assert.isTrue(rs.getSuccess(),rs.getMsg());


       /* //如果是租户登录则新增租户成员记录
        if(tenantId != null){
            SysMember member = new SysMember();
            member.setSysUserId(user.getId());
            member.setTenantId(tenantId);
            this.sysMemberMapper.insert(member);
        }*/

        //新增员工记录
        SysEmployee employee = new SysEmployee();
        employee.setSysOrgId(sysEmployeeDTO.getOrgId());
        Map<String, Object> userMap = (Map<String, Object>) rs.getData();
        employee.setSysUserId(Long.parseLong(userMap.get("id").toString()));
        employee.setSysPostId(sysEmployeeDTO.getPostId());
        this.insert(employee);
    }


    /**
     * 修改员工信息
     *
     * @param sysEmployeeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysEmployeeDTO sysEmployeeDTO) {
        Long tenantId = SecurityUtils.getTenantId();

        Assert.notNull(sysEmployeeDTO.getOrgId(), "所属部门不能为空");
        //检查组织架构数据
        this.validateOrganization(tenantId, sysEmployeeDTO.getOrgId());

        //检查岗位数据
        if (sysEmployeeDTO.getPostId() != null) {
            this.validatePost(tenantId, sysEmployeeDTO.getPostId());
        }

        UserVO userVo = new UserVO();
        BeanUtils.copyProperties(sysEmployeeDTO,userVo);
        this.ucClient.update(userVo);

        SysEmployee employee = this.selectById(sysEmployeeDTO.getId());
        //需改员工信息
        BeanUtils.copyProperties(sysEmployeeDTO, employee);
        this.updateById(employee);

        /*//修改用户信息
        SysUser user = this.sysUserService.selectById(employee.getSysUserId());
        Long userId = user.getId();
        BeanUtils.copyProperties(sysEmployeeDTO,user);
        user.setId(userId);
        this.sysUserService.update(user);*/
    }

}
