package com.example.horizontalscaling.rabbit;

import com.example.horizontalscaling.domains.Task;
import com.example.horizontalscaling.services.InitializerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class TaskListener {

    @Value("${server.port}")
    @NonFinal
    Integer port;

    public static final String TASK_QUEUE = "task.queue";
    public static final String TASK_EXCHANGE = "task.exchange";

    // Прием задачи
    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = TASK_QUEUE, durable = Exchange.TRUE, autoDelete = Exchange.TRUE),
                    exchange = @Exchange(value = TASK_EXCHANGE, autoDelete = Exchange.TRUE)
            )
    )
    public void handleTask(Task task) {

        Thread.sleep(15_000);

        log.info(
                String.format(
                        "Service \"%s\" end process task \"%s\" from service \"%s\"",
                        port,
                        task.getId(),
                        task.getFromServerPort()
                        )
        );
    }
}
