package utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.UnsupportedEncodingException;

public class Simetrico {

    private static String sk = "SI2018JoaoRui";
    // Objects required for encryption/decryption
//    private static SecretKey secretKey;
//    private static Base64.Encoder encoder;
//    private static Base64.Decoder decoder;
//
//    // In constructor
//
//
//    public static void init ()
//    {
//        try {
//            secretKey = new SecretKeySpec(sk.getBytes("UTF-8"), "AES");
//            encoder = Base64.getUrlEncoder();
//            decoder = Base64.getUrlDecoder();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


    public static String encryptText(String msg) {
//        try {
//
//            // Get byte array which has to be encrypted.
//            byte[] plainTextByte = toByteArray(msg);
//
//            // Encrypt the bytes using the secret key
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            byte[] encryptedByte = cipher.doFinal(plainTextByte);
//
//            // Use Base64 encoder to encode the byte array
//            // into Base 64 representation. Requires Java 8.
//            return encoder.encodeToString(encryptedByte);
//
//        } catch (Exception e) {
//            System.out.println("Failed to encrypt" + e.toString());
//            //logger.error("Failed to encrypt", e);
//        }
//
//        return null;
        try {
            return Base64.encode(msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptText(String msg) {
//        try {
//            // Decode Base 64 String into bytes array.
//            byte[] encryptedByte = decoder.decode(msg);
//
//            //Do the decryption
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            byte[] decryptedByte = cipher.doFinal(encryptedByte);
//
//            // Get hexadecimal string from the byte array.
//            return toHexString(decryptedByte);
//
//        } catch (Exception e) {
////            logger.error("Failed to decrypt {}", encrypted, e);
//        }
//        return null;
        try {
            return new String(Base64.decode(msg), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }




    // ALG = Algoritmo de Encriptação AES | DES
    // AES - Mais seguro
//    static void gerarChave(String alg, String ficheiroChave) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
//        KeyGenerator kg = KeyGenerator.getInstance(alg);
//        kg.init(256);
//        SecretKey sk = kg.generateKey();
//        byte[] skEncoded = sk.getEncoded();
//        FileOutputStream fo = new FileOutputStream(ficheiroChave);
//        fo.write(skEncoded);
//        fo.flush();
//    }
//
//    // Encriptação Simétrica.
//    static void cifrar(String alg, String ficheiroChave, String textoClaro, String ficheiroTextoCifrado) {
//        /*FileInputStream FISTextoClaro = new FileInputStream(ficheiroTextoClaro);
//        byte[] buffTextoClaro = new byte[(int)FISTextoClaro.available()];
//        FISTextoClaro.read(buffTextoClaro);*/
//        try {
//            byte[] byteTextoClaro = textoClaro.getBytes();
//
//            // Ler o par de chaves
//            FileInputStream chave = new FileInputStream(ficheiroChave);
//            byte[] buffChave = new byte[(int)chave.available()];
//            chave.read(buffChave);
//
//            //
//            SecretKey sk = new SecretKeySpec(buffChave, alg);
//
//            Cipher c = Cipher.getInstance(alg);
//            c.init(Cipher.ENCRYPT_MODE, sk);
//            byte[] criptograma = c.doFinal(byteTextoClaro);
//
//            // Mandar para o ficheiro .dat
//            FileOutputStream textoCifrado = new FileOutputStream(ficheiroTextoCifrado);
//            textoCifrado.write(criptograma);
//            textoCifrado.flush();
//            textoCifrado.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // Desencriptção Simétrica.
//    static String decifrar(String alg, String ficheiroChave, String ficheiroTextoCifrado) {
//        try {
//            FileInputStream textoCifrado = new FileInputStream(ficheiroTextoCifrado);
//            byte[] buffTCifrado = new byte[(int) textoCifrado.available()];
//            textoCifrado.read(buffTCifrado);
//
//            FileInputStream chave = new FileInputStream(ficheiroChave);
//            byte[] buffChave = new byte[(int) chave.available()];
//            chave.read(buffChave);
//
//            textoCifrado.close();
//            chave.close();
//
//            SecretKey sk = new SecretKeySpec(buffChave, alg);
//
//            Cipher c = Cipher.getInstance(alg);
//            c.init(Cipher.DECRYPT_MODE, sk);
//            byte[] texto = c.doFinal(buffTCifrado);
//            String textoClaro = new String(texto);
//
//            return textoClaro;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
