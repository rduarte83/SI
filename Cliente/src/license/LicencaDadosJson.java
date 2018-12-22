package license;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class LicencaDadosJson{

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
