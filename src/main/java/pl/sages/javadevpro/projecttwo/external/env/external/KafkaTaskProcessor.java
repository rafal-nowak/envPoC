package pl.sages.javadevpro.projecttwo.external.env.external;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.external.env.domain.ExecutionStatus;
import pl.sages.javadevpro.projecttwo.external.env.domain.Task;
import pl.sages.javadevpro.projecttwo.external.env.domain.TaskExecutor;
import pl.sages.javadevpro.projecttwo.external.env.domain.TaskProcessor;

import static pl.sages.javadevpro.projecttwo.external.env.config.KafkaConfiguration.*;
import static pl.sages.javadevpro.projecttwo.external.env.domain.TaskStatus.COMPLETED;
import static pl.sages.javadevpro.projecttwo.external.env.domain.TaskStatus.FAILED;

@Service
@RequiredArgsConstructor
public class KafkaTaskProcessor implements TaskProcessor {

    private final KafkaTemplate<String, Task> kafkaTemplate;
    private final TaskExecutor taskExecutor;

    @KafkaListener(topics = TASKS_INBOUND_TOPIC, groupId = KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
    public void onReceive(Task task) {
        var executionResult = taskExecutor.execute(task);
        var taskStatus =  executionResult == ExecutionStatus.COMPLETED ? COMPLETED : FAILED;
        kafkaTemplate.send(TASKS_OUTBOUND_TOPIC, task.withStatus(taskStatus));
    }

}
