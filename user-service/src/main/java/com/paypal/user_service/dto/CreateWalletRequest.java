package com.paypal.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateWalletRequest {
    private Long userId;
    private String currency;

}