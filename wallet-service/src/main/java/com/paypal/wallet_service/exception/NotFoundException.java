package com.paypal.wallet_service.exception;


//If the user which we are trying to send money doesnot exists
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
