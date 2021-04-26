package com.zero.servlet;

import com.zero.controller.BaseController;
import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.response.CommonReturnType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;



/**
 * @Author Zero
 * @Date 2021/4/12 9:17
 * @Since 1.8
 **/
@Slf4j
//@WebFilter("/file/upload") //制定拦截路径
public class MyFilter extends BaseController implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("myfilter初始化完成");
    }
    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        log.info("工作");
    }

    @Override
    public void destroy() {
        log.info("销毁");
    }
}
