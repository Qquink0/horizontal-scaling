package com.example.horizontalscaling.services;

import com.example.horizontalscaling.config.RedisLock;
import com.example.horizontalscaling.rabbit.TaskSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class InitializerService {

    RedisLock redisLock;

    TaskSender taskSender;

    private static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    private static final String GENERATE_TASK_KEY = "horizontal:scaling:generate:tasks";

    @Scheduled(cron = "* * * * * *")
    public void generateTask() {

        if (redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, GENERATE_TASK_KEY)) {

            for (int i = 0; i < 100; i++) {


            }

        redisLock.releaseLock(GENERATE_TASK_KEY);
        }

    }
}
