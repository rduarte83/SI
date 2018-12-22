import License.License;

import java.util.Scanner;

public class Server {

    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_RED = "\033[31;1m";
    public static final String ANSI_GREEN = "\u001b[032m";
    public static final String ANSI_YELLOW = "\u001b[033m";

    public static void main ( String[] args ){
        menu(args);
    }

    public static void menu(String[] args) {

        if ( args.length < 1 ){
            showHelp();
            return;
        }


        int index = 0;
        for (String arg : args) {
            // Comando Gerar
            switch (arg) {
                case "-gerar":
                    if (index + 1 < args.length) {
                        String filename = args[index + 1];
                        License.decifrar(filename);
                    }else {
                        System.out.println(ANSI_RED+"Insira o nome do ficheiro."+ANSI_RESET);
                    }
                    return;
                case "-ler":  // Comando Ler
                    String filename = args[index + 1];
                    //License.decifrar(filename);
                    return;
                case "-help":
                    showHelp();
                    return;
            }
            index++;
        }
        System.out.println(ANSI_RED+"Comando inválido.."+ANSI_RESET);
        showHelp();
    }

    private static void showHelp(){
        System.out.println(ANSI_YELLOW+"Pode usar os seguintes comandos:"+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"-gerar {nome do ficheiro dat} ( tem de estar na pasta Dat! )"+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"-ler {nome do ficheiro dat}"+ANSI_RESET);
        System.out.println(ANSI_YELLOW+"-help ( para obter a lista de comandos."+ANSI_RESET);
    }
}
