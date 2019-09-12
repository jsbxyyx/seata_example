package org.xxz.test.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface TkMapper extends InsertListMapper<TkTest> {

    @Update("<script>" +
            "<foreach collection='list' item='item' index='index' open='' close='' separator=';'>" +
            "update tk_test" +
            "<set>" +
            "name=#{item.name}," +
            "name2=#{item.name2}" +
            "</set>" +
            "where id = #{item.id}" +
            "</foreach>" +
            "</script>")
    int batchUpdate(@Param("list") List<TkTest> list);

}
