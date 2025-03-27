package com.arbiter.service.task;

import com.arbiter.common.constant.RedisConstant;
import com.arbiter.common.util.RedisLikeUtil;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.repository.RedisViewRepository;
import com.arbiter.service.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class ViewTask extends QuartzJobBean {
    private final RedisViewRepository redisViewRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("ViewTask-------- {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        redisViewRepository.setViewCount();

    }
}
