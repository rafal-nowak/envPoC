package pl.sages.jdp.envpoc.listener;


import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.sages.jdp.envpoc.executor.TaskExecutor;
import pl.sages.jdp.envpoc.model.Task;


@Service
@AllArgsConstructor
public class KafkaConsumer {

    private KafkaTemplate<String, Task> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_Report_json";


    @KafkaListener(topics = "Kafka_Task_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(Task task) {

        System.out.println("Consumed JSON Task: " + task);

        TaskExecutor taskExecutor = new TaskExecutor(task);
        taskExecutor.execute();

        task.setTaskStatus("finished");

        kafkaTemplate.send(TOPIC, task);
        System.out.println("Finished JSON Task: " + task);

    }
}
