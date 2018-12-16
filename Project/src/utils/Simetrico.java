package utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Simetrico {

    private final static String tipoAlgoritmo = "AES";
    private final static int tamanhoAlgoritmo = 256;
    public final static String SYM_KEY = "92AE31A79FEEB2A3";

    // Objects required for encryption/decryption
    private SecretKey secretKey ;
    private Base64.Encoder encoder ;
    private Base64.Decoder decoder;


    public Simetrico() {
        try {
            this.secretKey = new SecretKeySpec(SYM_KEY.getBytes(), "AES");
            this.encoder = Base64.getUrlEncoder();
            this.decoder = Base64.getUrlDecoder();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String encrypt(String plainText) {
        try {
            byte[] plainTextByte = plainText.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            return encoder.encodeToString(encryptedByte);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            byte[] encryptedByte = decoder.decode(encrypted);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedByte);
            return new String (decryptedByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
    private String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array).toLowerCase();
    }
}

