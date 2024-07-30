package com.shaily.intuitLeaderboard.dao;

import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;

import java.util.List;

public interface LeaderBoardDao {

    void addScoreAndUpdateLeaderBoard(final Score score);
    List<LeaderBoardEntry> getLeaderBoard() throws RuntimeException;
}
