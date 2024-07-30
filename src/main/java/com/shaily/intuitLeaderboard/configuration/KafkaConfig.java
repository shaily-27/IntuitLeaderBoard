package com.shaily.intuitLeaderboard.configuration;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.shaily.intuitLeaderboard.utils.Constants.KAFKA_ENDPOINT;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic() {
        return new NewTopic(Constants.KAFKA_TOPIC, 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, Score> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ENDPOINT);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Score> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
