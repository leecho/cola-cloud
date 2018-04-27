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
import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.parser.CriteriaParser;
import com.honvay.cola.cloud.framework.base.pagination.PageableCriteria;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础服务实现类
 * @author LIQIU
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class BaseServiceImpl<T> implements BaseService<T> {

	private static final Log logger = LogFactory.getLog(ServiceImpl.class);

    @Autowired
    protected BaseMapper<T> mapper;

    protected CriteriaParser criteriaParser = new CriteriaParser();

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

    public EntityWrapper<T> newEntityWrapper(){
        return new EntityWrapper<>();
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

    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    
    @Override
    public T findOneByColumn(String column, String value) {
     T t   =this.selectOne(new EntityWrapper<T>().eq(column, value));
    	return t;
    }

    @Override
    public List<T> selectList() {
        return this.selectList(Condition.EMPTY);
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

    /**
     * 插入实体
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(T entity) {
        return retBool(mapper.insert(entity));
    }

    /**
     * 插入实体中所有的列
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertAllColumn(T entity) {
        return retBool(mapper.insertAllColumn(entity));
    }

    /**
     * 批量查询
     * @param entityList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if (com.baomidou.mybatisplus.toolkit.CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, entityList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
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
    @Transactional(rollbackFor = Exception.class)
    @Override
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

    @Transactional(rollbackFor = Exception.class)
    @Override
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertOrUpdateBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, 30);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, 30, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, false);
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
        if (com.baomidou.mybatisplus.toolkit.CollectionUtils.isEmpty(entityList)) {
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(Serializable id) {
        return SqlHelper.delBool(mapper.deleteById(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByMap(Map<String, Object> columnMap) {
        if (com.baomidou.mybatisplus.toolkit.MapUtils.isEmpty(columnMap)) {
            throw new MybatisPlusException("deleteByMap columnMap is empty.");
        }
        return SqlHelper.delBool(mapper.deleteByMap(columnMap));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Wrapper<T> wrapper) {
        return SqlHelper.delBool(mapper.delete(wrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        return SqlHelper.delBool(mapper.deleteBatchIds(idList));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(T entity) {
        return retBool(mapper.updateById(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAllColumnById(T entity) {
        return retBool(mapper.updateAllColumnById(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(T entity, Wrapper<T> wrapper) {
        return retBool(mapper.update(entity, wrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(List<T> entityList) {
        return updateBatchById(entityList, 30);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAllColumnBatchById(List<T> entityList) {
        return updateAllColumnBatchById(entityList, 30);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
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
        if (com.baomidou.mybatisplus.toolkit.CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
            String sqlStatement = sqlStatement(sqlMethod);
            for (int i = 0; i < size; i++) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
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
    public List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return mapper.selectBatchIds(idList);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
    }

    @Override
    public T selectOne(Wrapper<T> wrapper) {
        return SqlHelper.getObject(mapper.selectList(wrapper));
    }

    @Override
    public T selectOne(EntityWrapper<T> wrapper, Criteria<T> criteria,String group){
        return (T) this.selectOne(this.criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public T selectOne(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectOne(wrapper,criteria,null);
    }

    @Override
    public T selectOne(Criteria<T> criteria){
        return this.selectOne(new EntityWrapper<>(),criteria,null);
    }
    @Override
    public Map<String, Object> selectMap(Wrapper<T> wrapper) {
        return SqlHelper.getObject(mapper.selectMaps(wrapper));
    }


    public Map<String, Object> selectMap(EntityWrapper<T> wrapper, Criteria<T> criteria,String group){
        return this.selectMap(criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public Map<String, Object> selectMap(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectMap(wrapper,criteria,null);
    }

    @Override
    public Map<String, Object> selectMap(Criteria<T> criteria){
        return this.selectMap(new EntityWrapper<>(),criteria,null);
    }

    @Override
    public Object selectObj(Wrapper<T> wrapper) {
        return SqlHelper.getObject(mapper.selectObjs(wrapper));
    }

    @Override
    public Object selectObj(EntityWrapper<T> wrapper, Criteria<T> criteria, String group){
        return this.selectObj(criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public Object selectObj(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectObj(wrapper,criteria,null);
    }

    @Override
    public Object selectObj(Criteria<T> criteria){
        return this.selectObj(new EntityWrapper<>(),criteria,null);
    }

    @Override
    public int selectCount(Wrapper<T> wrapper) {
        return SqlHelper.retCount(mapper.selectCount(wrapper));
    }

    @Override
    public int selectCount(EntityWrapper<T> wrapper, Criteria<T> criteria, String group){
        return this.selectCount(criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public int selectCount(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectCount(wrapper,criteria,null);
    }

    @Override
    public int selectCount(Criteria<T> criteria){
        return this.selectCount(new EntityWrapper<>(),criteria,null);
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
    public List<Object> selectObjs(EntityWrapper<T> wrapper, Criteria<T> criteria, String group){
        return this.selectObjs(criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public List<Object> selectObjs(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectObjs(wrapper,criteria,null);
    }

    @Override
    public List<Object> selectObjs(Criteria<T> criteria){
        return this.selectObjs(new EntityWrapper<>(),criteria,null);
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
        wrapper = (Wrapper<T>) SqlHelper.fillWrapper(page,  wrapper);
        page.setRecords(mapper.selectMapsPage(page, wrapper));
        return page;
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Page page, EntityWrapper<T> wrapper, Criteria<T> criteria, String group){
        return this.selectMapsPage(page,criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Page page,EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectMapsPage(page,wrapper,criteria,null);
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Page page,Criteria<T> criteria){
        return this.selectMapsPage(page,new EntityWrapper<T>(),criteria,null);
    }


    @Override
    public List<T> selectList(Wrapper<T> wrapper) {
        return mapper.selectList(wrapper);
    }

    @Override
    public List<T> selectList(String column, Object value) {
        return this.selectList(new EntityWrapper<T>().eq(column, value));
    }

    @Override
    public List<T> selectList(EntityWrapper<T> wrapper, Criteria<T> criteria,String group){
        return this.selectList(criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public List<T> selectList(EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectList(wrapper,criteria,null);
    }

    @Override
    public List<T> selectList(Criteria<T> criteria){
        return this.selectList(new EntityWrapper<>(),criteria,null);
    }

    @Override
    public Page<T> selectPage(Page<T> page) {
        return selectPage(page, Condition.EMPTY);
    }

    @Override
    public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        wrapper = (Wrapper<T>) SqlHelper.fillWrapper(page,  wrapper);
        page.setRecords(mapper.selectPage(page, wrapper));
        return page;
    }

    @Override
    public Page<T> selectPage(Page<T> page, EntityWrapper<T> wrapper, Criteria<T> criteria, String group){
        return this.selectPage(page,criteriaParser.parse(criteria,wrapper,group));
    }

    @Override
    public Page<T> selectPage(Page<T> page, EntityWrapper<T> wrapper, Criteria<T> criteria){
        return this.selectPage(page,wrapper,criteria,null);
    }

    @Override
    public Page<T> selectPage(Page<T> page, Criteria<T> criteria){
        return this.selectPage(page,new EntityWrapper<>(),criteria,null);
    }

    @Override
    public Page<T> selectPage(PageableCriteria pageableCriteria) {
        return this.selectPage(pageableCriteria.getPage(),pageableCriteria);
    }
}
