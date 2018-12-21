package utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {
    private static final String PUBLIC_KEY =  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCfeP59DDplf+EzYWYeXjLscRqXQQqnpyxG2220CR54WbK5yVVOBRH0tqxJyRWUfx1hOaLwowPm/BTnWOF9PQq7nL804a9ENxrrybm2j4q7PUT7QGUKmwmGVtmIFt1ioKN6QYglgIq00NF8h/spdTvW76taDShsNUw8XR/0dgj5hUc/1SxvlXhFedMmBg9+b+iZpDH0ZKflxXbNm94TOjjw168rgVO72fPFkDvklFp4cL8ijSK6rvMEcltDZ+4F8GBFyEUbXXwQ4fNN0S2aI0dSqrJTJTWaSR4vw5zGS20XvVuFbapDfJYte28mkGxDsHDwHaKFPaFy0pPcN6LN6+QIDAQAB";

    public static byte[] encriptarTudo(String plainText)
    {
        String resposta = "";
        //Create Keys
        String symKey = CryptoUtils.createSymKey();

        System.out.println("SymKey: "+symKey);
        System.out.println("TClaro: "+ plainText);

        //Encoding
        byte[] encodedKey = Base64.getDecoder().decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        //Encrypt sym AES
        byte [] symText = CryptoUtils.encryptAES(symKeyDec , plainText);
        System.out.println("TCifrado: "+symText);

        resposta += Base64.getEncoder().encodeToString(symText);

        resposta += "\n";

        //Encrypt Assym RSA
        byte[] encryptedKey = CryptoUtils.encryptRSA(symKeyDec, CryptoUtils.loadPublicKey(PUBLIC_KEY));
        System.out.println("encryptedKey: "+encryptedKey);

        resposta += Base64.getEncoder().encodeToString(encryptedKey);
        return Base64.getEncoder().encode(resposta.getBytes(StandardCharsets.UTF_8));
    }


}

class CryptoUtils {

    public static String createSymKey() {
        try {
            //Generate Symmetric Key (AES with 128 bits)
            KeyGenerator generator = null;
            generator = KeyGenerator.getInstance("AES");
            generator.init(128); // The AES key size in number of bits
            SecretKey secKey = generator.generateKey();
            //Convert to string - Base64 encoding
            String stringKey = Base64.getEncoder().encodeToString(secKey.getEncoded());

            return stringKey;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey loadPublicKey(String stored) {
        try {
            byte[] data = Base64.getDecoder().decode(stored);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = kf.generatePublic(spec);
            System.out.println("PublicKey Original: "+pubKey);
            return pubKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptAES(SecretKey secKey, String plainText) {
        try {
            //Encrypt plain text using AES
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
            byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return byteCipherText;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptRSA(SecretKey symKey, PublicKey publicKey) {
        try {
            //Encrypt the key using RSA public key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            byte[] encryptedKey = cipher.doFinal(symKey.getEncoded()/*Secret Key From Step 1*/);

            //Send encrypted data (byteCipherText) + encrypted AES Key (encryptedKey)
            return encryptedKey;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
