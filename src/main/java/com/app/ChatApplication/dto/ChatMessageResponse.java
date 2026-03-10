package com.app.ChatApplication.dto;

import java.util.Date;
import java.util.UUID;

public record ChatMessageResponse(UUID id, String content, String senderUsername, Date timestamp) {
}
