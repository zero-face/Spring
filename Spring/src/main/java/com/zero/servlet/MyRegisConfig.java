package com.zero.servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @Author Zero
 * @Date 2021/4/12 9:50
 * @Since 1.8
 **/
@Configuration(proxyBeanMethods = true) //有组件依赖
public class MyRegisConfig {
    @Bean
    public ServletRegistrationBean myServlet() {
        MyServlet myServlet = new MyServlet();

        return new ServletRegistrationBean(myServlet,"/file/upload");
    }
    @Bean
    public FilterRegistrationBean myFilter() {
        MyFilter myFilter = new MyFilter();
//        return new FilterRegistrationBean(myFilter,myServlet());//拦截servlet路径
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter); //自定义拦截路径
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my","/css/*"));
        return filterRegistrationBean;
    }
    @Bean
    public ServletListenerRegistrationBean myListenler() {
        Mylistener myListener = new Mylistener();
        return new ServletListenerRegistrationBean(myListener);

    }
}
