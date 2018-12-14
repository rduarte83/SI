package utils;

import org.jutils.jhardware.HardwareInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Utils {

    public static String getCpu() {
        return HardwareInfo.getProcessorInfo().getFullInfo().get("Name");
    }

    public static String getMb() {
        return HardwareInfo.getMotherboardInfo().getFullInfo().get("SerialNumber");
    }


    public static String getMac() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            while ( ni.hasMoreElements() ) {
                NetworkInterface atual = ni.nextElement();

            }

            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }

        return "";
    }


}
