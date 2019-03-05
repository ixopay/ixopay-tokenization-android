package com.ixopay.api.tokenization;

/**
 * This class holds credit card information to be passed to the tokenization api
 */
public class CardData {
    public final String pan;
    public final String cardHolder;
    public final String cvv;

    public final int expirationMonth;
    public final int expirationYear;

    /**
     * @param pan primary account number
     * @param cvv card verification value
     * @param cardHolder name of the card owner
     * @param expirationMonth month of the card expiration date
     * @param expirationYear year of the card expiration date
     */
    public CardData(String pan, String cvv, String cardHolder, int expirationMonth, int expirationYear) {
        this.pan = pan;
        this.cvv = cvv;
        this.cardHolder = cardHolder;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }
}
