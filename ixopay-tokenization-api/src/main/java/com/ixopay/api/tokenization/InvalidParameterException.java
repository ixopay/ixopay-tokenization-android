package com.ixopay.api.tokenization;

/**
 * This exception gets thrown if the sent {@link CardData} contained invalid information.
 * It indicates which field was erroneous.
 *
 * Fields without an error are set to null.
 */
public class InvalidParameterException extends Exception {

    public final PanError panError;
    public final CvvError cvvError;
    public final DateError monthError;
    public final DateError yearError;

    public InvalidParameterException(PanError panError, CvvError cvvError, DateError monthError, DateError yearError) {
        super("Invalid card parameter");
        this.panError = panError;
        this.cvvError = cvvError;
        this.monthError = monthError;
        this.yearError = yearError;
    }

    public enum PanError { empty, invalid_length, invalid_luhn}
    public enum CvvError { empty, invalid_format}
    public enum DateError { empty, invalid_expiration_date, expired}

}
