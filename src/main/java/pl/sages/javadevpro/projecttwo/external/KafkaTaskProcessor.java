package pl.sages.javadevpro.projecttwo.external;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.config.KafkaConfiguration;
import pl.sages.javadevpro.projecttwo.domain.ExecutionStatus;
import pl.sages.javadevpro.projecttwo.domain.TaskExecutor;
import pl.sages.javadevpro.projecttwo.domain.TaskProcessor;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.task.TaskStatus;

@Service
@RequiredArgsConstructor
public class KafkaTaskProcessor implements TaskProcessor {

    private final KafkaTemplate<String, Task> kafkaTemplate;
    private final TaskExecutor taskExecutor;

    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
    public void onReceive(Task task) {
        var executionResult = taskExecutor.execute(task);
        var taskStatus =  executionResult == ExecutionStatus.COMPLETED ? TaskStatus.COMPLETED : TaskStatus.FAILED;
        kafkaTemplate.send(KafkaConfiguration.TASKS_OUTBOUND_TOPIC, task.withStatus(taskStatus));
    }

}
