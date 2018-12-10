/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifra;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cifra {
   
    /**
     * @param args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        FileInputStream tClaro = new FileInputStream(args[0]);
        byte[] buffTClaro = new byte[(int)tClaro.available()];
        tClaro.read(buffTClaro);
        
        FileInputStream chave = new FileInputStream(args[1]);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);
        
        SecretKey sk = new SecretKeySpec(buffChave, "DES");
        
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.ENCRYPT_MODE, sk);
        byte[] criptograma = c.doFinal(buffTClaro);
     
        FileOutputStream tCifrado = new FileOutputStream(args[2]);
        tCifrado.write(criptograma);
        tCifrado.flush();
        tCifrado.close();
    }
} 
