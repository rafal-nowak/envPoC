package pl.sages.javadevpro.projecttwo.external.env.listener;


import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.external.env.executor.TaskExecutor;
import pl.sages.javadevpro.projecttwo.external.env.executor.TaskExecutorStatus;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskStatusEnv;
import pl.sages.javadevpro.projecttwo.external.env.executor.TaskExecutorService;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

import static pl.sages.javadevpro.projecttwo.external.env.config.KafkaConfiguration.KAFKA_GROUP_ID;
import static pl.sages.javadevpro.projecttwo.external.env.config.KafkaConfiguration.KAFKA_LISTENER_TOPIC;
import static pl.sages.javadevpro.projecttwo.external.env.config.KafkaConfiguration.KAFKA_PRODUCER_TOPIC;

@Log
@Service
@AllArgsConstructor
public class KafkaService {

    private KafkaTemplate<String, UserTaskEnv> kafkaTemplate;


    @KafkaListener(topics = KAFKA_LISTENER_TOPIC, groupId = KAFKA_GROUP_ID,
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(UserTaskEnv task) {

        log.info("Consumed JSON Task: " + task);

        TaskExecutor taskExecutor = new TaskExecutorService(task);
        var result = taskExecutor.execute();

        task.setTaskStatus(result == TaskExecutorStatus.COMPLETED ? UserTaskStatusEnv.COMPLETED : UserTaskStatusEnv.FAILED);

        kafkaTemplate.send(KAFKA_PRODUCER_TOPIC, task);
        log.info("Finished JSON Task: " + task);
    }
}
