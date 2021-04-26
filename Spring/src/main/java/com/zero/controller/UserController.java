package com.zero.controller;

import com.alibaba.druid.util.StringUtils;
import com.zero.viewobject.UserVo;
import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.service.UserService;
import com.zero.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.zero.response.CommonReturnType;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 17:49
 * @since: jdk 1.8
 */
/**
 * @Description //TODO resetcontroller == controller + ResonseBody
 *                      如果使用的是@RestController注解那么在controller无法返回jsp或者是HTML页面，只能返return中的数据；
 *                      使用@Controller注解，视图解析器可以解析HTML页面，并且跳转到相应的页面，如果要返回json等内容到页面，那么就需要加在方法上ResponseBody注解
 *
 *                    @Autowired注解（类型优先注入）：自动注入对象；注入过程是：首先去spring容器中找到对应的实现类，然后将其注入，如果出现多个实现类，那么就会出现不知道注入哪个实现类的情况
 *                      如果出现多个实现类，可以使用 如Qualifier(“DemoImpl2”) 指定所需要的DemoImpl2。但是需要在对应的类上使用component注解来将类注册为spring中的bean对象。
 *                    @Resource(名字优先注入):1.会先在spring容器中找到名字相同的类（byName），即使有多个实现类，但是只需要在spring中注册时，使用了（如demo）对该实现类进行命名，则会自动找到该类
 *                      2.如果类没有命名，则会根据类型查找它的实现类（byType），如果不唯一，则报错
 *                    @component：将类注册为spring中的bean对象。（把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>）
 *                      Repository就是其扩展，就想当于把对象进行实例化。
 *                      service也是其扩展。表示service层的实现
 * @Author Linduse
 * @Date 19:00 2021/4/2
 * @param
 * @return
 **/
@Controller("/user")
@RequestMapping("/user")
public class UserController extends BaseController {
    private Map<String,String> map = new HashMap<>();
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户登录接口
    @RequestMapping(value = "/login",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone")String telephone,
                                  @RequestParam(name="password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {

        if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
    //校验用户登录是否合法
    UserModel userModel = userService.validateLogin(telephone,EncodeByMd5(password));
    this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
    this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
    return CommonReturnType.create(null);
    }
    //用户注册接口
    @RequestMapping(value = "/register",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender")String gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
    //验证手机号和对应的otpcode符合
    //    String inSessionOtpCode =(String) this.httpServletRequest.getSession().getAttribute(telephone);
        System.out.println("手机号为" + telephone +"的用户正在进行注册");
        String str = map.get(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,str)) { //判断了一下传过来的otp验证码是否为空，为空则不等
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr = base64Encoder.encode(messageDigest.digest(string.getBytes("utf-8")));
        return newstr;
    }


    //用户获取opt短信接口
    @RequestMapping(value = "/getotp",method= {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone) {
        //按照一定的规则生成otp验证码
        Random random = new Random();
        int anInt = random.nextInt(99999);
        anInt += 10001;
        String otpCode = String.valueOf(anInt);
        //将otp验证码同对应的用户手机号关联,使用httpsession的方式绑定他的手机号与OTPCODE
        //httpServletRequest.getSession().setAttribute(telephone,otpCode);
        map.put(telephone,otpCode);
        //将OTP验证码通过短信通道发送给用户(暂时打印)
        System.out.println("telephone:" + telephone + ",otpcode:" + otpCode);
        return CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户信息并返回给前端
        UserModel userModel = userService.getUserById(id);
        //若获取的对应用户信息不存在
        if(userModel == null) {
            //userModel.setEncrptPassword("12345");
            throw new BusinessException(EmBustinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户已对象转化为可供UI使用viewobject
        UserVo userVo = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVo);
    }
    //将模型对象再次进行处理返回给前端
    private UserVo convertFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }


}
