package com.shaily.intuitLeaderboard.controller;

import com.shaily.intuitLeaderboard.entity.Score;
import com.shaily.intuitLeaderboard.exception.MessageQueueException;
import com.shaily.intuitLeaderboard.service.client.EventProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.shaily.intuitLeaderboard.utils.Constants.PUSH_EVENT_FAILED;
import static com.shaily.intuitLeaderboard.utils.Constants.SUCCESSFUL_EVENT_PUSH;

@RestController
@Log4j2
public class EventProducerController {

    private final EventProducerService eventProducerService;

    @Autowired
    public EventProducerController(EventProducerService eventProducerService) {
        this.eventProducerService = eventProducerService;
    }

    @PostMapping("/pushEvent")
    public ResponseEntity<String> pushEvent(@RequestBody final Score newEvent) {
        try {
            eventProducerService.addEventToQueue(newEvent);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESSFUL_EVENT_PUSH);
        } catch (MessageQueueException e) {
            log.error(PUSH_EVENT_FAILED + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
