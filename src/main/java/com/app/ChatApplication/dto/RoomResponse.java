package com.app.ChatApplication.dto;

import java.util.UUID;

public record RoomResponse(UUID id, String name, String description) {
}
