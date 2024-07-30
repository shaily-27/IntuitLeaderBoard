package com.shaily.intuitLeaderboard.controller;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.response.ScoreResponse;
import com.shaily.intuitLeaderboard.service.ScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.shaily.intuitLeaderboard.utils.Constants.SCORE_UPDATE_FAILED;
import static com.shaily.intuitLeaderboard.utils.Constants.SUCCESSFUL_SCORE_UPDATE;

@RestController
@Log4j2
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @GetMapping("/getScore")
    public ScoreResponse getScore(@RequestBody final String playerId) {
        return scoreService.getScore(playerId);
    }

    @PutMapping("/updateScore")
    public ResponseEntity<String> updateScore(@RequestBody final Score score) {
        try {
            scoreService.updateScore(score);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFUL_SCORE_UPDATE);
        } catch (Exception e) {
            log.error(SCORE_UPDATE_FAILED + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
