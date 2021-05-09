package com.dingtalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：启动类
 * @date ：Created in 2021-04-27
 */
@EnableScheduling
@SpringBootApplication
public class MsgSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsgSenderApplication.class, args);
    }

}
