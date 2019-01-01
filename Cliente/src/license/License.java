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

    /**
     * Contructor da licença
     * @param nomeDaApp Nome da aplicação
     * @param versao Versão da aplicação
     */

    public License(String nomeDaApp, String versao){
        this.nomeDaApp = nomeDaApp;
        this.versao = versao;

        if (CartaoBiblioteca.isCardReaderPresent()) {
            if (CartaoBiblioteca.isCardPresent()) {

                if( isRegistered() )  {
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

    /**
     * Método invocado no início da execução do programa.
     * <p>Valida a correcta execução da aplicação atual.
     * <p>Verifica o ficheiro de licença (.lic).
     * <p>Desencripta o ficheiro e compara os dados da mesma com as do utilizador e equipamento actual.
     * @return True se licença válida, senão False
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
            System.out.print(ANSI_YELLOW+"A verificar os dados da maquina..."+ANSI_RESET);
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

    /**
     * Inicia o processo de registo de uma nova licenca pedindo o email ao utilizador
     * <p>e recolhe dados do utilizador e da máquina
     * <p>gravando á posterior num ficheiro .dat
     * @return True se registo completa com sucesso, False em caso de erro
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

        System.out.print(ANSI_YELLOW+"A Obter dados da maquina..."+ANSI_RESET);
        // Dados da MaquinaString BIOS, String CPU, String MAC, String GC
        DadosMaquina dadosMaquina = Utils.getSystemInfo();
        LicencaDados.setDadosMaquina(dadosMaquina);
        System.out.println(ANSI_GREEN+"OK"+ANSI_RESET);


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
    }

    /**
     * Apresenta os dados da licença, no caso de existir.
     * <p>Caso contrário, apresenta a informação de que a aplicação não se encontra registada.
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

    /**
     * Verifica se o ficheiro existe
     * @param path nome do ficheiro a verificar
     * @return True se exister, False em caso contrário
     */
    private Boolean fileExists(String path){
        File f = new File(path);
        return f.exists() && !f.isDirectory();
        // Se o resultado da função lógica der true ele manda true, se der false ele manda false.
    }

    /**
     * Verificar se os dados da maquina coincidem com os da licença
     * <p>Tem uma margem de erro (delta) que permite que até um componente seja alterado
     * @return True se dados coincidem, False em caso contrário
     */
    private Boolean verificarDadosMaquina()
    {
        int delta = 0;
        // Desencriptar dados vindos do ficheiro e colocar na variavel ficheiro.
        DadosMaquina ficheiro = Crypto.ldjLic.getDadosMaquina();
        DadosMaquina atual = Utils.getSystemInfo();
        if ( !ficheiro.getMb().equals(atual.getMb()) ) delta++;
        if ( !ficheiro.getCpu().equals(atual.getCpu()) ) delta++;
        if ( !ficheiro.getMac().equals(atual.getMac())) delta++;
        if ( !ficheiro.getGc().equals(atual.getGc()) ) delta++;

        return delta < 2;
    }

    /**
     * Verifica se o programa foi alterado
     * <p>Compara o tamanho do programa com o valor armazenado no ficheiro de licença
     * @return True se intacto, false se alterado
     */
    private Boolean verificarProgramaAlterado(){
        File f = new File("Cliente.jar");
        long size = f.length();
        long sizeFromLic = Crypto.ldjLic.getTamanhoPrograma();
        return size == sizeFromLic;
    }

    /**
     * Verifica se dados do cartão de cidadão coincidem com os do ficheiro de licença
     * @return True se forem iguais, false em caso contrário
     */
    private boolean verificarCartaoCidadao () {
        LicencaDadosJson lic = Crypto.ldjLic;
        return (LicencaDados.getPrimeiroNome().equals(lic.getPrimeiroNome()) &&
                LicencaDados.getUltimoNome().equals(lic.getUltimoNome()) &&
                LicencaDados.getIdentificacaoCivil().equals(lic.getIdentificacaoCivil()));
    }

    /**
     * Verifica se o nome e a versão da aplicação coincidem com os da licença
     * @return True se estiver correcto, False se incorrecto
     */
    private boolean verificaNomeVersao() {
        LicencaDadosJson lic = Crypto.ldjLic;
        return versao.equals(lic.getVersao()) && nomeDaApp.equals(lic.getNomeDaApp());
    }

}
