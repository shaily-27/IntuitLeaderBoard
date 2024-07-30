package com.shaily.intuitLeaderboard.repository;

import com.shaily.intuitLeaderboard.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, String> {
}
