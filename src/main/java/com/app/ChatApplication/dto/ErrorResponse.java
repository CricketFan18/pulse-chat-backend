package com.app.ChatApplication.dto;

public record ErrorResponse(int status, String message, long timestamp) {
}
