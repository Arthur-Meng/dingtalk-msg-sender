package com.dingtalk.job;

import java.util.Arrays;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.dingtalk.util.MsgSender;

import lombok.extern.slf4j.Slf4j;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：测试任务
 * @date ：Created in 2021/4/27
 */
@Slf4j
@Component
public class TestJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TestJobInfo jobInfo = (TestJobInfo) jobExecutionContext.getJobDetail().getJobDataMap().get("jobInfo");
        MsgSender.sendTextToRobot(jobInfo.getTitle(),
                Arrays.asList(jobInfo.getContent().split(",")),
                Arrays.asList(jobInfo.getAtUserPhone().split(",")),
                jobInfo.getWebhook(),
                jobInfo.getRobotSecret());
        log.info("发送测试消息成功！");
    }
}