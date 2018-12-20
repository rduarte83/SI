import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Cifras c = new Cifras();
        String symKey = c.createSymKey();
        String plainText = "O João é muito lindo!";

        System.out.println("SymKey: "+symKey);
        System.out.println("TClaro: "+ plainText);

        Base64.Decoder dec = Base64.getDecoder();
        byte[] encodedKey = dec.decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        byte [] symText = c.encryptAES(symKeyDec , plainText);
        System.out.println("TCifrado: "+symText);

        c.createAsymKeys();

        PublicKey publicKey = c.getPublicKey();
        System.out.println("pubkey:" +publicKey);
        byte[] encryptedKey = c.encryptRSA(symKeyDec, publicKey);
        System.out.println("encryptedKey: "+encryptedKey);

        PrivateKey privateKey = c.getPrivateKey();
        System.out.println("privKey: "+privateKey);
        String decodedText = c.decrypt (privateKey, symText, encryptedKey);
        System.out.println("decodedText:" +decodedText);

    }
}
