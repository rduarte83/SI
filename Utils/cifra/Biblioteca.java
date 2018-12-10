/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Biblioteca {
    
    public static void gerarChave(String alg, String fichChave) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        KeyGenerator kg = KeyGenerator.getInstance(alg);
        kg.init(256);
        SecretKey sk = kg.generateKey();
        byte[] x = sk.getEncoded();
        FileOutputStream fo = new FileOutputStream(fichChave);
        fo.write(x);
        fo.flush();
    }
    
    public static void cifrar(String alg, String fichChave ,String fichTClaro, String fichTCifrado) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        FileInputStream tClaro = new FileInputStream(fichTClaro);
        byte[] buffTClaro = new byte[(int)tClaro.available()];
        tClaro.read(buffTClaro);
        
        FileInputStream chave = new FileInputStream(fichChave);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);
        
        SecretKey sk = new SecretKeySpec(buffChave, alg);
        
        Cipher c = Cipher.getInstance(alg);
        c.init(Cipher.ENCRYPT_MODE, sk);
        byte[] criptograma = c.doFinal(buffTClaro);
     
        FileOutputStream tCifrado = new FileOutputStream(fichTCifrado);
        tCifrado.write(criptograma);
        tCifrado.flush();
        tCifrado.close();
    }
    
    public static void decifrar(String alg, String fichChave, String fichTCifrado, String fichTClaro) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream tCifrado = new FileInputStream(fichTCifrado);
        byte[] buffTCifrado = new byte[(int)tCifrado.available()];
        tCifrado.read(buffTCifrado);
        
        FileInputStream chave = new FileInputStream(fichChave);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);
        
        SecretKey sk = new SecretKeySpec(buffChave, alg);
        
        Cipher c = Cipher.getInstance(alg);
        c.init(Cipher.DECRYPT_MODE, sk);
        byte[] texto = c.doFinal(buffTCifrado);
        
        FileOutputStream tClaro = new FileOutputStream(fichTClaro);
        tClaro.write(texto);
        tClaro.flush();
        tClaro.close();
    }    
}
