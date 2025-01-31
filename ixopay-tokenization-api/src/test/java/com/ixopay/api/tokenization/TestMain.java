package com.ixopay.api.tokenization;

import java.io.IOException;

class TestMain {

    public static void main(String[] args) throws IOException {
        try {
            TokenizationApi api = com.ixopay.api.tokenization.TokenizationApi.builder()
                    .integrationKey(System.getenv("IXOPAY_INTEGRATION_KEY"))
                    .build();

            Token result = api.tokenizeCardData(new CardData("4111 1111 1111 1111", "111", "Some cardholder", 10, 2022));

            System.out.println("Token: " + result.token);
            System.out.println("Fingerprint: " + result.fingerprint);

        } catch (InvalidParameterException e) {
            if (e.cvvError != null)
                System.err.println("CVV error: " + e.cvvError);
            if (e.monthError != null)
                System.err.println("Month error: " + e.monthError);
            if (e.yearError != null)
                System.err.println("Year error: " + e.yearError);
            if (e.panError != null)
                System.err.println("PAN error: " + e.panError);
        } catch (TokenizationApiException e) {
            System.err.println(e.type + ": " + e.message);
        }
    }
}
