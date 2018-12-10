/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cartaocidadao;

//import java.io.FileInputStream;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

/**
 *
 * @author Licinio
 */
public class CartaoCidadao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException, NoSuchProviderException, javax.security.cert.CertificateException, InvalidKeySpecException {
        Scanner sc = new Scanner(System.in);
        int opcao;
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Assinar documento");
            System.out.println("2 - Validar assinatura");
            opcao = sc.nextInt();
            sc.nextLine();
            switch (opcao) {
                case 1:
                    CartaoCidadaoAux.assinar(args[0], args[1], args[2]);
                    break;
                case 2:
                    CartaoCidadaoAux.validarAssinatura(args[0], args[1], args[2]);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
    }
}
