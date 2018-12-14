package license;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class Crypto {

    // O que é alg?
    protected static void gerarChave(String alg, String ficheiroChave) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        KeyGenerator kg = KeyGenerator.getInstance(alg);
        kg.init(256);
        SecretKey sk = kg.generateKey();
        byte[] skEncoded = sk.getEncoded();
        FileOutputStream fo = new FileOutputStream(ficheiroChave);
        fo.write(skEncoded);
        fo.flush();
    }

    // ALG = Algoritmo de Encriptação AES | DES
    static void cifrar(String alg, String ficheiroChave, String ficheiroTextoClaro, String ficheiroTextoCifrado) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        FileInputStream FISTextoClaro = new FileInputStream(ficheiroTextoClaro);
        byte[] buffTextoClaro = new byte[(int)FISTextoClaro.available()];
        FISTextoClaro.read(buffTextoClaro);

        FileInputStream chave = new FileInputStream(ficheiroChave);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);

        SecretKey sk = new SecretKeySpec(buffChave, alg);

        Cipher c = Cipher.getInstance(alg);
        c.init(Cipher.ENCRYPT_MODE, sk);
        byte[] criptograma = c.doFinal(buffTextoClaro);

        FileOutputStream textoCifrado = new FileOutputStream(ficheiroTextoCifrado);
        textoCifrado.write(criptograma);
        textoCifrado.flush();
        textoCifrado.close();
    }

    static String decifrar(String alg, String ficheiroChave, String ficheiroTextoCifrado) throws  IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream textoCifrado = new FileInputStream(ficheiroTextoCifrado);
        byte[] buffTCifrado = new byte[(int)textoCifrado.available()];
        textoCifrado.read(buffTCifrado);

        FileInputStream chave = new FileInputStream(ficheiroChave);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);

        textoCifrado.close();
        chave.close();

        SecretKey sk = new SecretKeySpec(buffChave, alg);

        Cipher c = Cipher.getInstance(alg);
        c.init(Cipher.DECRYPT_MODE, sk);
        byte[] texto = c.doFinal(buffTCifrado);
        String textoClaro = new String(texto);
        return textoClaro;
    }
}
