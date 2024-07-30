package com.shaily.intuitLeaderboard.service;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.response.ScoreResponse;
import org.springframework.stereotype.Component;

@Component
public interface ScoreService {

    ScoreResponse getScore(final String playerId);
    void updateScore(final Score score) throws DatabaseStorageException;
}
