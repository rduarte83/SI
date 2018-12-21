package license;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


class LicencaDadosJson{

    private String primeiroNome;
    private String ultimoNome;
    private String email;
    private String identificacaoCivil;
    @JsonDeserialize
    @JsonSerialize
    private DadosMaquina dadosMaquina;
    private String nomeDaApp;
    private String versao;
    private long tamanhoPrograma;
    private String inicioValidadeLicenca;
    private String fimValidadeLicenca;
    private String chavePublica;

    public String getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(String chavePublica) {
        this.chavePublica = chavePublica;
    }

    public DadosMaquina getDadosMaquina() {
        return dadosMaquina;
    }

    public void setDadosMaquina(DadosMaquina dadosMaquina) {
        this.dadosMaquina = dadosMaquina;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificacaoCivil() {
        return identificacaoCivil;
    }

    public void setIdentificacaoCivil(String identificacaoCivil) {
        this.identificacaoCivil = identificacaoCivil;
    }

    public String getNomeDaApp() {
        return nomeDaApp;
    }

    public void setNomeDaApp(String nomeDaApp) {
        this.nomeDaApp = nomeDaApp;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public long getTamanhoPrograma() {
        return tamanhoPrograma;
    }

    public void setTamanhoPrograma(long tamanhoPrograma) {
        this.tamanhoPrograma = tamanhoPrograma;
    }

    public String getInicioValidadeLicenca() {
        return inicioValidadeLicenca;
    }

    public void setInicioValidadeLicenca(String inicioValidadeLicenca) {
        this.inicioValidadeLicenca = inicioValidadeLicenca;
    }

    public String getFimValidadeLicenca() {
        return fimValidadeLicenca;
    }

    public void setFimValidadeLicenca(String fimValidadeLicenca) {
        this.fimValidadeLicenca = fimValidadeLicenca;
    }

    public void setDadosClass(){
        this.setPrimeiroNome(LicencaDados.getPrimeiroNome());
        this.setUltimoNome(LicencaDados.getUltimoNome());
        this.setEmail(LicencaDados.getEmail());
        this.setIdentificacaoCivil(LicencaDados.getIdentificacaoCivil());
        this.setDadosMaquina(LicencaDados.getDadosMaquina());
        this.setNomeDaApp(LicencaDados.getNomeDaApp());
        this.setVersao(LicencaDados.getVersao());
        this.setTamanhoPrograma(LicencaDados.getTamanhoPrograma());
        this.setInicioValidadeLicenca(LicencaDados.getInicioValidadeLicenca());
        this.setFimValidadeLicenca(LicencaDados.getFimValidadeLicenca());
        this.setChavePublica(LicencaDados.getChavePublica());
    }

    public void getDadosClass(){
        LicencaDados.setPrimeiroNome(this.getPrimeiroNome());
        LicencaDados.setUltimoNome(this.getUltimoNome());
        LicencaDados.setEmail(this.getEmail());
        LicencaDados.setIdentificacaoCivil(this.getIdentificacaoCivil());
        LicencaDados.setDadosMaquina(this.getDadosMaquina());
        LicencaDados.setNomeDaApp(this.getNomeDaApp());
        LicencaDados.setVersao(this.getVersao());
        LicencaDados.setTamanhoPrograma(this.getTamanhoPrograma());
        LicencaDados.setInicioValidadeLicenca(this.getInicioValidadeLicenca());
        LicencaDados.setFimValidadeLicenca(this.getFimValidadeLicenca());
        LicencaDados.setChavePublica(this.getChavePublica());
    }
}

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
