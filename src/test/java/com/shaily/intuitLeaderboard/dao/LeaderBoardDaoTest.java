package com.shaily.intuitLeaderboard.dao;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.dao.impl.LeaderBoardDaoImpl;
import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LeaderBoardDaoTest {

    @Mock
    private Jedis jedis;

    @InjectMocks
    private LeaderBoardDaoImpl leaderBoardDao;

    @Test
    void testAddScoreAndUpdateLeaderBoard_success() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        when(jedis.zadd(eq(DEFAULT_CLIENT), eq(100.0), eq("player1"))).thenReturn(1L);

        assertDoesNotThrow(() -> leaderBoardDao.addScoreAndUpdateLeaderBoard(score));
        verify(jedis, times(1)).zadd(eq(DEFAULT_CLIENT), eq(100.0), eq("player1"));
    }

    @Test
    void testAddScoreAndUpdateLeaderBoard_scoreNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            leaderBoardDao.addScoreAndUpdateLeaderBoard(null);
        });
        assertEquals("Score and Player ID must not be null", thrown.getMessage());
    }

    @Test
    void testAddScoreAndUpdateLeaderBoard_playerIdNull() {
        Score score = new Score(null, DEFAULT_CLIENT, 100.0);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            leaderBoardDao.addScoreAndUpdateLeaderBoard(score);
        });
        assertEquals("Score and Player ID must not be null", thrown.getMessage());
    }

    @Test
    void testAddScoreAndUpdateLeaderBoard_redisException() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        doThrow(new RuntimeException("Redis error")).when(jedis).zadd(anyString(), anyDouble(), anyString());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            leaderBoardDao.addScoreAndUpdateLeaderBoard(score);
        });
        assertEquals("Error adding score to leaderboard", thrown.getMessage());
    }

    @Test
    void testGetLeaderBoard_success() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);
        Tuple tuple = mock(Tuple.class);
        when(tuple.getBinaryElement()).thenReturn("player1".getBytes());
        when(tuple.getScore()).thenReturn(100.0);
        List<Tuple> tuples = List.of(tuple);
        when(jedis.zrevrangeWithScores(anyString(), anyLong(), anyLong())).thenReturn(tuples);

        List<LeaderBoardEntry> result = leaderBoardDao.getLeaderBoard();

        assertNotNull(result);
        assertEquals(1, result.size());
        LeaderBoardEntry entry = result.get(0);
        assertEquals("player1", entry.getPlayerId());
        assertEquals(100.0, entry.getHighestScore());
        assertEquals(1, entry.getRank());
    }

    @Test
    void testGetLeaderBoard_emptyList() {
        when(jedis.zrevrangeWithScores(anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        List<LeaderBoardEntry> result = leaderBoardDao.getLeaderBoard();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLeaderBoard_redisException() {
        when(jedis.zrevrangeWithScores(anyString(), anyLong(), anyLong())).thenThrow(new RuntimeException("Redis error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            leaderBoardDao.getLeaderBoard();
        });
        assertEquals("Error retrieving leaderboard", thrown.getMessage());
    }

    @Test
    void testGetLeaderBoard_parsingException() {
        Tuple mockTuple = mock(Tuple.class);
        List<Tuple> mockOutput = Collections.singletonList(mockTuple);

        when(jedis.zrevrangeWithScores(anyString(), anyLong(), anyLong())).thenReturn(mockOutput);
        doThrow(new RuntimeException("Parsing error")).when(mockTuple).getBinaryElement();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            leaderBoardDao.getLeaderBoard();
        });
        assertEquals("Error parsing leaderboard entry", thrown.getMessage());
    }
}
