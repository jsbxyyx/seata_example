package org.xxz.test.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jsbxyyx
 */
@Mapper
public interface Test1Mapper {

    int save(Test1 param);

    int saveOracle(Test1 param);

    int saveList(@Param("list") List<Test1> list);

}
