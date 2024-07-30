package com.shaily.intuitLeaderboard.exception;

public class MessageQueueException extends Exception {
    public MessageQueueException(String message, Exception e) {
        super(message);
    }
}