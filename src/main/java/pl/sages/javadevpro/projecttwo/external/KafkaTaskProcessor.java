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
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnvMapper;

@Service
@RequiredArgsConstructor
public class KafkaTaskProcessor {

    private final KafkaTemplate<String, TaskEnv> kafkaTemplate;
    private final TaskExecutor taskExecutor;
    private final TaskEnvMapper taskEnvMapper;

    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
    public void onReceive(TaskEnv taskEnv) {
        var executionResult = taskExecutor.execute(taskEnvMapper.toDomain(taskEnv));
        var taskStatus =  executionResult == ExecutionStatus.COMPLETED ? TaskStatus.COMPLETED : TaskStatus.FAILED;
        var task = taskEnvMapper.toDomain(taskEnv).withStatus(taskStatus);
        kafkaTemplate.send(KafkaConfiguration.TASKS_OUTBOUND_TOPIC, taskEnvMapper.toDto(task));
    }

}
