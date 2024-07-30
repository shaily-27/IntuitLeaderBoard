package com.shaily.intuitLeaderboard.service.impl;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.facade.ScoreFacade;
import com.shaily.intuitLeaderboard.response.ScoreResponse;
import com.shaily.intuitLeaderboard.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final ScoreFacade scoreFacade;

    @Autowired
    public ScoreServiceImpl(ScoreFacade scoreFacade) {
        this.scoreFacade = scoreFacade;
    }

    @Override
    public ScoreResponse getScore(final String playerId) {
        ScoreResponse scoreResponse = new ScoreResponse();
        try {
            Score score = scoreFacade.getScore(playerId);
            scoreResponse = ScoreResponse.builder()
                    .score(score)
                    .build();
            scoreResponse.setMessage("Score fetched successfully for Player : " + playerId);
        } catch (DatabaseStorageException e) {
            scoreResponse.setMessage("Could not fetch score for Player : " + playerId + e.getMessage());
        }
        return scoreResponse;
    }

    @Override
    public void updateScore(final Score score) throws DatabaseStorageException {
        scoreFacade.persistScore(score);
        scoreFacade.publishToLeaderBoards(score);
    }

}
