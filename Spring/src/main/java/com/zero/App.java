package com.zero;

import org.apache.catalina.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Hello world!
 *
 */
@ServletComponentScan(basePackages = "com.zero.servlet") //指定原生servlet组件都放在哪里
@SpringBootApplication(scanBasePackages = {"com.zero"})
@MapperScan("com.zero.dao")
//@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }
}
