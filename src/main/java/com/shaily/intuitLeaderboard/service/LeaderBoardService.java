package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LeaderBoardService {

    List<LeaderBoardEntry> getLeaderBoard() throws RuntimeException;
    void publish(final Score score);
}
