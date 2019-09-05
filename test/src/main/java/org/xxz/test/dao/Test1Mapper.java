package org.xxz.test.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author jsbxyyx
 */
@Mapper
public interface Test1Mapper {

    int save(Test1Param param);

}
