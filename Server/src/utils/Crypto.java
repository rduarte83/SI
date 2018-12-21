package utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {

    public static byte[] encriptarTudo(String plainText, String publicKey)
    {
        String resposta = "";
        //Create Keys
        String symKey = createSymKey();

        System.out.println("SymKey: "+symKey);
        System.out.println("TClaro: "+ plainText);

        //Encoding
        byte[] encodedKey = Base64.getDecoder().decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        //Encrypt sym AES
        byte [] symText = encryptAES(symKeyDec , plainText);
        System.out.println("TCifrado: "+symText);

        resposta += Base64.getEncoder().encodeToString(symText);

        resposta += "\n";

        //Encrypt Assym RSA
        byte[] encryptedKey = encryptRSA(symKeyDec, loadPublicKey(publicKey));
        System.out.println("encryptedKey: "+encryptedKey);

        resposta += Base64.getEncoder().encodeToString(encryptedKey);
        return Base64.getEncoder().encode(resposta.getBytes(StandardCharsets.UTF_8));
    }

    public static String desencriptar(PrivateKey privateKey, byte[] byteCipherText, byte[] encryptedKey) {
        try {
            //On the client side, decrypt symmetric key using RSA private key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            byte[] decryptedKey = cipher.doFinal(encryptedKey);

            //Decrypt the cipher using decrypted symmetric key
            //Convert bytes to AES SecretKey
            SecretKey originalKey = new SecretKeySpec(decryptedKey ,0, decryptedKey.length, "AES");

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
            String plainText = new String(bytePlainText);

            return plainText;

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

    public static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

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

    public static KeyPair createAsymKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();
        return pair;
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

    public static PrivateKey loadPrivateKey(String stored) {
        try {
            byte[] data = Base64.getDecoder().decode(stored);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(spec);
            System.out.println("PrivateKey Original: "+privKey);
            return privKey;
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