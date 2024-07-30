package com.shaily.intuitLeaderboard.service;

public interface EventConsumerService<T> {

    void consumeEventFromQueue(T newEvent);
}
