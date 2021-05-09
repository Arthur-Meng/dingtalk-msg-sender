package com.dingtalk.quartz;

import org.quartz.Job;

import lombok.Builder;
import lombok.Data;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：Quartz任务数据
 * @date ：Created in 2021/4/27
 */
@Data
@Builder
public class QuartzBean {

    /**
     * 任务id
     */
    private String jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务执行类
     */
    private Class<? extends Job> jobClass;
    /**
     * 任务信息
     */
    private Object jobInfo;
    /**
     * 任务运行时间表达式
     */
    private String cron;

}