package com.arbiter.service.task;

import com.arbiter.service.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Slf4j
public class LikeTask extends QuartzJobBean {

    private final LikeService likeService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("LikeTask-------- {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        likeService.transLikedFromRedis2DB();
        likeService.transLikedCountFromRedis2DB();
    }
}
