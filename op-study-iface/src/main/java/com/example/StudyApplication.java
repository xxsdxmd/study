package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xxs
 * @Date 2024/6/30 14:59
 */
@SpringBootApplication(exclude = {
        org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration.class
})
@EnableCaching
@ComponentScan(basePackages = {
        "com.example",
        "op.study",
        "domain"
})
@MapperScan("op.study.infra.db.mp.mapper")
public class StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
        System.out.println("=================================");
        System.out.println("用户域DDD项目启动成功！");
        System.out.println("Swagger文档地址: http://localhost:8080/swagger-ui.html");
        System.out.println("=================================");
    }
} 