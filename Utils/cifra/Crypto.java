/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Crypto {
    
    
    /**
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.NoSuchPaddingException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, FileNotFoundException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        Scanner sc = new Scanner(System.in);
        int opcao = 1;
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Gerar chave");
        System.out.println("2 - Cifrar texto");
        System.out.println("3 - Decifrar texto");
        System.out.println("0 - Sair");
        opcao = sc.nextInt();
        sc.nextLine();
        switch (opcao)
        {
            case 1:
                Biblioteca.gerarChave(args[0], args[1]);
                System.out.println(args[0]);
                System.out.println("Chave criada com sucesso\n");
                break;
            case 2:
                Biblioteca.cifrar(args[0], args[1], args[2], args[3]);
                System.out.println(args[0]);
                System.out.println("Ficheiro " + args[2] + " cifrado com sucesso");
                break;
            case 3:
                Biblioteca.decifrar(args[0], args[1], args[2], args[3]);
                System.out.println(args[0]);
                System.out.println("Ficheiro " + args[2] + " decifrado com sucesso");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }
    
}
