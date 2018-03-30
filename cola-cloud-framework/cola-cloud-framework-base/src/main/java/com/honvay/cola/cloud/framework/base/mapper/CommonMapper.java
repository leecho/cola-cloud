package com.honvay.cola.cloud.framework.base.mapper;

import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共Mapper，可执行任何SQL语句
 * @author LIQIU
 * @date 2018-1-4
 **/
public class CommonMapper {

    private static final String INSERT = "com.baomidou.mybatisplus.mapper.SqlRunner.Insert";
    private static final String DELETE = "com.baomidou.mybatisplus.mapper.SqlRunner.Delete";
    private static final String UPDATE = "com.baomidou.mybatisplus.mapper.SqlRunner.Update";
    private static final String SELECT_LIST = "com.baomidou.mybatisplus.mapper.SqlRunner.SelectList";
    private static final String SELECT_OBJS = "com.baomidou.mybatisplus.mapper.SqlRunner.SelectObjs";
    private static final String COUNT = "com.baomidou.mybatisplus.mapper.SqlRunner.Count";
    private static final String SQLScript = "${sql}";
    private static final String SQL = "sql";
    private SqlSessionFactory factory;

    public CommonMapper(SqlSessionFactory factory) {
        this.factory = factory;
    }

    public boolean insert(String sql, Object... args) {
        return SqlHelper.retBool(sqlSession().insert(INSERT, sqlMap(sql, args)));
    }

    public boolean delete(String sql, Object... args) {
        return SqlHelper.retBool(sqlSession().delete(DELETE, sqlMap(sql, args)));
    }

    /**
     * 获取sqlMap参数
     *
     * @param sql
     * @param args
     * @return
     */
    private Map<String, String> sqlMap(String sql, Object... args) {
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put(SQL, StringUtils.sqlArgsFill(sql, args));
        return sqlMap;
    }

    public boolean update(String sql, Object... args) {
        return SqlHelper.retBool(sqlSession().update(UPDATE, sqlMap(sql, args)));
    }

    public List<Map<String, Object>> selectList(String sql, Object... args) {
        return sqlSession().selectList(SELECT_LIST, sqlMap(sql, args));
    }

    public List<Object> selectObjs(String sql, Object... args) {
        return sqlSession().selectList(SELECT_OBJS, sqlMap(sql, args));
    }

    public Object selectObj(String sql, Object... args) {
        return SqlHelper.getObject(selectObjs(sql, args));
    }

    public int selectCount(String sql, Object... args) {
        return SqlHelper.retCount(sqlSession().<Integer>selectOne(COUNT, sqlMap(sql, args)));
    }

    public Map<String, Object> selectOne(String sql, Object... args) {
        return SqlHelper.getObject(selectList(sql, args));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Page<Map<String, Object>> selectPage(Page page, String sql, Object... args) {
        if (null == page) {
            return null;
        }
        page.setRecords(sqlSession().selectList(SELECT_LIST, sqlMap(sql, args), page));
        return page;
    }

    /**
     * <p>
     * 获取Session 默认自动提交
     * <p/>
     */
    private SqlSession sqlSession() {
        return GlobalConfigUtils.getSqlSession(factory.getConfiguration());
    }
}
