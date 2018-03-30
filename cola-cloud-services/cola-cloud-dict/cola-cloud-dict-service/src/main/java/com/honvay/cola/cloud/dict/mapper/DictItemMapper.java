package com.honvay.cola.cloud.dict.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.honvay.cola.cloud.dict.entity.DictItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
  * 字典表 Mapper 接口
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
public interface DictItemMapper extends BaseMapper<DictItem> {

    /**
     * 通过字典编号获取字典项
     * @param code
     * @return
     */
    @Select("select * from adi_dict_item t " +
            " where t.dict_id in (select id from adi_dict d where d.code = #{code} order by order_no)")
    public List<DictItem> selectListByDictCode(@Param("code") String code);
}