package pl.sages.javadevpro.projecttwo.external.env.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {


    public static final String KAFKA_IP_ADDRESS = "127.0.0.1:9092";
    public static final String KAFKA_GROUP_ID = "group_json";
    public static final String KAFKA_TRUSTED_PACKAGES = "*";
    public static final String KAFKA_LISTENER_TOPIC = "Kafka_Task_json";
    public static final String KAFKA_PRODUCER_TOPIC = "Kafka_Task_Report_json";

    @Bean
    public ConsumerFactory<String, UserTaskEnv> taskConsumerFactory() {
        Map<String, Object> config = new HashMap<>();


        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_IP_ADDRESS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, KAFKA_TRUSTED_PACKAGES);


        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(UserTaskEnv.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserTaskEnv> taskKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserTaskEnv> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, UserTaskEnv> taskProducerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_IP_ADDRESS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, UserTaskEnv> taskKafkaTemplate() {
        return new KafkaTemplate<>(taskProducerFactory());
    }


}
