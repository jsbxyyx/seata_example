package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxz.test.dao.Test1Dao;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class Test1Service {

    @Autowired
    Test1Dao test1Dao;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test3() {
        test1Dao.select();
        test1Dao.update();
    }

}

