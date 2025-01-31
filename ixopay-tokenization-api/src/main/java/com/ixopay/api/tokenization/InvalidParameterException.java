package com.ixopay.api.tokenization;

/**
 * This exception gets thrown if the sent {@link CardData} contained invalid information.
 * It indicates which field was erroneous.
 *
 * Fields without an error are set to null.
 */
public class InvalidParameterException extends Exception {

    /** Indicates an error with the supplied {@link CardData#pan}, or {@code null} if no error. */
    public final PanError panError;
    /** Indicates an error with the supplied {@link CardData#cvv}, or {@code null} if no error. */
    public final CvvError cvvError;
    /** Indicates an error with the supplied {@link CardData#expirationMonth}, or {@code null} if no error. */
    public final DateError monthError;
    /** Indicates an error with the supplied {@link CardData#expirationYear}, or {@code null} if no error. */
    public final DateError yearError;

    /**
     * Create a new parameter exception.
     *
     * @param panError   Error with the supplied {@link CardData#pan}, or {@code null} if no error.
     * @param cvvError   Error with the supplied {@link CardData#cvv}, or {@code null} if no error.
     * @param monthError Error with the supplied {@link CardData#expirationMonth}, or {@code null} if no error.
     * @param yearError  Error with the supplied {@link CardData#expirationYear}, or {@code null} if no error.
     */
    public InvalidParameterException(PanError panError, CvvError cvvError, DateError monthError, DateError yearError) {
        super("Invalid card parameter");
        this.panError = panError;
        this.cvvError = cvvError;
        this.monthError = monthError;
        this.yearError = yearError;
    }

    /** Reason why the supplied PAN is invalid. */
    public enum PanError {
      /** The supplied PAN was empty. */
      empty,
      /** The supplied PAN is of an invalid length */
      invalid_length,
      /** The <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn check</a> for the supplied PAN failed. */
      invalid_luhn
    }

    /** Reason why the supplied CVV is invalid. */
    public enum CvvError {
      /** The supplied CVV was empty. */
      empty,
      /** The supplied CVV had an invalid format. */
      invalid_format
    }

    /** Reason why the supplied expiration month/year is invalid. */
    public enum DateError {
      /** The supplied expiration month/year was empty. */
      empty,
      /** The supplied expiration month/year was outside allowed boundaries. */
      invalid_expiration_date,
      /** The supplied expiration month/year indicates an expired card. */
      expired,
      /** The supplied expiration month/year was invalid for other reasons, for example not a number etc. */
      invalid
    }

}
