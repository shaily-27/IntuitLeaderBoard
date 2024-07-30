package com.shaily.intuitLeaderboard.service.impl;

import com.shaily.intuitLeaderboard.dao.LeaderBoardDao;
import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private final LeaderBoardDao leaderBoardDao;

    @Autowired
    public LeaderBoardServiceImpl(LeaderBoardDao leaderBoardDao) {
        this.leaderBoardDao = leaderBoardDao;
    }

    @Override
    public List<LeaderBoardEntry> getLeaderBoard() throws RuntimeException{
        return leaderBoardDao.getLeaderBoard();
    }

    @Override
    public void publish(final Score newScore) {
        leaderBoardDao.addScoreAndUpdateLeaderBoard(newScore);
    }
}
