package license;

import java.util.Scanner;

public class License {

    private String nomeDaApp;
    private String versao;
    private static Boolean license = false;
    /*
        Este m´etodo corresponde
        `a inicializa¸c˜ao da biblioteca de controlo de execu¸c˜ao. No
        caso de uma implementa¸c˜ao orientada a objetos, este m´etodo pode
        corresponder ao construtor.
     */
    void init(String nomeDaApp, String versao){
        this.nomeDaApp = nomeDaApp;
        this.versao = versao;
    }

    /*
        Uma aplica¸c˜ao dever´a invocar este m´etodo no
        in´ıcio da sua execu¸c˜ao e sempre que ache necess´ario. Este m´etodo dever´a
        executar de forma r´apida e eficiente, validando a correta execu¸c˜ao da
        aplica¸c˜ao atual. Caso se verifique que a aplica¸c˜ao executa de forma
        autorizada, ele n˜ao dever´a imprimir qualquer valor e dever´a devolver o
        valor True. Caso contr´ario dever´a devolver o valor False
    */
    Boolean isRegistered(){
        return license;
    }

    /*
        Este m´etodo deve apresentar uma interface
        (da forma mais adequada `a aplica¸c˜ao, o que pode utilizar o stdout
        ou um interface gr´afico) indicando que a aplica¸c˜ao n˜ao se encontra
        registada e possibilitando iniciar o processo de registo de uma nova
        licen¸ca. Os detalhes sobre este processo encontram-se na subsection 1.4.
     */
    Boolean startRegistration(){
        Scanner in = new Scanner(System.in);

        return false;
    }

    /*
        Esta fun¸c˜ao apresenta os dados da licen¸ca
        atual (caso ela exista), ou informa¸c˜ao de que a aplica¸c˜ao n˜ao se encontra
        registada. A apresenta¸c˜ao desta informa¸c˜ao, mais uma vez, dever´a ser
        efetuada da maneira mais adequada `a aplica¸c˜ao. Para uma aplica¸c˜ao
        de linha de comandos esta informa¸c˜ao pode ser escrita para o terminal
        (stdout)
     */
    void showLicenseInfo(){
        if(license){
            System.out.println("Aplicação não se encontra registada.");
            startRegistration();
        }else {
            System.out.println("Dados da licença");
            System.out.println("xxx - xxx");
        }
    }

    void getSystemInfo(){

    }

    void getLicenseFile(){

    }
}
