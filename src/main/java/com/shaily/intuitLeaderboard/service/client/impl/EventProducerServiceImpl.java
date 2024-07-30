package com.shaily.intuitLeaderboard.service.client.impl;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.MessageQueueException;
import com.shaily.intuitLeaderboard.service.client.EventProducerService;
import com.shaily.intuitLeaderboard.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EventProducerServiceImpl implements EventProducerService<Score> {

    private final KafkaTemplate<String, Score> kafkaTemplate;

    @Autowired
    public EventProducerServiceImpl(KafkaTemplate<String, Score> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void addEventToQueue(Score newEvent) throws MessageQueueException {
        try {
            kafkaTemplate.send(Constants.KAFKA_TOPIC, newEvent);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka topic '{}': {}", Constants.KAFKA_TOPIC, e.getMessage(), e);
            throw new MessageQueueException("Failed to send message to Kafka topic", e);
        }
    }
}
