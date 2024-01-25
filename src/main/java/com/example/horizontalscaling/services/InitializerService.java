package com.example.horizontalscaling.services;

import com.example.horizontalscaling.config.RedisLock;
import com.example.horizontalscaling.domains.Task;
import com.example.horizontalscaling.rabbit.TaskSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class InitializerService {

    @Value("${server.port}")
    @NonFinal Integer port;

    RedisLock redisLock;

    TaskSender taskSender;

    private static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    private static final String GENERATE_TASK_KEY = "horizontal:scaling:generate:tasks";

    @Scheduled(cron = "0/15 * * * * *")
    public void generateTask() {

        if (redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, GENERATE_TASK_KEY)) {

            log.info(Strings.repeat("-", 100));
            log.info(String.format("Service \"%s\" start generate tasks", port));

            for (int i = 0; i < 5; i++) {
                taskSender.sentTask(
                        Task.builder()
                                .id(generateShortId())
                                .fromServerPort(port)
                                .build()
                );
            }

            log.info(String.format("Service \"%s\" end generate tasks", port));
            log.info(Strings.repeat("-", 100));

            redisLock.releaseLock(GENERATE_TASK_KEY);
        }
    }
    private static String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 4);
    }
}
