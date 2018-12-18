package app;

import license.CartaoBiblioteca;
import license.License;

import java.util.Scanner;

public class App {

    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001b[032m";
    public static final String ANSI_YELLOW = "\u001b[033m";
    public static void main (String[] args ){
        init();

        // Programa com esteroides. LOW PRIORITY
        System.out.println("Hello World.");
    }

    private static void init(){
        License lic = new License("HelloWorld", "1.0.1");

        if (CartaoBiblioteca.isCardReaderPresent()) {
            if (CartaoBiblioteca.isCardPresent()) {
                if( lic.isRegistered() )  {
                    return;
                } else {
                    System.out.println(ANSI_YELLOW+"Não está registado."+ANSI_RESET);
                    System.out.println(ANSI_YELLOW+"Iniciar o registo? (S/N)"+ANSI_RESET);
                    Scanner sc = new Scanner(System.in);
                    String opt = sc.nextLine();
                    switch (opt.toLowerCase()) {
                        case "s":
                        case "sim":
                            lic.startRegistration();
                            break;
                        case "n":
                        case "nao":
                        case "não":
                            System.exit(0);
                            break;
                    }
                }
            } else {
                System.out.println(ANSI_RED+"Cartão não encontrado."+ANSI_RESET);
                System.exit(0);
            }
        } else {
            System.out.println(ANSI_RED+"Leitor de Cartões não detectado."+ANSI_RESET);
            System.exit(0);
        }
    }
}
