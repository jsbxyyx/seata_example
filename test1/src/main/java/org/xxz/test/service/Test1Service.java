package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxz.test.dao.Test1Dao;

import java.util.Random;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class Test1Service {

    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        int i = new Random().nextInt(1000);
        if (i % 2 == 0) {
            throw new RuntimeException("rollback");
        }
    }

    @Autowired
    Test1Dao test1Dao;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test3() {
        test1Dao.select();
        test1Dao.update();
    }

}

