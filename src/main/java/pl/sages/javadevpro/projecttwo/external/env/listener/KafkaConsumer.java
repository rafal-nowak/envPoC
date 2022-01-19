package pl.sages.javadevpro.projecttwo.external.env.listener;


import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskStatusEnv;
import pl.sages.javadevpro.projecttwo.external.env.executor.TaskExecutor;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;


@Service
@AllArgsConstructor
public class KafkaConsumer {

    private KafkaTemplate<String, UserTaskEnv> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_Report_json";


    @KafkaListener(topics = "Kafka_Task_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(UserTaskEnv task) {

        System.out.println("Consumed JSON Task: " + task);

        TaskExecutor taskExecutor = new TaskExecutor(task);
        var result = taskExecutor.execute();

        task.setTaskStatus(result == 0 ? UserTaskStatusEnv.COMPLETED : UserTaskStatusEnv.FAILED);

        kafkaTemplate.send(TOPIC, task);
        System.out.println("Finished JSON Task: " + task);

    }
}