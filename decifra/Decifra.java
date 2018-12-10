/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decifra;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Decifra {

    /**
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
     
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
        FileInputStream tCifrado = new FileInputStream(args[0]);
        byte[] buffTCifrado = new byte[(int)tCifrado.available()];
        tCifrado.read(buffTCifrado);
        
        FileInputStream chave = new FileInputStream(args[1]);
        byte[] buffChave = new byte[(int)chave.available()];
        chave.read(buffChave);
        
        SecretKey sk = new SecretKeySpec(buffChave, "DES");
        
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.DECRYPT_MODE, sk);
        byte[] texto = c.doFinal(buffTCifrado);
        
        FileOutputStream tClaro = new FileOutputStream(args[2]);
        tClaro.write(texto);
        tClaro.flush();
        tClaro.close();
    }    
}
