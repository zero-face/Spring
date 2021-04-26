package com.zero.service.impl;

import com.zero.dao.OrderDaoMapper;
import com.zero.dao.SequenceDaoMapper;
import com.zero.dataobject.OrderDao;
import com.zero.dataobject.SequenceDao;
import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.service.ItemService;
import com.zero.service.OrderService;
import com.zero.service.UserService;
import com.zero.service.model.ItemModel;
import com.zero.service.model.OrderModel;
import com.zero.service.model.UserModel;
import com.zero.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 21:18
 * @since: jdk 1.8
 */
@Service
public class OrcderServiceImpl implements OrderService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private OrderDaoMapper orderDaoMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private SequenceDaoMapper sequenceDaoMapper;


    private OrderDao convertFromOrderModel(OrderModel orderModel) {
        if(orderModel == null) {
            return null;
        }
        OrderDao orderDao = new OrderDao();
        BeanUtils.copyProperties(orderModel,orderDao);
        orderDao.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDao.setOrderAmount(orderModel.getOrderAmount().doubleValue());
        orderDao.setItenId(orderModel.getItemId());
        return orderDao;
    }



    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //校验下单状态（商品是否存在，用户是否合法，购买数量是否正确）
        //1.检验商品是否存在
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        //2.校验用户是否合法
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        //3.校验数量
        if(amount <=0 || amount > 99) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }
        //落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result) {
            throw new BusinessException(EmBustinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setAmount(amount);
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setItemPrice(BigDecimal.valueOf(itemModel.getPrice()));
        BigDecimal res = BigDecimal.valueOf(itemModel.getPrice()).multiply(new BigDecimal(amount));
        orderModel.setOrderAmount(res);
        //生成交易流水号
        orderModel.setId(generateOrderNo());
        //将核心领域的model转化为数据object
        OrderDao orderDao = convertFromOrderModel(orderModel);
        //入库
        orderDaoMapper.insert(orderDao);
        //增加销量
        itemService.increaseSales(itemId,amount);
        //返回前端
        return orderModel;
    }
    //生成订单号
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {
        //订单16位，前8位是时间信息，中间6位是自增序列，最后两位是分库分表位
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        SequenceDao sequenceDao = sequenceDaoMapper.getSequeceByName("order_info");
        int currentValue = sequenceDao.getCurrentValue();
        sequenceDao.setCurrentValue(sequenceDao.getCurrentValue()+sequenceDao.getStep());
        sequenceDaoMapper.updateByPrimaryKey(sequenceDao);
        String sequenceStr = String.valueOf(currentValue);
        for(int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
}
