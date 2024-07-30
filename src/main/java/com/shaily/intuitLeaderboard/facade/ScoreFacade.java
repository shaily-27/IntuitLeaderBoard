package com.shaily.intuitLeaderboard.facade;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.DatabaseStorageException;
import com.shaily.intuitLeaderboard.repository.ScoreRepository;
import com.shaily.intuitLeaderboard.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ScoreFacade {

    private final ScoreRepository scoreRepository;
    private final LeaderBoardService leaderBoardService;

    @Autowired
    public ScoreFacade(ScoreRepository scoreRepository, LeaderBoardService leaderBoardService) {
        this.scoreRepository = scoreRepository;
        this.leaderBoardService = leaderBoardService;
    }

    public Score getScore(final String playerId) throws DatabaseStorageException {
        try {
            return scoreRepository.findById(playerId)
                    .orElseThrow(() -> new DatabaseStorageException("Score not found for Player : " + playerId));
        } catch (Exception e) {
            throw new DatabaseStorageException("Could not fetch score for Player : " + playerId + " " + e);
        }
    }

    public void persistScore(final Score score) throws DatabaseStorageException {
        if (score == null || score.getPlayerId() == null) {
            throw new IllegalArgumentException("Score or Player ID cannot be null");
        }
        try {
            Optional<Score> existingScore = scoreRepository.findById(score.getPlayerId());
            if(existingScore.isPresent()) {
                double updatedScore = existingScore.get().getScore() + score.getScore();
                score.setScore(updatedScore);
            }
            scoreRepository.save(score);
        } catch (Exception e) {
            throw new DatabaseStorageException("Could not save score for Player : " + score.getPlayerId());
        }
    }

    public void publishToLeaderBoards(final Score score) {
        if (score == null) {
            throw new IllegalArgumentException("Score cannot be null");
        }
        leaderBoardService.publish(score);
    }
}
