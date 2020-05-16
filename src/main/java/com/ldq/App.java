package com.ldq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
@MapperScan("com.ldq.mapper")
@ServletComponentScan
public class App 
{
    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }
}
