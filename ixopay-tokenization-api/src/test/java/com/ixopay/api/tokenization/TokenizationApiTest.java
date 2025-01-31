package com.ixopay.api.tokenization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import com.ixopay.api.tokenization.InvalidParameterException.DateError;

class TokenizationApiTest {

    private TokenizationApi api;

    void setup() {
        api = TokenizationApi.builder()
                .gatewayHost(System.getProperty("com.ixopay.gateway_host"))
                .tokenizationHost(System.getProperty("com.ixopay.tokenization_host"))
                .integrationKey(System.getProperty("com.ixopay.integration_key"))
                .build();
    }

    @Test
    void success() throws IOException, TokenizationApiException, InvalidParameterException {
        setup();
        Token result = api.tokenizeCardData(new CardData("4111 1111 1111 1111", null, "Some one", 9, 2100));
        assertNotNull(result.fingerprint);
        assertNotNull(result.token);
    }


    @Test
    void errorYearMonthExpired() {
        setup();
        InvalidParameterException error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("4111 1111 1111 1111", null, "Some one", 9, 2018)));
        assertEquals(DateError.expired, error.monthError);
        assertEquals(DateError.expired, error.yearError);
        assertNull(error.panError);
        assertNull(error.cvvError);
    }

    @Test
    void errorYearMonthInvalid() {
        setup();
        InvalidParameterException error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("4111 1111 1111 1111", null, "Some one", -1, -1)));
        assertEquals(DateError.invalid, error.monthError);
        assertEquals(DateError.invalid, error.yearError);
        assertNull(error.panError);
        assertNull(error.cvvError);
    }

    @Test
    void errorPanInvalid() {
        setup();
        InvalidParameterException error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("4121 1111 1111 1111", null, "Some one", 1, 2100)));
        assertEquals(InvalidParameterException.PanError.invalid_luhn, error.panError);
        assertNull(error.cvvError);
        assertNull(error.yearError);
        assertNull(error.monthError);

        error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("4121 1111 1111", null, "Some one", 1, 2100)));
        assertEquals(InvalidParameterException.PanError.invalid_length, error.panError);
        assertNull(error.cvvError);
        assertNull(error.yearError);
        assertNull(error.monthError);

        error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("", null, "Some one", 1, 2100)));
        assertEquals(InvalidParameterException.PanError.empty, error.panError);
        assertNull(error.cvvError);
        assertNull(error.yearError);
        assertNull(error.monthError);
    }

    @Test
    void errorCvvInvalid() {
        setup();
        InvalidParameterException error = assertThrows(InvalidParameterException.class, () -> api.tokenizeCardData(new CardData("4111 1111 1111 1111", "1231223", "Some one", 1, 2100)));
        assertEquals(InvalidParameterException.CvvError.invalid_format, error.cvvError);
        assertNull(error.panError);
        assertNull(error.yearError);
        assertNull(error.monthError);
    }

    @Test
    void errorPublicIntegrationKey() {
        api = TokenizationApi.builder()
                .integrationKey("aaaaaBBBBaaaaaBBBBaB")
                .build();

        TokenizationApiException error = assertThrows(TokenizationApiException.class, () -> api.tokenizeCardData(new CardData("4111 1111 1111 1111", null, "Some one", 9, 2100)));
        assertEquals(TokenizationApiException.Cause.invalid_public_integration_key, error.type);
    }

    @Test
    void illegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> api = TokenizationApi.builder().build());
    }

    @Test
    void customHttpClient() throws IOException, TokenizationApiException, InvalidParameterException {
        AtomicBoolean requestIntercepted = new AtomicBoolean(false);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    requestIntercepted.set(true);
                    return chain.proceed(chain.request());
                })
                .build();
        api = TokenizationApi.builder()
                .gatewayHost(System.getProperty("com.ixopay.gateway_host"))
                .tokenizationHost(System.getProperty("com.ixopay.tokenization_host"))
                .integrationKey(System.getProperty("com.ixopay.integration_key"))
                .httpClient(httpClient)
                .build();

        Token result = api.tokenizeCardData(new CardData("4111 1111 1111 1111", null, "Some one", 9, 2100));
        assertNotNull(result.fingerprint);
        assertNotNull(result.token);
        assertTrue(requestIntercepted.get());

        assertThrows(IllegalArgumentException.class, () -> api.tokenizeCardData(null));
    }
}