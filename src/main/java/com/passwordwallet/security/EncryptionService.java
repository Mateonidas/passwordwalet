package com.passwordwallet.security;

import com.passwordwallet.entity.PasswordEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class EncryptionService {

    private static final String ALGO = "AES";
    private static String hmacKey;
    private static String pepper;
    private static String plainPassword;

    @Value("${properties.hmac-key}")
    public void setHmacKey(String hmacKey) {
        EncryptionService.hmacKey = hmacKey;
    }

    @Value("${properties.pepper}")
    public void setPepper(String pepper) {
        EncryptionService.pepper = pepper;
    }

    public static void setPlainPassword(String plainPassword) {
        EncryptionService.plainPassword = plainPassword;
    }

    public static String encryptMasterPassword(String password, String salt, String usedAlgorithm){

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

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);

        return salt.toString();
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
        }
        return result;
    }

    // Generate a new encryption key.
    public static Key generateKey(String password) throws Exception {
        return new SecretKeySpec(calculateMD5(password), ALGO);
    }

    private static byte[] calculateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //encrypts string and returns encrypted string
    public static String encrypt(String data, Key receivedKey) throws Exception {

        Key key;

        if(receivedKey != null){
            key = receivedKey;
        } else {
            key = generateKey(plainPassword);
        }

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    //decrypts string and returns plain text
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey(plainPassword);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    public static List<PasswordEntity> hidePasswords(List<PasswordEntity> passwords, HttpSession session){
        if (session.getAttribute("passwordToShow") != null) {
            int id = (int) session.getAttribute("passwordToShow");

            passwords.forEach(password -> {
                if (password.getId() == id) {
                    try {
                        password.setPassword(EncryptionService.decrypt(password.getPassword()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    password.setPassword("**********");
                }
            });
        } else {
            passwords.forEach(password -> {
                password.setPassword("**********");
            });
        }

        return passwords;
    }

    public static List<PasswordEntity> changePasswordsEncryption(List<PasswordEntity> passwords, String newPassword){

        passwords.forEach(password -> {
            try {
                password.setPassword(
                        EncryptionService.encrypt(
                                EncryptionService.decrypt(password.getPassword()),
                                EncryptionService.generateKey(newPassword))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EncryptionService.setPlainPassword(newPassword);
        return passwords;
    }
}
