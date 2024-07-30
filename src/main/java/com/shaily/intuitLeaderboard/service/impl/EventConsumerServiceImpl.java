package com.shaily.intuitLeaderboard.service.impl;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.service.EventConsumerService;
import com.shaily.intuitLeaderboard.service.ScoreService;
import com.shaily.intuitLeaderboard.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EventConsumerServiceImpl implements EventConsumerService<Score> {

    private final ScoreService scoreService;

    @Autowired
    public EventConsumerServiceImpl(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @KafkaListener(topics = Constants.KAFKA_TOPIC, groupId = Constants.KAFKA_GROUP_ID)
    public void consumeEventFromQueue(Score newEvent) {
        if (newEvent == null) {
            log.warn("Received null event from Kafka");
            return;
        }
        try {
            scoreService.updateScore(newEvent);
        } catch (DatabaseStorageException e) {
            log.error("Database error while updating score for player ID: " + newEvent.getPlayerId() + " - " + e.getMessage());
        } catch (Exception e) {
            log.error("Error while processing score for player ID: " + e.getMessage());
        }
        log.debug("Published : " + newEvent);
    }
}
