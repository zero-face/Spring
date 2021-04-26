package com.zero.controller;




import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.response.CommonReturnType;
import com.zero.service.OrderService;
import com.zero.service.model.OrderModel;
import com.zero.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 23:09
 * @since: jdk 1.8
 */
@Controller("/order")
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;



    //封装下单请求
    @RequestMapping(value = "/createorder",method={RequestMethod.GET},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,
                                        @RequestParam(name="amount") Integer amount) throws BusinessException {
//        获取用户登录信息
//        Boolean bl = (Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
//
//        if(bl == null || !bl.booleanValue()) {
//            throw new BusinessException(EmBustinessError.USER_NOT_LOGIN);
//        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount);
        return CommonReturnType.create(null);
    }
}
