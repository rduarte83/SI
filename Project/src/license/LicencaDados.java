package license;

class LicencaDados {

    private static String chave;
    private static String primeiroNome;
    private static String ultimoNome;
    private static String email;
    private static String chavePublica;
    private static String identificacaoCivil;
    private static DadosMaquina dadosMaquina;
    private static String nomeDaApp;
    private static String versao;
    private static String inicioValidadeLicenca;
    private static String fimValidadeLicenca;
    private static long tamanhoPrograma;

    public static String getChave() {
        return chave;
    }

    public static void setChave(String chave) {
        LicencaDados.chave = chave;
    }

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

    public static String getChavePublica() {
        return chavePublica;
    }

    public static void setChavePublica(String chavePublica) {
        LicencaDados.chavePublica = chavePublica;
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

    /**

     * @return
     */

    public static String stringTo() {
        return "{" +
                "chave:'"+chave+"'," +
                "primeiroNome:'"+primeiroNome+"'," +
                "ultimoNome:'"+ultimoNome+"'," +
                "email:'"+email+"'," +
                "chavePublica:'"+chavePublica+"'," +
                "identificacaoCivil:'"+identificacaoCivil+"'," +
                "dadosMaquina:"+dadosMaquina.toString()+"," +
                "nomeDaApp:'"+nomeDaApp+"'," +
                "versao:'"+versao+"'," +
                "inicioValidadeLicenca:'"+inicioValidadeLicenca+"'," +
                "fimValidadeLicenca:'"+fimValidadeLicenca+"'," +
                "tamanhoPrograma:'"+tamanhoPrograma+"'" +
                "}";
    }
}
