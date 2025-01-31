package com.ixopay.api.tokenization;

import java.io.Serializable;

/**
 * This class holds the result of the tokenization request
 */
public class Token implements Serializable {
    private static final long serialVersionUID = -1L;

    /** The token that can be used for subsequent Transaction API calls. */
    public final String token;
    /** Fingerprint for the creditcard to detect if the same card has already been tokenized. */
    public final String fingerprint;

    /**
     * Create a new token.
     *
     * @param token       The token that can be used for subsequent Transaction API calls.
     * @param fingerprint Fingerprint for the creditcard to detect if the same card has already been tokenized.
     */
    public Token(String token, String fingerprint) {
        this.token = token;
        this.fingerprint = fingerprint;
    }
}
