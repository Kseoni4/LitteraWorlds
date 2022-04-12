package org.litteraworlds.net;

import org.litteraworlds.view.Debug;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class SecurityConfig {

    byte[] SALT = "Соль1".getBytes(StandardCharsets.UTF_8);

    char[] SECRET_PHRASE = "СложныйПароль".toCharArray();

    int IV_LENGHT = 12; //bytes
    int TAG_SIZE = 128; //bits (128/8 = 16 bytes)

    SecretKey generateKey(char[] secretPhrase) throws Exception {
        var secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        var spec = new PBEKeySpec(secretPhrase, SALT, 1, 128);

        var temp = secretKeyFactory.generateSecret(spec);

        return new SecretKeySpec(temp.getEncoded(),"AES");
    }

    byte[] encrypt(SecretKey secretKey, byte[] openText) throws Exception {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cipherText = cipher.doFinal(openText);

        var gcmParams = cipher.getParameters().getParameterSpec(GCMParameterSpec.class);

        byte[] iv = gcmParams.getIV(); //Вектор инициализации

        var result = new byte[iv.length+cipherText.length];

        System.arraycopy(iv, 0, result, 0, iv.length);

        System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);

        return result;
    }

    byte[] decrypt(SecretKey secretKey, byte[] message) throws Exception {
        var iv = Arrays.copyOfRange(message, 0, IV_LENGHT);

        var cipherText = Arrays.copyOfRange(message, IV_LENGHT, message.length);

        var gcmParams = new GCMParameterSpec(TAG_SIZE, iv);

        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParams);

        return cipher.doFinal(cipherText);
    }

    String digest(String... data) throws NoSuchAlgorithmException {
        var scrString = Arrays.stream(data)
                .map(s -> s == null ? "" : s)
                .map(String::trim)
                .map(s -> Normalizer.normalize(s, Normalizer.Form.NFC))
                .collect(Collectors.joining("|"));

        System.out.println(scrString);

        var srcBytes = scrString.getBytes(StandardCharsets.UTF_8);

        var md = MessageDigest.getInstance("SHA-256");

        var result = md.digest(srcBytes);

        return Base64.getEncoder().encodeToString(result);
    }

    public void testConfig(){
        try {

            var key = generateKey(SECRET_PHRASE);
            Debug.toLog(new String(key.getEncoded()));
            var encrypted = encrypt(key, "hello".getBytes(StandardCharsets.UTF_8));
            Debug.toLog(new String(encrypted));
            Debug.toLog(new String(decrypt(key, encrypted)));

            Debug.toLog(digest("building1323", "as"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
