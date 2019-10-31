package org.xxz.test.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author jsbxyyx
 */
public interface TestUuidMapper extends InsertListMapper<TestUuid> {

    @Insert("<script>" +
        "insert into test_uuid (id, name) values" +
        "<foreach collection='list' item='item' index='index' open='' close='' separator=','>" +
        "(#{item.id}, #{item.name})" +
        "</foreach>" +
        "</script>")
    int batchInsert(@Param("list") List<TestUuid> list);

}
