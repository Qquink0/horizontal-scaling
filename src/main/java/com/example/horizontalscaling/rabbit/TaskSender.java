package com.example.horizontalscaling.rabbit;

import com.example.horizontalscaling.domains.Task;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class TaskSender {

    RabbitMessagingTemplate rabbitMessagingTemplate;

    // Отправка задачи
    public void sentTask(Task task) {

        rabbitMessagingTemplate
                .convertAndSend(TaskListener.TASK_EXCHANGE, task);

    }
}
