package com.passwordwallet.security;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.hamcrest.Matchers.*;

class EncryptionServiceTest {

    @Test
    public void calculateHMAC_dataShouldBeEncrypted(){

        var password = "test";
        var key = "key";

        var encryptedPassword = EncryptionService.calculateHMAC(password, key);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void calculateSHA512_dataShouldBeEncrypted(){
        var password = "test";

        var encryptedPassword = EncryptionService.calculateSHA512(password);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void encryptAES_dataShouldBeEncrypted() throws Exception {
        var password = "test";
        Key key = EncryptionService.generateKey("testKey");

        var encryptedPassword = EncryptionService.encryptAES(password, key);

        MatcherAssert.assertThat(encryptedPassword, not(equalTo(password)));
    }

    @Test
    public void encryptAES_dataShouldBeDecrypted() throws Exception {
        var password = "test";
        Key key = EncryptionService.generateKey("testKey");
        var encryptedPassword = EncryptionService.encryptAES(password, key);

        var decryptedPassword = EncryptionService.decryptAES(encryptedPassword, key);

        MatcherAssert.assertThat(decryptedPassword, equalTo(password));
    }
}