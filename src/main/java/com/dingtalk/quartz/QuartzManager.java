package com.dingtalk.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import lombok.SneakyThrows;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：Quartz管理器
 * @date ：Created in 2021/4/27
 */
public class QuartzManager {

    /**
     * 创建定时任务 定时任务创建之后默认启动状态
     *
     * @param scheduler  调度器
     * @param quartzBean 定时任务信息类
     */
    @SneakyThrows
    public static void createScheduleJob(Scheduler scheduler, QuartzBean quartzBean) {
        // 构建定时任务信息
        JobDetail jobDetail = JobBuilder.newJob(quartzBean.getJobClass())
                .withIdentity(quartzBean.getJobId()).build();
        // 加入任务信息
        jobDetail.getJobDataMap().put("jobInfo", quartzBean.getJobInfo());
        // 设置定时任务执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzBean.getCron());
        // 构建触发器trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().
                withIdentity(quartzBean.getJobId()).withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 根据任务编号暂停定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务编号
     */
    @SneakyThrows
    public static void pauseScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 根据任务编号恢复定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务编号
     */
    @SneakyThrows
    public static void resumeScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 根据任务编号立即运行一次定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务编号
     */
    @SneakyThrows
    public static void runOnce(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新定时任务
     *
     * @param scheduler  调度器
     * @param quartzBean 定时任务信息类
     */
    @SneakyThrows
    public static void updateScheduleJob(Scheduler scheduler, QuartzBean quartzBean) {
        // 获取到对应任务的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzBean.getJobId());
        // 设置定时任务执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzBean.getCron());
        // 重新构建任务的触发器trigger
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 重置对应的job
        scheduler.rescheduleJob(triggerKey, trigger);

    }

    /**
     * 根据定时任务编号从调度器当中删除定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务编号
     */
    @SneakyThrows
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.deleteJob(jobKey);
    }

}