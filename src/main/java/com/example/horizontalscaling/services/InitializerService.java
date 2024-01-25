package com.example.horizontalscaling.services;

import com.example.horizontalscaling.config.RedisLock;
import com.example.horizontalscaling.domains.Task;
import com.example.horizontalscaling.rabbit.TaskSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class InitializerService {

    public static final String SERVICE_ID = UUID.randomUUID().toString();

    RedisLock redisLock;

    TaskSender taskSender;

    private static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    private static final String GENERATE_TASK_KEY = "horizontal:scaling:generate:tasks";

    @Scheduled(fixedDelay = 1000L)
    public void generateTask() {

        if (redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, GENERATE_TASK_KEY)) {

            log.info(Strings.repeat("-", 100));
            log.info(String.format("Service \"%s\" start generate tasks", SERVICE_ID));

            for (int i = 0; i < 5; i++) {
                taskSender.sentTask(
                        Task.builder()
                                .id(UUID.randomUUID().toString())
                                .fromServer(SERVICE_ID)
                                .build()
                );
            }

            log.info(String.format("Service \"%s\" start generate tasks", SERVICE_ID));
            log.info(Strings.repeat("-", 100));

            redisLock.releaseLock(GENERATE_TASK_KEY);
        }

    }
}
