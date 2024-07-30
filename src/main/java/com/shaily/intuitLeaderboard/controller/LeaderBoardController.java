package com.shaily.intuitLeaderboard.controller;

import com.shaily.intuitLeaderboard.domain.LeaderBoardEntry;
import com.shaily.intuitLeaderboard.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class LeaderBoardController {

    @Autowired
    LeaderBoardService leaderBoardService;

    @GetMapping("/getLeaderBoard")
    public ResponseEntity<List<LeaderBoardEntry>> getLeaderBoard() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(leaderBoardService.getLeaderBoard());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
