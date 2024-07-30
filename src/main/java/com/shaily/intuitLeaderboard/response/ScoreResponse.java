package com.shaily.intuitLeaderboard.response;

import com.shaily.intuitLeaderboard.entity.Score;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponse extends BaseResponse{

    private Score score;
}
