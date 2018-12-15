package app;

import license.CartaoBiblioteca;
import license.License;

public class App {

    public static void main (String[] args ){

        init();


        System.out.println("Hello World.");
    }

    private static void init(){
        License lic = new License("HelloWorld", "1.0.1");
        CartaoBiblioteca.assinar("153435623.dat", "assinatura.dat");

        /*Simetrico.init();
        String msg = "Olá João Lindo sou o Rui.";
        System.out.println("Texto em Claro:"+msg);
        System.out.println("Encriptado:"+Simetrico.encryptText(msg));
        System.out.println("Desencriptado:"+Simetrico.decryptText(msg));*/

        /*if( lic.isRegistered() )  {
            System.out.println("Está registado.");
            return;
        }else {
            System.out.println("Não está registado.");
            lic.startRegistration();
            // Registar

            return;
            // System.exit(0);
        }*/
    }

}
