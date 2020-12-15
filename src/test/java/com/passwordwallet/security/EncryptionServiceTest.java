package com.passwordwallet.security;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.hamcrest.Matchers.*;

class EncryptionServiceTest {

    @Test
    public void shouldReturnTrueIfPasswordWasEncryptedInHMAC(){

        var password = "test";
        var key = "key";

        var encryptedPassword = EncryptionService.calculateHMAC(password, key);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void shouldReturnTrueIfPasswordWasEncryptedInSHA512(){
        var password = "test";

        var encryptedPassword = EncryptionService.calculateSHA512(password);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void shouldReturnTrueIfPasswordWasEncryptedInAES() {
        var password = "test";
        Key key = EncryptionService.generateKey("testKey");

        var encryptedPassword = EncryptionService.encryptAES(password, key);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void shouldReturnTrueIfPasswordWasDecryptedInAES() {
        var password = "test";
        Key key = EncryptionService.generateKey("testKey");
        var encryptedPassword = EncryptionService.encryptAES(password, key);

        var decryptedPassword = EncryptionService.decryptAES(encryptedPassword, "testKey");

        MatcherAssert.assertThat(decryptedPassword, equalTo(password));
    }
}