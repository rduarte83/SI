package license;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class License {

    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001b[032m";
    public static final String ANSI_YELLOW = "\u001b[033m";

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

        if (CartaoBiblioteca.isCardReaderPresent()) {
            if (CartaoBiblioteca.isCardPresent()) {

                if( isRegistered() )  {
                    System.out.println(ANSI_YELLOW+"Está registado."+ANSI_RESET);
                    System.out.println(ANSI_YELLOW+"1 - Ir para o programa"+ANSI_RESET);
                    System.out.println(ANSI_YELLOW+"2 - Ver dados da Licença"+ANSI_RESET);
                    System.out.println(ANSI_YELLOW+"3 - Sair do programa"+ANSI_RESET);
                    Scanner sc = new Scanner(System.in);
                    String opt = sc.nextLine();
                    switch (opt.toLowerCase()) {
                        case "1":
                            break;
                        case "2":
                            showLicenseInfo();
                            System.exit(0);
                            break;
                        case "3":
                            System.exit(0);
                            break;
                    }
                    return;
                } else {
                    System.out.println(ANSI_YELLOW+"Não está registado."+ANSI_RESET);
                    System.out.println(ANSI_YELLOW+"Iniciar o registo? (S/N)"+ANSI_RESET);
                    Scanner sc = new Scanner(System.in);
                    String opt = sc.nextLine();
                    switch (opt.toLowerCase()) {
                        case "s":
                        case "sim":
                            startRegistration();
                            System.exit(0);
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
        System.out.print(ANSI_YELLOW+"A obter informação do cartão de cidadão..."+ANSI_RESET);
        CartaoBiblioteca.getCartaoInfo();
        System.out.println(ANSI_GREEN+"Ok"+ANSI_RESET);
        // Verifica se ficheiro existe.
        if ( fileExists(LicencaDados.getIdentificacaoCivil()+".lic") ) {

            // Desencriptar a Licença
            if ( !Crypto.desencriptarLicenca(LicencaDados.getIdentificacaoCivil()) ) {
                return false;
            }

            // Verificar Cartão de Cidadão
            if ( !verificarCartaoCidadao() ){
                return false;
            }

            // ( Ver se os dados da maquina coincidem com os dados do ficheiro   )
            System.out.println(ANSI_YELLOW+"A verificar os dados da maquina..."+ANSI_RESET);
            if ( !verificarDadosMaquina()){
                return false;
            }
            System.out.println(ANSI_GREEN+"Ok"+ANSI_RESET);

            // Verifica se o programa foi alterado ou não
            if ( !verificaNomeVersao() ) {
                return false;
            }

            // ( Ver o tamanho do ficheiro e comprar. ( .jar ) )
            if ( !verificarProgramaAlterado() ) {
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
        System.out.print(ANSI_YELLOW+"Insira o seu email:"+ANSI_RESET);
        LicencaDados.setEmail(in.nextLine());

        System.out.println(ANSI_YELLOW+"A gerar licença..."+ANSI_RESET);
        // Gerar Par de chaves Asym
        Assinatura.generateNewAsymKeys();

        // Nome, Email, Chave Publica CC, nº Identificação Civil
        CartaoBiblioteca.getCartaoInfo();

        // Dados da MaquinaString BIOS, String CPU, String MAC, String GC
        DadosMaquina dadosMaquina = Utils.getSystemInfo();
        LicencaDados.setDadosMaquina(dadosMaquina);

        // NomeDaApp e Versão
        LicencaDados.setNomeDaApp(nomeDaApp);
        LicencaDados.setVersao(versao);

        // hash -> Tamanho do programa
        File file = new File("Cliente.jar");
        long fileLength = file.length();
        LicencaDados.setTamanhoPrograma(fileLength);

        // Criar ficheiro .dat
        try {
            // Parse to Json the LicençaDados class.
            ObjectMapper mapper = new ObjectMapper();
            LicencaDadosJson ldj = new LicencaDadosJson();
            ldj.setDadosClass();
            String jsonToEncript = mapper.writeValueAsString(ldj);

            // Nome do Ficheiro Criar... & Encriptar
            String nomeFicheiro = LicencaDados.getIdentificacaoCivil()+".dat";

            String jsonEcripted = Base64.getEncoder().encodeToString(jsonToEncript.getBytes(StandardCharsets.UTF_8));

            // Assinar Cartão
            String textoAssB64 = CartaoBiblioteca.assinar(jsonEcripted);

            // Juntar Tudo!!
            FileOutputStream fosFinal = new FileOutputStream(nomeFicheiro);
            byte[] finalEnc = Crypto.encriptarTudo(textoAssB64);
            fosFinal.write(finalEnc);
            fosFinal.flush();
            fosFinal.close();
            System.out.println(ANSI_GREEN+"Licença gerada com sucesso!"+ANSI_RESET);
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
    public void showLicenseInfo(){
        // Mostrar os dados da Licença se Houver!
        // Ler Dados do Cartão
        CartaoBiblioteca.getCartaoInfo();

        // Verifica se ficheiro existe.
        if ( fileExists(LicencaDados.getIdentificacaoCivil()+".lic") ) {
            // Caso o ldjLic == null, Ler ficheiro e desencriptarLicença
            if (  Crypto.ldjLic == null  && Crypto.desencriptarLicenca(LicencaDados.getIdentificacaoCivil()+".lic")) return;
            LicencaDadosJson lic = Crypto.ldjLic;

            System.out.println("Aplicação encontra-se registada.");

            // Mostrar informaçao ( Sobre a Aplicação )
            System.out.println("-Dados do Cliente.jar-");
            System.out.println("Nome da App:"+ lic.getNomeDaApp() );
            System.out.println("Versão:"+ lic.getVersao() );
            System.out.println("Inicio da Validade:"+ lic.getInicioValidadeLicenca() );
            System.out.println("Fim da Validade:"+ lic.getFimValidadeLicenca() );

            // Dados da Maquina
            System.out.println("-Dados da Maquina-");
            System.out.println("CPU:"+lic.getDadosMaquina().getCpu());
            System.out.println("GPU:"+lic.getDadosMaquina().getGc());
            System.out.println("MAC:"+lic.getDadosMaquina().getMac());
            System.out.println("MotherBoard:"+lic.getDadosMaquina().getMb());

            // Dados do Utilizador.
            System.out.println("-Dados do Utilizador-");
            System.out.println("Email:"+lic.getEmail());
            System.out.println("Primeiro nome:"+lic.getPrimeiroNome());
            System.out.println("Ultimo nome:"+lic.getUltimoNome());
            System.out.println("Identificação Civil:"+lic.getIdentificacaoCivil());
        }else {
            System.out.println("Aplicação não se encontra registada.");
        }
    }

    // Ficheiro existe?
    private Boolean fileExists(String path){
        File f = new File(path);
        return f.exists() && !f.isDirectory(); // Se o resultado da função lógica der true ele manda true, se der false ele manda false.
    }

    // Verificar se os dados da Maquina Coincidem
    private Boolean verificarDadosMaquina()
    {
        // Desencriptar dados vindos do ficheiro e colocar na variavel ficheiro.
        DadosMaquina ficheiro = Crypto.ldjLic.getDadosMaquina();
        DadosMaquina atual = Utils.getSystemInfo();

        return ficheiro.getMb().equals(atual.getMb()) &&
                ficheiro.getCpu().equals(atual.getCpu()) &&
                ficheiro.getMac().equals(atual.getMac()) &&
                ficheiro.getGc().equals(atual.getGc());
    }

    // Verificar a varacidade do programa.
    private Boolean verificarProgramaAlterado(){
        File f = new File("Cliente.jar");
        long size = f.length();
        long sizeFromLic = Crypto.ldjLic.getTamanhoPrograma();
        return size == sizeFromLic;
    }

    // Verificar Cartão de Cidadão
    private boolean verificarCartaoCidadao () {
        LicencaDadosJson lic = Crypto.ldjLic;
        return (LicencaDados.getPrimeiroNome().equals(lic.getPrimeiroNome()) &&
                LicencaDados.getUltimoNome().equals(lic.getUltimoNome()) &&
                LicencaDados.getIdentificacaoCivil().equals(lic.getIdentificacaoCivil()));
    }

    private boolean verificaNomeVersao() {
        LicencaDadosJson lic = Crypto.ldjLic;
        return versao.equals(lic.getVersao()) && nomeDaApp.equals(lic.getNomeDaApp());
    }

}
