import org.jutils.jhardware.HardwareInfo;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HwInfo {

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