package org.xxz.test.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author jsbxyyx
 */
@Mapper
public interface Test1Mapper {

    int save(Test1 param);

    int saveOracle(Test1 param);

    int saveList(@Param("list") List<Test1> list);

    @Insert("insert into test_escape values(#{sid}, #{param}, #{createTime})")
    int save1(@Param("sid") String sid, @Param("param") String param, @Param("createTime") Date createTime);

}
