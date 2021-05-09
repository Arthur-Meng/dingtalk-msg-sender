package com.dingtalk.job;

import lombok.Data;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：测试任务信息
 * @date ：Created in 2021/4/27
 */
@Data
public class TestJobInfo {

    /**
     * 测试任务的标题
     */
    private String title;
    /**
     * 测试任务的内容
     */
    private String content;
    /**
     * 测试任务执行的cron表达式
     */
    private String cron;
    /**
     * 测试任务通知群中的需要@的人的手机号，多个用逗号分隔
     */
    private String atUserPhone;
    /**
     * 测试任务通知的群机器人的webhook
     */
    private String webhook;
    /**
     * 测试任务通知的群机器人的robotSecret
     */
    private String robotSecret;

}