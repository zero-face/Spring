package com.zero.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.zero.dao.UserPasswordDaoMapper;
import com.zero.dao.userDaoMapper;
import com.zero.dataobject.UserPasswordDao;
import com.zero.dataobject.userDao;
import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.service.UserService;
import com.zero.service.model.UserModel;
import com.zero.validator.ValidationResult;
import com.zero.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 17:50
 * @since: jdk 1.8
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private userDaoMapper userDaoMapper;
    @Autowired
    private UserPasswordDaoMapper userPasswordDaoMapper;
    @Autowired
    private ValidatorImpl validator;


    @Override
    public UserModel getUserById(Integer id) {
        userDao userDao = userDaoMapper.selectByPrimaryKey(id);
        if(userDao == null) {
            return null;
        }
        //先改写根据id获取密码对象
        //然后再通过获得的对象id传入查找对象的密码
        UserPasswordDao userPasswordDao = userPasswordDaoMapper.selectByUserId(userDao.getId());
        return convertFromDataObject(userDao,userPasswordDao);
    }

    //将userDao和userpasswordDao对象合成一个UserModel对象
    private UserModel convertFromDataObject(userDao userDao, UserPasswordDao userPasswordDao) {
        if(userDao == null) {
            return null;
        }
        //创建一个userModel对象
        UserModel userModel = new UserModel();
        //使用BeanUtils中的拷贝方法，将userdao拷贝到给userModel中
        BeanUtils.copyProperties(userDao,userModel);
        if(userPasswordDao != null) {
            //再单独将密码这个属性赋给userModel对象
            userModel.setEncrptPassword(userPasswordDao.getEncrptPassword());
        }

        return userModel;
    }
    //注册业务具体实现
    @Override
    @Transactional //事务标签，标记为事务，要么里面的所有任务全部执行，要么全部不执行（因为这里需要将用户身份信息和密码信息同时填入两个表中）
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null) { //如果传入的这个model是空（意味着没有拿到前端的用户信息），就直接报错
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        //if(StringUtils.isEmpty(userModel.getName())
        //    || userModel.getGender() == null
        //    || userModel.getAge() == null
        //    || userModel.getEncrptPassword() == null
        //    || userModel.getTelephone() == null) {
        //    throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        //}
        //将userModel丢进validator进行校验，返回校验结果
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrs()) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model到dataobject的转换
        userDao userDao1 = convertFromModel(userModel);

        try { //设置了手机号为唯一主键，那么当插入相同的手机，那么就会抛出异常：用户已存在
            userDaoMapper.insert(userDao1);
        } catch (DuplicateKeyException exception) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户已存在");
        }

        userModel.setId(userDao1.getId());
        //实现model到dataobject中的password转换
        UserPasswordDao userPasswordDao = convertPasswordFromModel(userModel);
        userPasswordDaoMapper.insert(userPasswordDao);
    }
    //从model中还原userdao
    private userDao convertFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        userDao userDao = new userDao();
        BeanUtils.copyProperties(userModel,userDao);
        return userDao;
    }
    //从model中还原userpassworddao
    private UserPasswordDao convertPasswordFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        UserPasswordDao userPasswordDao = new UserPasswordDao();
        userPasswordDao.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDao.setUserId(userModel.getId());
        return userPasswordDao;
    }

    //登录业务的实现
    @Override
    public UserModel validateLogin(String telephone, String encrptpassword) throws BusinessException {
        //通过手机拿到用户的信息
        userDao userDao = userDaoMapper.selectByTelephone(telephone);
        if(userDao == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户手机或密码不正确");
        }
        UserPasswordDao userPasswordDao = userPasswordDaoMapper.selectByUserId(userDao.getId());
        UserModel userModel = convertFromDataObject(userDao, userPasswordDao);
        //比对密码是否相匹配
        if(!StringUtils.equals(encrptpassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户名或者密码错误");
        }
        return userModel;
    }


}
