package com.paypal.user_service.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token,
        Long id,
        String name,
        String email
) {
}
