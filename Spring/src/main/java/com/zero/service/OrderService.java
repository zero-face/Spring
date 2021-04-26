package com.zero.service;

import com.zero.error.BusinessException;
import com.zero.service.model.OrderModel;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 21:16
 * @since: jdk 1.8
 */
public interface OrderService {
    //创建交易订单
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;




}
