package com.zero.service;

import com.zero.error.BusinessException;
import com.zero.service.model.UserModel;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 17:50
 * @since: jdk 1.8
 */
public interface UserService {
    //根据id获取用户信息
    UserModel getUserById(Integer id);
    //用户注册业务
    void register(UserModel userModel) throws BusinessException;
    //登录服务
    UserModel validateLogin(String telephone,String encrptpassword) throws BusinessException;
}
