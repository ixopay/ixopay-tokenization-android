package com.ixopay.api.tokenization;

/** An error occurred during tokenization. */
public class TokenizationApiException extends Exception {

    /** Error message. */
    public final String message;
    /** Cause of the API exception. */
    public final Cause type;

    /**
     * Create a new tokenization API exception.
     *
     * @param type Cause of the API exception.
     * @param message Error message.
     */
    public TokenizationApiException(Cause type, String message) {
        super(type + " " + message);
        this.message = message;
        this.type = type;
    }

    /** The cause of the API exception. */
    public enum Cause {
      /** The supplied public integration key was invalid. */
      invalid_public_integration_key,
      /** The tokenize key received by the API was invalid. */
      invalid_token_key,
      /** Unexpected response recevied from the API. */
      invalid_response
    }
}
