package license;

import utils.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    public License(String nomeDaApp, String versao){
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
    // Caso False GerarDados.
    public Boolean isRegistered(){
        // Ler Dados do Cartão
        CartaoBiblioteca.getCartaoInfo();

        // Verifica se ficheiro existe.
        if ( fileExists(LicencaDados.getIdentificacaoCivil()+".lic") ) {

            // A licença é valida? ( id-firstlastname.lic )
            if ( !verificarLicenca("nomeapelido.lic")){
                return false;
            }

            // ( Ver se os dados da maquina coincidem com os dados do ficheiro id-firstlastname.dat  )
            if ( !verificarDadosMaquina("firstlastname.dat")){
                return false;
            }

            // Verifica se o programa foi alterado ou não
            // ( Ver o tamanho do ficheiro e comprar. ( .jar ) )
            if ( !verificarProgramaAlterado("path") ) {
                return false;
            }
            return true;
        }else {
            return false;
        }
    }



    /*
        Este m´etodo deve apresentar uma interface
        (da forma mais adequada `a aplica¸c˜ao, o que pode utilizar o stdout
        ou um interface gr´afico) indicando que a aplica¸c˜ao n˜ao se encontra
        registada e possibilitando iniciar o processo de registo de uma nova
        licen¸ca. Os detalhes sobre este processo encontram-se na subsection 1.4.
     */
    public Boolean startRegistration(){
        Scanner in = new Scanner(System.in);
        // Pedir Email
        System.out.print("Insira o seu email:");
        LicencaDados.setEmail(in.nextLine());

        // Nome, Email, Chave Publica CC, nº Identificação Civil
        CartaoBiblioteca.getCartaoInfo();

        // Dados da Maquina
        LicencaDados.setDadosMaquina(new DadosMaquina(Utils.getMb(), Utils.getCpu(),Utils.getMac()));

        // NomeDaApp e Versão
        LicencaDados.setNomeDaApp(nomeDaApp);
        LicencaDados.setVersao(versao);

        // Data de validade da linceça
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LicencaDados.setInicioValidadeLicenca(sdf.format(timestamp));

        // hash -> Tamanho do programa
        File file = new File("cliente.jar");
        long fileLength = file.length();
        LicencaDados.setTamanhoPrograma(fileLength);


        // Criar ficheiro .dat
        try {
            String nomeFicheiro = LicencaDados.getIdentificacaoCivil()+".dat";
            FileOutputStream fos = new FileOutputStream(nomeFicheiro);
            byte[] output = LicencaDados.stringTo().getBytes();
            fos.write(output);
            fos.flush();
            fos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Encriptar Dados
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
        // Mostrar os dados da Licença se Houver!
        // Ler Dados do Cartão
        CartaoBiblioteca.getCartaoInfo();

        // Verifica se ficheiro existe.
        if ( fileExists(LicencaDados.getIdentificacaoCivil()+".lic") ) {
            // Decifrar

            // Mostrar informaçao
        }else {
            System.out.println("Aplicação não se encontra registada.");
        }

    }

    // Ficheiro existe?
    private Boolean fileExists(String path){
        File f = new File(path);
        return f.exists() && !f.isDirectory(); // Se o resultado da função lógica der true ele manda true, se der false ele manda false.
    }

    // Verificar Licença.
    private Boolean verificarLicenca(String path){


        return false;
    }

    // Verificar se os dados da Maquina Coincidem
    private Boolean verificarDadosMaquina(String path)
    {
        // Desencriptar dados vindos do ficheiro e colocar na variavel ficheiro.
        DadosMaquina ficheiro = new DadosMaquina("a", "a", "a");
        DadosMaquina atual = getSystemInfo();

        return ficheiro.getBIOS().equals(atual.getBIOS()) &&
                ficheiro.getCPU().equals(atual.getCPU()) &&
                ficheiro.getMAC().equals(atual.getMAC());
    }

    // Verificar a varacidade do programa.
    private Boolean verificarProgramaAlterado(String path){
        File f = new File(path);
        long size = f.length();
        long sizeFromFile = getSizeFromFile();
        return false;
    }


    // Ler Ficheiro - https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    private byte[] lerFicheiro(String path) throws IOException {

        FileInputStream ficheiroLer = new FileInputStream(path);
        byte[] bufferTexto = new byte[(int)ficheiroLer.available()];
        ficheiroLer.read(bufferTexto);
        ficheiroLer.close();

        return bufferTexto;
    }

    // Encriptar dados
    private void encriptarDados(String textoClaro, String ficheiroChave, String ficheiroClaro) {
        // Ir Buscar chave
        // Encriptar os dados
        // Escrever no ficheiro.

        try {
            // alg, ficheiroChave, String ficheiroTextoClaro, String ficheiroTextoCifrado
            Crypto.gerarChave("", "");
            Crypto.cifrar("", "", "", "");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    // Desencriptar dados
    private void unEncriptarDados(byte[] textoEscuro) {
        // Ir Buscar chave
        // Desencriptar Chave
        // Desencriptar dados com a chave.
    }

    private long getSizeFromFile() {

        return 0;
    }

    private DadosMaquina getSystemInfo(){

        return null;
    }

    void getLicenseFile(){

    }
}
