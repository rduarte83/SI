package License;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {

    /**
     * Gera uma chave simétrica
     * <p>Encripta o texto em claro simetricamente (AES) e converte para Base64
     * <p>Encripta a chave simétrica assimetricamente(RSA) e converte para Base64
     * <p>Concatena os 2 elementos e converte para bytes
     * @param plainText texto em claro
     * @param publicKey chave pública
     * @return byte[] criptograma+chave encriptada
     */
    public static byte[] encriptarTudo(String plainText, String publicKey)
    {
        String resposta = "";
        //Create Keys
        String symKey = createSymKey();

        //Encoding
        byte[] encodedKey = Base64.getDecoder().decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        //Encrypt sym AES
        byte [] symText = encryptAES(symKeyDec , plainText);

        resposta += Base64.getEncoder().encodeToString(symText);
        resposta += "\n";

        //Encrypt Assym RSA
        byte[] encryptedKey = encryptRSA(symKeyDec, loadPublicKey(publicKey));

        resposta += Base64.getEncoder().encodeToString(encryptedKey);
        return Base64.getEncoder().encode(resposta.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Desencripta assimetricamente a chave simétrica
     * <p>Desencripta o criptograma usando a chave anterior
     * @param privateKey chave privada usada na desencriptação assimétrica
     * @param byteCipherText texto cifrado
     * @param encryptedKey chave simétrica cifrada assimetricamente
     * @return String - texto em claro
     * @see PrivateKey
     */
    public static String desencriptar(PrivateKey privateKey, byte[] byteCipherText, byte[] encryptedKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            byte[] decryptedKey = cipher.doFinal(encryptedKey);

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

    /**
     * Gera uma chave simétrica de 128bits
     * @return
     */
    public static String createSymKey() {
        try {
            KeyGenerator generator = null;
            generator = KeyGenerator.getInstance("AES");
            generator.init(128); // The AES key size in number of bits
            SecretKey secKey = generator.generateKey();

            String stringKey = Base64.getEncoder().encodeToString(secKey.getEncoded());

            return stringKey;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converte uma chave pública de Base64 para PublicKey
     * @param stored chave pública em Base64
     * @return PublicKey chave pública
     * @see PublicKey
     */
    public static PublicKey loadPublicKey(String stored) {
        try {
            byte[] data = Base64.getDecoder().decode(stored);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = kf.generatePublic(spec);
            return pubKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converte uma chave privada de Base64 para PrivateKey
     * @param stored chave privada em Base64
     * @return PrivateKey chave privada
     * @see PrivateKey
     */
    public static PrivateKey loadPrivateKey(String stored) {
        try {
            byte[] data = Base64.getDecoder().decode(stored);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(spec);
            return privKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encripta um texto com encriptação simétrica AES
     * @param secKey chave simétrica gerada
     * @param plainText texto em claro
     * @return byte[] - texto cifrado em bytes
     * @see SecretKey
     */
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

    /** Encripta uma chave com encriptação assimétrica RSA
     * @param symKey chave a ser encriptada
     * @param publicKey chave pública usada para a encriptação
     * @return
     * @see SecretKey
     * @see PublicKey
     */
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