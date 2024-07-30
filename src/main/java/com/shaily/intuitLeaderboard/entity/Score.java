package com.shaily.intuitLeaderboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="Score")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {

    @Id
    String playerId;
    String clientId;
    Double score;
}
