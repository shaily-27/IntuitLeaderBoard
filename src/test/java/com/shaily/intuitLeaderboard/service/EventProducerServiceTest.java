package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.MessageQueueException;
import com.shaily.intuitLeaderboard.service.client.impl.EventProducerServiceImpl;
import com.shaily.intuitLeaderboard.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EventProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Score> kafkaTemplate;

    @InjectMocks
    private EventProducerServiceImpl eventProducerService;

    @Test
    void testAddEventToQueue_success() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        CompletableFuture<SendResult<String, Score>> mockFuture = mock(CompletableFuture.class);
        when(kafkaTemplate.send(Constants.KAFKA_TOPIC, score)).thenReturn(mockFuture);
        assertDoesNotThrow(() -> eventProducerService.addEventToQueue(score));
    }

    @Test
    void testAddEventToQueue_failure() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(Constants.KAFKA_TOPIC, score);
        assertThrows(MessageQueueException.class, () -> eventProducerService.addEventToQueue(score));
    }
}
