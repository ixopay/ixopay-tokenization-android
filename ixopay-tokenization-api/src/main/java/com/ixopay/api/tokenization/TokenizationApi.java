package com.ixopay.api.tokenization;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class is the entry point to the IXOPAY tokenization API.
 */
public class TokenizationApi {

    private static final String GATEWAY_HOST = "https://gateway.ixopay.com";
    private static final String TOKENIZATION_HOST = "https://secure.ixopay.com";

    private final String integrationKey;
    private final String gatewayHost;
    private final String tokenizationHost;
    private final OkHttpClient httpClient;

    private TokenizationApi(OkHttpClient httpClient, String integrationKey, String gatewayHost, String tokenizationHost) {
        this.httpClient = httpClient;
        this.integrationKey = integrationKey;
        this.gatewayHost = gatewayHost;
        this.tokenizationHost = tokenizationHost;
    }

    /**
     * Creates a {@link Builder} for creating a new instance of {@link TokenizationApi}
     * @return Builder for creating a new {@link TokenizationApi} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String integrationKey;
        private String gatewayHost;
        private String tokenizationHost;
        private OkHttpClient httpClient;

        private Builder() {}

        /**
         * @param integrationKey required for authenticating the application against the API.
         * @return this
         */
        public Builder integrationKey(String integrationKey) {
            this.integrationKey = integrationKey;
            return this;
        }

        /**
         * @param gatewayHost this can be used to switch to a testing/staging environment of the IXOPAY gateway.
         * @return this
         */
        public Builder gatewayHost(String gatewayHost) {
            this.gatewayHost = gatewayHost;
            return this;
        }

        /**
         * @param tokenizationHost this can be used to switch to a testing/staging environment of the tokenization API.
         * @return this
         */
        public Builder tokenizationHost(String tokenizationHost) {
            this.tokenizationHost = tokenizationHost;
            return this;
        }

        /**
         * @param httpClient this can be used to customize the {@link OkHttpClient} used in the {@link TokenizationApi}.
         * @return this
         */
        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * @throws IllegalArgumentException in case of a missing {@code integrationKey}.
         * @return a new instance of {@link TokenizationApi}
         */
        public TokenizationApi build() {
            if (integrationKey == null)
                throw new IllegalArgumentException("integrationKey needs to be defined");

            if (httpClient == null)
                httpClient = new OkHttpClient();
            if (gatewayHost == null)
                gatewayHost = GATEWAY_HOST;
            if (tokenizationHost == null)
                tokenizationHost = TOKENIZATION_HOST;
            return new TokenizationApi(httpClient, integrationKey, gatewayHost, tokenizationHost);
        }
    }

    private String getTokenizationKey() throws IOException, TokenizationApiException {
        URL url = new URL(gatewayHost + "/integrated/getTokenizationKey/" + integrationKey);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_public_integration_key, "Invalid public integration key");
            if (response.body() == null)
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Empty response body");

            JSONObject parsedResponse = new JSONObject(response.body().string());
            if (!parsedResponse.has("tokenizationKey"))
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Missing tokenizationKey in response");

            return parsedResponse.getString("tokenizationKey");
        }
    }

    private void processErrors(JSONObject errorObject) throws TokenizationApiException, InvalidParameterException {
        if (errorObject == null)
            throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Error messages missing");

        InvalidParameterException.PanError panError = null;
        InvalidParameterException.CvvError cvvError = null;
        InvalidParameterException.DateError monthError = null;
        InvalidParameterException.DateError yearError = null;

        if (errorObject.has("pan"))
            panError = InvalidParameterException.PanError.valueOf(errorObject.getJSONArray("pan").getString(0));
        if (errorObject.has("cvv"))
            cvvError = InvalidParameterException.CvvError.valueOf(errorObject.getJSONArray("cvv").getString(0));
        if (errorObject.has("month"))
            monthError = InvalidParameterException.DateError.valueOf(errorObject.getJSONArray("month").getString(0));
        if (errorObject.has("year"))
            yearError = InvalidParameterException.DateError.valueOf(errorObject.getJSONArray("year").getString(0));

        throw new InvalidParameterException(panError, cvvError, monthError, yearError);
    }

    private RequestBody buildCardParams(CardData cardData) {
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("cardHolder", cardData.cardHolder)
                .add("month", String.valueOf(cardData.expirationMonth))
                .add("year", String.valueOf(cardData.expirationYear));

        if (cardData.cvv != null && !cardData.cvv.isEmpty()) {
            bodyBuilder
                    .add("pan", cardData.pan)
                    .add("cvv", cardData.cvv);
        } else {
            bodyBuilder.add("panonly", cardData.pan);
        }
        return bodyBuilder.build();
    }

    /**
     *
     * @param cardData for which the token should be received. It must not be null.
     * @return the tokenization result on success. In case of an error an exception is thrown. It never returns null.
     * @throws TokenizationApiException in case of an unexpected error or an invalid public integration key (see {@link Builder#integrationKey(String)}.
     * @throws InvalidParameterException in case of invalid {@link CardData}
     * @throws IOException forwarded from the {@link OkHttpClient}
     */
    public Token tokenizeCardData(CardData cardData) throws TokenizationApiException, InvalidParameterException, IOException {
        if (cardData == null)
            throw new IllegalArgumentException("cardData must not be null");

        String tokenizationKey = getTokenizationKey();
        URL url = new URL(tokenizationHost + "/v1/" + tokenizationKey + "/tokenize/creditcard");
        Request request = new Request.Builder()
                .url(url)
                .post(buildCardParams(cardData))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 401 || response.code() == 418)
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_token_key, "Invalid token key");
            else if (!response.isSuccessful())
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Unexpected response code: " + response.code());

            if (response.body() == null)
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Empty response body");

            JSONObject parsedResponse = new JSONObject(response.body().string());
            if (!parsedResponse.has("success"))
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Expected success in reponse");

            if (!parsedResponse.getBoolean("success"))
                processErrors(parsedResponse.getJSONObject("errors"));

            if (!parsedResponse.has("token") || !parsedResponse.has("fingerprint"))
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Expected token and fingerprint in response");

            String token = parsedResponse.getString("token");
            String fingerprint = parsedResponse.getString("fingerprint");

            if (token == null || fingerprint == null) {
                throw new TokenizationApiException(TokenizationApiException.Cause.invalid_response, "Expected token or fingerprint is null");
            }
            return new Token(token, fingerprint);
        }
    }
}
