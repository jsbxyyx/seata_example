package org.xxz.test.service;

import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxz.test.dao.OrderRepository;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Transactional(rollbackFor = Exception.class)
    public int createOrder(int userId, double amount) {
        String xid = RootContext.getXID();
        System.out.println("order xid=" + xid);
        return orderRepository.save(userId, amount);
    }

}

