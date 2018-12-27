package license;

import org.jutils.jhardware.HardwareInfo;

import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {

    /**
     * Obtém informações sobre o processador instalado na máquina
     * @return informações sobre o processador instalado na máquina 
     */
    public static String getCPU() {
        String cpu = HardwareInfo.getProcessorInfo().getFullInfo().get("Name");
        cpu += " - " + HardwareInfo.getProcessorInfo().getFullInfo().get("ProcessorId");
        return cpu;
    }

    /**
     * Obtém informações sobre a motherboard instalada na máquina
     * @return informações sobre a motherboard instalada na máquina 
     */
    public static String getMB() {
        String mb = HardwareInfo.getMotherboardInfo().getFullInfo().get("Manufacturer");
        mb += " " + HardwareInfo.getMotherboardInfo().getFullInfo().get("Product");
        mb += " - " + HardwareInfo.getMotherboardInfo().getFullInfo().get("SerialNumber");
        return mb;
    }

    /**
     * Obtém informações sobre placas gráficas instaladas na máquina
     * @return informações sobre placas gráficas 
     */
    public static String getGC() {
        return HardwareInfo.getGraphicsCardInfo().getGraphicsCards().get(0).getName();
    }

    /**
     * Obtém os endereços MAC das placas de rede instaladas na máquina
     * @return MAC Address
     */
    public static String getNW() {
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb;
            byte[] mac;
            String res = "";
            String macs = "";

            while ( ni.hasMoreElements() ) {
                NetworkInterface act = ni.nextElement();
                mac = act.getHardwareAddress();
                if (mac == null) continue;
                if (isVMMac(mac)) continue;
                sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                res = macs += "'"+sb.toString()+"',";
            }
            res = res.substring(0, res.length() - 1);
            return res;
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Verifica se endereço MAC é virtual
     * @param mac MAC Address a analisar
     * @return True se for uma placa de rede virtual, False caso contrário
     */
    private static boolean isVMMac(byte[] mac) {
        if(null == mac) return false;
        byte invalidMacs[][] = {
                {0x00, 0x00, 0x00},				//VPN
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs){
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }

    /**
     * Obtém informações da máquina (Motherboard, CPU, Placa Gráfica e Enderecos MAC das placas de rede)
     * @return informações da máquina
     */
    public static DadosMaquina getSystemInfo(){
        return new DadosMaquina(Utils.getMB(), Utils.getCPU(), Utils.getNW(), Utils.getGC());
    }

    /** 
     * Lê ficheito em bytes
     * @param f ficheiro a ser lido
     * @return bytes do ficheiro lido
     * @throws IOException
     * @see File
     */
    public static byte[] getFileInBytes(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fbytes = new byte[(int) f.length()];
        fis.read(fbytes);
        fis.close();
        return fbytes;
    }

    /**
     * Trato o texto dividindo-o por linhas
     * @param texto texto a ser lido
     * @return linhas separadas
     * @throws IOException
     */
    public static String[] extrairLinhas(String texto) throws IOException {
        LineNumberReader reader = new LineNumberReader(new StringReader(texto));
        String currentLine = null;
        String[] linhas = new String[3];
        while ((currentLine = reader.readLine()) != null) {
            if (reader.getLineNumber() == 1) {
                linhas[0] = currentLine;
            }
            if (reader.getLineNumber() == 2) {
                linhas[1] = currentLine;
            }
            if (reader.getLineNumber() == 3){
                linhas[2] = currentLine;
            }
        }
        return linhas;
    }
}
