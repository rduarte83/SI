package utils;

import org.jutils.jhardware.HardwareInfo;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {

    public static String getCPU() {
        String cpu = HardwareInfo.getProcessorInfo().getFullInfo().get("Name");
        cpu += " - " + HardwareInfo.getProcessorInfo().getFullInfo().get("ProcessorId");
        return cpu;
    }

    public static String getMB() {
        String mb = HardwareInfo.getMotherboardInfo().getFullInfo().get("Manufacturer");
        mb += " " + HardwareInfo.getMotherboardInfo().getFullInfo().get("Product");
        mb += " - " + HardwareInfo.getMotherboardInfo().getFullInfo().get("SerialNumber");
        return mb;
    }

    public static String getGC() {
        return HardwareInfo.getGraphicsCardInfo().getGraphicsCards().get(0).getName();
    }

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

    //Remove as placas de rede virtuais
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


}
