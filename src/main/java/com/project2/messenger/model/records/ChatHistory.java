package com.project2.messenger.model.records;

import com.project2.messenger.model.Message;

import java.util.List;

public record ChatHistory(List<Message> messages, Long cursorId, Boolean hasMore) {

}
