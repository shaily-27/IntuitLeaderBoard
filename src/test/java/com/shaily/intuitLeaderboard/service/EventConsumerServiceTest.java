package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.service.impl.EventConsumerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EventConsumerServiceTest {

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private EventConsumerServiceImpl eventConsumerService;

    @Test
    public void testConsumeEventFromQueue_validEvent() throws DatabaseStorageException {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        eventConsumerService.consumeEventFromQueue(score);
        verify(scoreService).updateScore(score);
    }

    @Test
    public void testConsumeEventFromQueue_nullEvent() throws DatabaseStorageException {
        eventConsumerService.consumeEventFromQueue(null);
        verify(scoreService, never()).updateScore(any());
    }

    @Test
    public void testConsumeEventFromQueue_DatabaseStorageException() throws DatabaseStorageException {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        doThrow(new DatabaseStorageException("Database error")).when(scoreService).updateScore(score);
        eventConsumerService.consumeEventFromQueue(score);
        verify(scoreService).updateScore(score);
    }

    @Test
    public void testConsumeEventFromQueue_GenericException() throws DatabaseStorageException {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        doThrow(new RuntimeException("Generic error")).when(scoreService).updateScore(score);
        eventConsumerService.consumeEventFromQueue(score);
        verify(scoreService).updateScore(score);
    }

}
