package license;

public class LicencaDados {

    private static String primeiroNome;
    private static String ultimoNome;
    private static String email;
    private static String identificacaoCivil;
    private static String chavePublica;
    private static DadosMaquina dadosMaquina;
    private static String nomeDaApp;
    private static String versao;
    private static long tamanhoPrograma;
    private static String inicioValidadeLicenca;
    private static String fimValidadeLicenca;

    public static void setChavePublica(String chavePublica) {LicencaDados.chavePublica = chavePublica;}

    public static String getChavePublica() {return chavePublica; }

    public static String getPrimeiroNome() {
        return primeiroNome;
    }

    public static void setPrimeiroNome(String primeiroNome) {
        LicencaDados.primeiroNome = primeiroNome;
    }

    public static String getUltimoNome() {
        return ultimoNome;
    }

    public static void setUltimoNome(String ultimoNome) {
        LicencaDados.ultimoNome = ultimoNome;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        LicencaDados.email = email;
    }

    public static String getIdentificacaoCivil() {
        return identificacaoCivil;
    }

    public static void setIdentificacaoCivil(String identificacaoCivil) {
        LicencaDados.identificacaoCivil = identificacaoCivil;
    }

    public static DadosMaquina getDadosMaquina() {
        return dadosMaquina;
    }

    public static void setDadosMaquina(DadosMaquina dadosMaquina) {
        LicencaDados.dadosMaquina = dadosMaquina;
    }

    public static String getNomeDaApp() {
        return nomeDaApp;
    }

    public static void setNomeDaApp(String nomeDaApp) {
        LicencaDados.nomeDaApp = nomeDaApp;
    }

    public static String getVersao() {
        return versao;
    }

    public static void setVersao(String versao) {
        LicencaDados.versao = versao;
    }

    public static String getInicioValidadeLicenca() {
        return inicioValidadeLicenca;
    }

    public static void setInicioValidadeLicenca(String inicioValidadeLicenca) {
        LicencaDados.inicioValidadeLicenca = inicioValidadeLicenca;
    }

    public static String getFimValidadeLicenca() {
        return fimValidadeLicenca;
    }

    public static void setFimValidadeLicenca(String fimValidadeLicenca) {
        LicencaDados.fimValidadeLicenca = fimValidadeLicenca;
    }

    public static long getTamanhoPrograma() {
        return tamanhoPrograma;
    }

    public static void setTamanhoPrograma(long tamanhoPrograma) {
        LicencaDados.tamanhoPrograma = tamanhoPrograma;
    }

    public static String stringTo() {
        return "{\n" +
                "identificacaoCivil:'"+identificacaoCivil+"',\n" +
                "primeiroNome:'"+primeiroNome+"',\n" +
                "ultimoNome:'"+ultimoNome+"',\n" +
                "email:'"+email+"',\n" +
                //"dadosMaquina:"+dadosMaquina.toString()+",\n" +
                "nomeDaApp:'"+nomeDaApp+"',\n" +
                "versao:'"+versao+"',\n" +
                "inicioValidadeLicenca:'"+inicioValidadeLicenca+"',\n" +
                "fimValidadeLicenca:'"+fimValidadeLicenca+"',\n" +
                "tamanhoPrograma:'"+tamanhoPrograma+"',\n" +
                "chavePublica:'"+chavePublica.toString()+"'\n" +
                "}";
    }
}
