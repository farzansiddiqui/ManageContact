package com.englishspeaking.app.dto.response;

public record ApiResponse(boolean success, String message, UserResponse user) {
}
