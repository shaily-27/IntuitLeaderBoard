package com.shaily.intuitLeaderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardEntry {
    private Integer rank;
    private String playerId;
    private Double highestScore;
}
