package com.app.ChatApplication.dto;

import java.util.UUID;

public record ChatMessageRequest(String content, UUID roomId, String senderEmail) {
}
