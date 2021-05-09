package com.dingtalk.job;

import java.util.List;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dingtalk.quartz.QuartzBean;
import com.dingtalk.quartz.QuartzManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：ItsmJob任务管理器
 * @date ：Created in 2021/4/27
 */
@Slf4j
@Component
public class TestJobManager implements ApplicationRunner {

    @Autowired
    private TestJobConfig testJobConfig;
    @Autowired
    private Scheduler scheduler;

    @Override
    @SuppressWarnings("all")
    public void run(ApplicationArguments args) throws Exception {
        // 读取配置中的任务
        List<TestJobInfo> jobInfoList = testJobConfig.getJob();
        for (int i = 0; i < jobInfoList.size(); i++) {
            TestJobInfo jobInfo = jobInfoList.get(i);
            // 构造调度任务
            QuartzBean quartzBean = QuartzBean.builder()
                    .jobId("TestJob" + i)
                    .jobName("测试任务" + i)
                    .jobClass(TestJob.class)
                    .cron(jobInfo.getCron())
                    .jobInfo(jobInfo)
                    .build();
            // 加入调度
            QuartzManager.createScheduleJob(scheduler, quartzBean);
            log.info("增加定时任务[{}],详细信息:{}", quartzBean.getJobId(), JSON.toJSONString(jobInfo));
        }
    }

}