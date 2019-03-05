package com.ixopay.api.tokenization;

/**
 * This class holds the result of the tokenization request
 */
public class Token {

    public final String token;
    public final String fingerprint;

    public Token(String token, String fingerprint) {
        this.token = token;
        this.fingerprint = fingerprint;
    }
}
