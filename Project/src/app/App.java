package app;

import license.License;

public class App {

    public static void main (String[] args ){

        init();


        System.out.println("Hello World.");
    }

    private static void init(){
        License lic = new License("HelloWorld", "1.0.1");

        if( lic.isRegistered() )  {
            System.out.println("Está registado.");
            return;
        }else {
            System.out.println("Não está registado.");
            lic.startRegistration();
            // Registar

            return;
            // System.exit(0);
        }
    }

}
