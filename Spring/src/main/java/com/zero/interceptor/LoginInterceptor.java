package com.zero.interceptor;

import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Zero
 * @Date 2021/4/10 21:35
 * @Since 1.8
 **/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截的请求：" + requestURI);
        Object is_login = request.getSession().getAttribute("IS_LOGIN");
        if(is_login != null) {
            System.out.println(request.getSession().getAttribute("LOGIN_USER"));
            return true;
        }
        throw new BusinessException(EmBustinessError.USER_NOT_LOGIN);
    }
}
