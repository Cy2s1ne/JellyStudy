package com.hzcu.jellystudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

/**
 * Question Answer 模块启动类
 */
@SpringBootApplication
@EnableDubbo
public class QuestionAnswerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionAnswerApplication.class, args);
    }
}