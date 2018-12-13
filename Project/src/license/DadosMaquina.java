package license;

public class DadosMaquina {
    private String BIOS;
    private String CPU;
    private String MAC;

    // Construtor
    public DadosMaquina (String BIOS, String CPU, String MAC){
        this.BIOS = BIOS;
        this.CPU = CPU;
        this.MAC = MAC;
    }

    public String getBIOS() {
        return BIOS;
    }

    public String getCPU() {
        return CPU;
    }

    public String getMAC() {
        return MAC;
    }
}
