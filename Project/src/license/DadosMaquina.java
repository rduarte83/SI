package license;

class DadosMaquina {
    private String BIOS;
    private String CPU;
    private String MAC;

    // Construtor
    DadosMaquina(String BIOS, String CPU, String MAC){
        this.BIOS = BIOS;
        this.CPU = CPU;
        this.MAC = MAC;
    }

    String getBIOS() {
        return BIOS;
    }

    String getCPU() {
        return CPU;
    }

    String getMAC() {
        return MAC;
    }

    @Override
    public String toString() {
        return "{" +
                "BIOS: '"+BIOS+"'," +
                "CPU: '"+CPU+"'," +
                "MAC: '"+MAC+"'" +
                "}";
    }
}
