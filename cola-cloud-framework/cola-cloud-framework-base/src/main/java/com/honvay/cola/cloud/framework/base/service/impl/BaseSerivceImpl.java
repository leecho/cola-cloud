package com.honvay.cola.cloud.framework.base.service.impl;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import com.honvay.cola.cloud.framework.base.entity.BaseEntity;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.framework.base.util.WebUtils;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基础服务实现类
 * @author LIQIU
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class BaseSerivceImpl <T> implements BaseService<T> {

	private static final Log logger = LogFactory.getLog(ServiceImpl.class);

    @Autowired
    protected BaseMapper<T> mapper;

    /**
     * <p>
     * 判断数据库操作是否成功
     * </p>
     * <p>
     * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
     * </p>
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    /**
     * 获取泛型类型
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(getClass(), 0);
    }
    
    /**
     * 根据Mapper类型获取Mapper
     * @param m
     * @return
     */
    @SuppressWarnings("unchecked")
	public <M extends BaseMapper> M getMapper(Class<M> m){
    	return (M)mapper;
    }

    public <M extends BaseMapper> M getMapper(){
        return (M)mapper;
    }

    
    @Override
    public T unique(String column, String value) {
     T t   =this.selectOne(new EntityWrapper<T>().eq(column, value));
    	return t;
    }
    
    @Override
    public List<T> listByProperty(String column, Object value){
    	return this.selectList(new EntityWrapper<T>().eq(column, value));
    }
    
    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    private void setAuditable(T entity){
        if(entity instanceof BaseEntity){
            BaseEntity baseEntity = (BaseEntity)entity;
            User user = SecurityUtils.getPrincipal();
            if(user != null){
                //设置创建者信息
                if(baseEntity.getCreateBy() == null){
                    baseEntity.setCreateBy(SecurityUtils.getUserId());
                    baseEntity.setCreateTime(new Date());
                }
                //设置修改者信息
                baseEntity.setModifyBy(SecurityUtils.getUserId());
                baseEntity.setModifyTime(new Date());
            }else{
                //设置创建日期
                if(baseEntity.getCreateBy() == null){
                    baseEntity.setCreateTime(new Date());
                }
                //设置修改日期
                baseEntity.setModifyTime(new Date());
            }
            baseEntity.setModifyIp(WebUtils.getClientIp());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(T entity) {
        this.setAuditable(entity);
        return retBool(mapper.insert(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertAllColumn(T entity) {
        this.setAuditable(entity);
        return retBool(mapper.insertAllColumn(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(List<T> entityList) {
        return insertBatch(entityList, 30);
    }

    /**
     * 批量插入
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                this.setAuditable(entityList.get(i));
                batchSqlSession.insert(sqlStatement, entityList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
        }
        return true;
    }

    /**
     * <p>
     * TableId 注解存在更新记录，否插入一条记录
     * </p>
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return insert(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
                    return updateById(entity) || insert(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateAllColumn(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return insertAllColumn(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
                    return updateAllColumnById(entity) || insertAllColumn(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, 30);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, 30, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, false);
    }

    /**
     * 获取新的EntityWrpper
     * @return
     */
    public EntityWrapper<T> newEntityWrapper(){
        return new EntityWrapper<T>();
    }

    /**
     * 批量插入修改
     *
     * @param entityList 实体对象列表
     * @param batchSize  批量刷新个数
     * @param selective  是否滤掉空字段
     * @return boolean
     */
    private boolean insertOrUpdateBatch(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            for (int i = 0; i < size; i++) {
                if (selective) {
                    insertOrUpdate(entityList.get(i));
                } else {
                    insertOrUpdateAllColumn(entityList.get(i));
                }
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertOrUpdateBatch Method. Cause", e);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Serializable id) {
        return retBool(mapper.deleteById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByMap(Map<String, Object> columnMap) {
        if (MapUtils.isEmpty(columnMap)) {
            throw new MybatisPlusException("deleteByMap columnMap is empty.");
        }
        return retBool(mapper.deleteByMap(columnMap));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Wrapper<T> wrapper) {
        return retBool(mapper.delete(wrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        return retBool(mapper.deleteBatchIds(idList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T entity) {
        this.setAuditable(entity);
        return retBool(mapper.updateById(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllColumnById(T entity) {
        this.setAuditable(entity);
        return retBool(mapper.updateAllColumnById(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity, Wrapper<T> wrapper) {
        this.setAuditable(entity);
        return retBool(mapper.update(entity, wrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(List<T> entityList) {
        return updateBatchById(entityList, 30);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllColumnBatchById(List<T> entityList) {
        return updateAllColumnBatchById(entityList, 30);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllColumnBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, false);
    }

    /**
     * 根据主键ID进行批量修改
     *
     * @param entityList 实体对象列表
     * @param batchSize  批量刷新个数
     * @param selective  是否滤掉空字段
     * @return boolean
     */
    private boolean updateBatchById(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
            String sqlStatement = sqlStatement(sqlMethod);
            for (int i = 0; i < size; i++) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                this.setAuditable(entityList.get(i));
                param.put("et", entityList.get(i));
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", e);
        }
        return true;
    }

    @Override
    public T selectById(Serializable id) {
        return mapper.selectById(id);
    }

    @Override
    public List<T> selectBatchIds(List<? extends Serializable> idList) {
        return mapper.selectBatchIds(idList);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
    }

    @Override
    public T selectOne(Wrapper<T> wrapper) {
      List<T> list=  mapper.selectList(wrapper);
        return SqlHelper.getObject(list);
    }

    @Override
    public Map<String, Object> selectMap(Wrapper<T> wrapper) {
        return SqlHelper.getObject(mapper.selectMaps(wrapper));
    }

    @Override
    public Object selectObj(Wrapper<T> wrapper) {
        return SqlHelper.getObject(mapper.selectObjs(wrapper));
    }

    @Override
    public int selectCount(Wrapper<T> wrapper) {
        return SqlHelper.retCount(mapper.selectCount(wrapper));
    }

    @Override
    public List<T> selectList(Wrapper<T> wrapper) {
        return mapper.selectList(wrapper);
    }

    @Override
    public List<T> selectList(){
        return mapper.selectList(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> selectPage(Page<T> page) {
        return selectPage(page, Condition.EMPTY);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return mapper.selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return mapper.selectObjs(wrapper);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
        SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(mapper.selectMapsPage(page, wrapper));
        return page;
    }

    @Override
    public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(mapper.selectPage(page, wrapper));
        return page;
    }
}
