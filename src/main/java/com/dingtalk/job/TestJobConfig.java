package com.dingtalk.job;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：测试任务信息配置类
 * @date ：Created in 2021/4/27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "test")
public class TestJobConfig {

    /**
     * 测试任务信息集合
     */
    private List<TestJobInfo> job = new ArrayList<>();

}