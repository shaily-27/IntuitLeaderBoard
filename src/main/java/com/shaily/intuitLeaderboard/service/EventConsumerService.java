package com.shaily.intuitLeaderboard.service;

import org.springframework.stereotype.Component;

@Component
public interface EventConsumerService<T> {

    void consumeEventFromQueue(T newEvent);
}
