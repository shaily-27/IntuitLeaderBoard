package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.facade.ScoreFacade;
import com.shaily.intuitLeaderboard.response.ScoreResponse;
import com.shaily.intuitLeaderboard.service.impl.ScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScoreServiceTest {

    @Mock
    private ScoreFacade scoreFacade;

    @InjectMocks
    private ScoreServiceImpl scoreService;

    private String playerId;
    private Score score;
    private ScoreResponse scoreResponse;

    @BeforeEach
    public void setup() {
        playerId = "player1";
        score = new Score(playerId, DEFAULT_CLIENT, 100.0);
        scoreResponse = ScoreResponse.builder().score(score).build();
    }

    @Test
    public void testGetScore_success() throws DatabaseStorageException {
        when(scoreFacade.getScore(playerId)).thenReturn(score);

        ScoreResponse response = scoreService.getScore(playerId);

        assertNotNull(response);
        assertEquals(score, response.getScore());
        assertEquals("Score fetched successfully for Player : " + playerId, response.getMessage());
    }

    @Test
    public void testGetScore_throwsDatabaseStorageException() throws DatabaseStorageException {
        when(scoreFacade.getScore(playerId)).thenThrow(new DatabaseStorageException("Database error"));

        ScoreResponse response = scoreService.getScore(playerId);

        assertNotNull(response);
        assertNull(response.getScore());
        assertEquals("Could not fetch score for Player : " + playerId + "Database error", response.getMessage());
    }

    @Test
    public void testUpdateScore_success() throws DatabaseStorageException {
        doNothing().when(scoreFacade).persistScore(score);
        doNothing().when(scoreFacade).publishToLeaderBoards(score);

        scoreService.updateScore(score);

        verify(scoreFacade, times(1)).persistScore(score);
        verify(scoreFacade, times(1)).publishToLeaderBoards(score);
    }

    @Test
    public void testUpdateScore_throwsDatabaseStorageException() throws DatabaseStorageException {
        doThrow(new DatabaseStorageException("Database error")).when(scoreFacade).persistScore(score);

        DatabaseStorageException exception = assertThrows(DatabaseStorageException.class, () -> {
            scoreService.updateScore(score);
        });

        assertEquals("Database error", exception.getMessage());
        verify(scoreFacade, times(1)).persistScore(score);
        verify(scoreFacade, never()).publishToLeaderBoards(any(Score.class));
    }

}
