package com.zero.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @Author Zero
 * @Date 2021/4/12 9:23
 * @Since 1.8
 **/
@Slf4j
//@WebListener //项目启动监听器
public class Mylistener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("项目启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("项目销毁");
    }
}
