package license;

import utils.Assimetrico;
import utils.Simetrico;
import utils.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
            if ( !verificarLicenca(LicencaDados.getIdentificacaoCivil()+".lic")){
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
        LicencaDados.setDadosMaquina(new DadosMaquina(Utils.getMB(), Utils.getCPU(),Utils.getNW()));

        // NomeDaApp e Versão
        LicencaDados.setNomeDaApp(nomeDaApp);
        LicencaDados.setVersao(versao);

//        // Data de validade da linceça
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());System.currentTimeMillis()+1year
//        LicencaDados.setInicioValidadeLicenca(sdf.format(timestamp));

        // hash -> Tamanho do programa
        File file = new File("cliente.jar");
        long fileLength = file.length();
        LicencaDados.setTamanhoPrograma(fileLength);


        // Criar ficheiro .dat
        try {
            String nomeFicheiro = LicencaDados.getIdentificacaoCivil()+".dat";
            FileOutputStream fos = new FileOutputStream(nomeFicheiro);
            System.out.println(LicencaDados.stringTo());
            byte[] output = Utils.encriptarB64(LicencaDados.stringTo()).getBytes(StandardCharsets.UTF_8);
            fos.write(output);
            fos.flush();
            fos.close();

            CartaoBiblioteca.assinar(nomeFicheiro, "assinatura.dat");

            String finalEnc = encriptarDadosSim(nomeFicheiro);
            finalEnc += System.getProperty("line.separator") ;
            // Encriptar Asym Chave
            finalEnc += encriptarChaveAsym();

            // Juntar Tudo!!
            FileOutputStream fosFinal = new FileOutputStream(nomeFicheiro);
            byte[] outputFinal = Utils.encriptarB64(finalEnc).getBytes();
            fosFinal.write(outputFinal);
            fosFinal.flush();
            fosFinal.close();

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
    private String encriptarDadosSim(String fTextoClaro) {
        try {
            FileInputStream fis = new FileInputStream(fTextoClaro);;
            byte[] fbytes = new byte[(int) fis.available()];
            fis.read(fbytes);
            fis.close();

            Simetrico s = new Simetrico();
            String encrypted = s.encrypt(new String(fbytes, StandardCharsets.UTF_8));
            return encrypted;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String encriptarChaveAsym() {
        Assimetrico ac = null;
        try {

            ac = new Assimetrico(1024);
            PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");

            String encrypted_msg = ac.encryptText(Simetrico.SYM_KEY, privateKey);
            return encrypted_msg;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private long getSizeFromFile() {

        return 0;
    }

    private DadosMaquina getSystemInfo(){

        return null;
    }
}
