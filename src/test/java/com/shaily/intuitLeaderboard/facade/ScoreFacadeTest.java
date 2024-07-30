package com.shaily.intuitLeaderboard.facade;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.repository.ScoreRepository;
import com.shaily.intuitLeaderboard.service.LeaderBoardService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScoreFacadeTest {

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private LeaderBoardService leaderBoardService;

    @InjectMocks
    private ScoreFacade scoreFacade;

    @Test
    void testGetScore_success() throws DatabaseStorageException {
        String playerId = "player1";
        Score expectedScore = new Score(playerId, DEFAULT_CLIENT, 100.0);

        when(scoreRepository.findById(playerId)).thenReturn(Optional.of(expectedScore));

        Score result = scoreFacade.getScore(playerId);
        assertNotNull(result);
        assertEquals(expectedScore, result);
    }

    @Test
    void testGetScore_scoreNotFound() {
        String playerId = "player1";

        when(scoreRepository.findById(playerId)).thenReturn(Optional.empty());

        DatabaseStorageException thrown = assertThrows(DatabaseStorageException.class, () -> {
            scoreFacade.getScore(playerId);
        });
        assertTrue(thrown.getMessage().contains("Score not found for Player : " + playerId));
    }

    @Test
    void testGetScore_throwsDatabaseStorageException() {
        String playerId = "player1";

        when(scoreRepository.findById(playerId)).thenThrow(new RuntimeException("Database error"));

        DatabaseStorageException thrown = assertThrows(DatabaseStorageException.class, () -> {
            scoreFacade.getScore(playerId);
        });
        assertTrue(thrown.getMessage().contains("Could not fetch score for Player : " + playerId));
    }

    @Test
    void testPersistScore_success_newScore() throws DatabaseStorageException {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);

        when(scoreRepository.findById(score.getPlayerId())).thenReturn(Optional.empty());

        scoreFacade.persistScore(score);

        verify(scoreRepository).save(score);
    }

    @Test
    void testPersistScore_success_updateScore() throws DatabaseStorageException {
        Score existingScore = new Score("player1", DEFAULT_CLIENT, 50.0);
        Score newScore = new Score("player1", DEFAULT_CLIENT, 100.0);

        when(scoreRepository.findById(existingScore.getPlayerId())).thenReturn(Optional.of(existingScore));

        scoreFacade.persistScore(newScore);

        verify(scoreRepository).save(argThat(score -> score.getScore() == 150.0));
    }

    @Test
    void testPersistScore_scoreNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            scoreFacade.persistScore(null);
        });
        assertEquals("Score or Player ID cannot be null", thrown.getMessage());
    }

    @Test
    void testPersistScore_playerIdNull() {
        Score score = new Score(null, DEFAULT_CLIENT, 100.0);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            scoreFacade.persistScore(score);
        });
        assertEquals("Score or Player ID cannot be null", thrown.getMessage());
    }

    @Test
    void testPersistScore_throwsDatabaseStorageException() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);

        when(scoreRepository.findById(score.getPlayerId())).thenReturn(Optional.of(score));
        doThrow(new RuntimeException("Database error")).when(scoreRepository).save(any(Score.class));

        DatabaseStorageException thrown = assertThrows(DatabaseStorageException.class, () -> {
            scoreFacade.persistScore(score);
        });
        assertTrue(thrown.getMessage().contains("Could not save score for Player : " + score.getPlayerId()));
    }

    @Test
    void testPublishToLeaderBoards_success() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);

        scoreFacade.publishToLeaderBoards(score);

        verify(leaderBoardService).publish(score);
    }

    @Test
    void testPublishToLeaderBoards_nullScore() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            scoreFacade.publishToLeaderBoards(null);
        });
        assertEquals("Score cannot be null", thrown.getMessage());
    }
}
