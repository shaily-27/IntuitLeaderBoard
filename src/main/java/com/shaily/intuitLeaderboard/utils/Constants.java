package com.shaily.intuitLeaderboard.utils;

public class Constants {

    // Kafka Configs
    public static final String KAFKA_TOPIC = "score_topic";
    public static final String KAFKA_GROUP_ID = "scoreGroupId";
    public static final int DEFAULT_LEADERBOARD_SIZE = 5;
    public static final String KAFKA_ENDPOINT = "localhost:9092";

    // Redis Configs
    public static final String REDIS_HOST = "localhost";
    public static final Integer REDIS_PORT = 6379;
    public static final String DEFAULT_CLIENT = "intuitGame";

    // Response Body
    public static final String SUCCESSFUL_EVENT_PUSH = "Event pushed to kafka topic";
    public static final String PUSH_EVENT_FAILED = "Failed to push event - ";
    public static final String SUCCESSFUL_SCORE_UPDATE = "Score added successfully!";
    public static final String SCORE_UPDATE_FAILED = "Leaderboard Update failed - ";
}
