package com.passwordwallet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;

@Component
public class EncryptionService {

    private static String hmacKey;

    private static String pepper;

    @Value("${properties.hmac-key}")
    public void setHmacKey(String hmacKey) {
        EncryptionService.hmacKey = hmacKey;
    }

    @Value("${properties.pepper}")
    public void setPepper(String pepper) {
        EncryptionService.pepper = pepper;
    }

    public static String encryptPassword(String password, String salt, String usedAlgorithm){

        String encryption;

        if(usedAlgorithm.equals("SHA-512")){
            encryption = calculateSHA512(pepper + salt + password);
        } else {
            encryption = calculateHMAC(password, hmacKey);
        }

        return encryption;
    }

    private static String calculateSHA512(String text) {

        //String SHA512Hash= calculateSHA512(pepper+salt+text);

        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //calculate message digest of the input string - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);

        return salt;
    }

    private static String calculateHMAC(String text, String key){
        Mac sha512Hmac;
        String result="";
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(text.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

}
