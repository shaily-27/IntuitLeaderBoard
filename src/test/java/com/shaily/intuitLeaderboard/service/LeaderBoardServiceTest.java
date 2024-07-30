package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.IntuitLeaderBoardApplication;
import com.shaily.intuitLeaderboard.dao.LeaderBoardDao;
import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.service.impl.LeaderBoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntuitLeaderBoardApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LeaderBoardServiceTest {

    @Mock
    private LeaderBoardDao leaderBoardDao;

    @InjectMocks
    private LeaderBoardServiceImpl leaderBoardService;

    @Test
    public void testGetLeaderBoard() {
        LeaderBoardEntry entry1 = new LeaderBoardEntry(1, "player1", 100.0);
        LeaderBoardEntry entry2 = new LeaderBoardEntry(2, "player2", 200.0);
        List<LeaderBoardEntry> mockLeaderBoard = Arrays.asList(entry1, entry2);

        when(leaderBoardDao.getLeaderBoard()).thenReturn(mockLeaderBoard);

        List<LeaderBoardEntry> result = leaderBoardService.getLeaderBoard();
        assertEquals(mockLeaderBoard, result);
    }

    @Test
    public void testGetEmptyLeaderBoard() {
        List<LeaderBoardEntry> emptyLeaderBoard = new ArrayList<>();

        when(leaderBoardDao.getLeaderBoard()).thenReturn(emptyLeaderBoard);

        List<LeaderBoardEntry> result = leaderBoardService.getLeaderBoard();
        assertEquals(emptyLeaderBoard, result);
    }

    @Test
    public void testPublish() {
        Score score = new Score("player1", DEFAULT_CLIENT, 100.0);

        leaderBoardService.publish(score);

        verify(leaderBoardDao).addScoreAndUpdateLeaderBoard(score);
    }
}
