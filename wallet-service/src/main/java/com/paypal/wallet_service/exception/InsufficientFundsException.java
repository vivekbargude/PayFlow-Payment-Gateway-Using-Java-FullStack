package com.paypal.wallet_service.exception;

//If available balance is 10 and we are trying to send 20 then throw this exception
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
