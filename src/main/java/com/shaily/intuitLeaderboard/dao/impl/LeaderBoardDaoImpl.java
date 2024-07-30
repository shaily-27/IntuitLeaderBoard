package com.shaily.intuitLeaderboard.dao.impl;

import com.shaily.intuitLeaderboard.dao.LeaderBoardDao;
import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.entity.Score;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.List;

import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_CLIENT;
import static com.shaily.intuitLeaderboard.utils.Constants.DEFAULT_LEADERBOARD_SIZE;

@Repository
@Log4j2
public class LeaderBoardDaoImpl implements LeaderBoardDao {

    private final Jedis jedis;

    @Autowired
    public LeaderBoardDaoImpl(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void addScoreAndUpdateLeaderBoard(final Score score) {
        if (score == null || score.getPlayerId() == null) {
            throw new IllegalArgumentException("Score and Player ID must not be null");
        }
        try {
            log.info("Adding LeaderBoard entry for Player: {}", score.getPlayerId());
            jedis.zadd(DEFAULT_CLIENT, score.getScore(), score.getPlayerId());
        } catch (Exception e) {
            log.error("Error adding score to leaderboard for player: {}. Error: {}", score.getPlayerId(), e.getMessage(), e);
            throw new RuntimeException("Error adding score to leaderboard", e);
        }
    }

    @Override
    public List<LeaderBoardEntry> getLeaderBoard() throws RuntimeException{
        List<LeaderBoardEntry> leaderBoardEntryList = new ArrayList<>();
        List<Tuple> output;
        try {
            output = jedis.zrevrangeWithScores(DEFAULT_CLIENT, 0, DEFAULT_LEADERBOARD_SIZE);
        } catch (Exception e) {
            log.error("Error retrieving leaderboard. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving leaderboard", e);
        }

        if (CollectionUtils.isEmpty(output)) {
            return leaderBoardEntryList;
        }
        int rank = 1;
        for (Tuple entry : output) {
            try {
                String playerId = new String(entry.getBinaryElement());
                leaderBoardEntryList.add(
                        LeaderBoardEntry.builder()
                                .playerId(playerId)
                                .highestScore(entry.getScore())
                                .rank(rank++)
                                .build()
                );
            } catch (Exception e) {
                log.error("Error parsing leaderboard entry. Error: {}", e.getMessage(), e);
                throw new RuntimeException("Error parsing leaderboard entry", e);
            }
        }
        return leaderBoardEntryList;
    }
}
