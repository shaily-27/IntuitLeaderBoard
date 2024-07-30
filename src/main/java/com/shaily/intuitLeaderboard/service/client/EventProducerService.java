package com.shaily.intuitLeaderboard.service.client;

import com.shaily.intuitLeaderboard.exception.MessageQueueException;

public interface EventProducerService<T> {

    void addEventToQueue(T newEvent) throws MessageQueueException;
}
