package com.arbiter.service.configuration;

import com.arbiter.service.task.LikeTask;
import com.arbiter.service.task.ViewTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";

    private static final String VIEW_TASK_IDENTITY = "ViewTaskQuartz";

    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(LikeTask.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
        //        .withIntervalInSeconds(30)  //设置时间周期单位秒
                .withIntervalInHours(1)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    // 新增的第二个定时任务配置
    @Bean
    public JobDetail viewTaskQuartzDetail() {
        return JobBuilder.newJob(ViewTask.class) // 假设这是你的新任务类
                .withIdentity(VIEW_TASK_IDENTITY)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger viewTaskTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
        //      .withIntervalInSeconds(30) // 30秒执行一次
                .withIntervalInHours(1)  //两个小时执行一次
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(viewTaskQuartzDetail())
                .withIdentity(VIEW_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
