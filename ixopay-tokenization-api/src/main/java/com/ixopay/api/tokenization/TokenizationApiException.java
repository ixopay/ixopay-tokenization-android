package com.ixopay.api.tokenization;

public class TokenizationApiException extends Exception {

    public final String message;
    public final Cause type;

    public TokenizationApiException(Cause type, String message) {
        super(type + " " + message);
        this.message = message;
        this.type = type;
    }

    public enum Cause {invalid_public_integration_key, invalid_token_key, invalid_response}
}
