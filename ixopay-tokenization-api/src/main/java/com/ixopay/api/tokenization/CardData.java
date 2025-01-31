package com.ixopay.api.tokenization;

import java.io.Serializable;

/**
 * This class holds credit card information to be passed to the tokenization api
 */
public class CardData implements Serializable {
    private static final long serialVersionUID = -1L;

    /** Primary Account Number (PAN). */
    public final String pan;
    /** Card Verification Value (CVV). */
    public final String cardHolder;
    /** Name of the cardholder. */
    public final String cvv;

    /** Month of the card expiration date. */
    public final int expirationMonth;
    /** Year of the card expiration date. */
    public final int expirationYear;

    /**
     * Create new card data.
     *
     * @param pan             Primary Account Number (PAN).
     * @param cvv             Card Verification Value (CVV).
     * @param cardHolder      Name of the cardholder.
     * @param expirationMonth Month of the card expiration date.
     * @param expirationYear  Year of the card expiration date.
     */
    public CardData(String pan, String cvv, String cardHolder, int expirationMonth, int expirationYear) {
        this.pan = pan;
        this.cvv = cvv;
        this.cardHolder = cardHolder;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }
}
