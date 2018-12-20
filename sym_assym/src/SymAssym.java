import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SymAssym {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private KeyPairGenerator kpg;
    private KeyPair pair;

    public SymAssym() throws NoSuchAlgorithmException {
        this.kpg = KeyPairGenerator.getInstance("RSA");
        this.kpg.initialize(2048);
    }

   /* public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }*/

    /*public void createAsymKeys() {
        this.pair = this.kpg.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }*/
    public static KeyPair createAsymKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();
//        PrivateKey privateKey = pair.getPrivate();
//        PublicKey publicKey = pair.getPublic();
        return pair;
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
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            byte[] encryptedKey = cipher.doFinal(symKey.getEncoded()/*Secret Key From Step 1*/);

            return encryptedKey;

            //TODO write to file instead
            //Send encrypted data (byteCipherText) + encrypted AES Key (encryptedKey)
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


    public static String decrypt(PrivateKey privateKey, byte[] byteCipherText, byte[] encryptedKey) {
        try {
            //On the client side, decrypt symmetric key using RSA private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
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
}