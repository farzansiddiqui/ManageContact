package com.englishspeaking.app.dto.response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        String message,
        Instant timestamp,
        Map<String, String> errors
) {
}
