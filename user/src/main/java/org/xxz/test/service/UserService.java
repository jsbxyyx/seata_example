package org.xxz.test.service;

import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxz.test.dao.UserRepository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class UserService {

    static final AtomicInteger counter = new AtomicInteger(1);

    @Autowired
    UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public int createUser(int userId, String name) {
        String xid = RootContext.getXID();
        System.out.println("user xid=" + xid);

        int count = counter.getAndIncrement();
        if (count % 2 == 0) {
            throw new RuntimeException("exception");
        }

        return userRepository.save(userId, name);
    }

}

