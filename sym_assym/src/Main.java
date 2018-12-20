import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //Encoding

        String symKey = SymAssym.createSymKey();
        String plainText = "O João é muito lindo!";

        System.out.println("SymKey: "+symKey);
        System.out.println("TClaro: "+ plainText);

        Base64.Decoder dec = Base64.getDecoder();
        byte[] encodedKey = dec.decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        byte [] symText = SymAssym.encryptAES(symKeyDec , plainText);
        System.out.println("TCifrado: "+symText);

        SymAssym.createAsymKeys();
        KeyPair kp = SymAssym.createAsymKeys();

        PublicKey publicKey = kp.getPublic();
        System.out.println("pubkey:" +publicKey);
        byte[] encryptedKey = SymAssym.encryptRSA(symKeyDec, publicKey);
        System.out.println("encryptedKey: "+encryptedKey);


        //Decoding
        PrivateKey privateKey = kp.getPrivate();
        System.out.println("privKey: "+privateKey);
        String decodedText = SymAssym.decrypt (privateKey, symText, encryptedKey);
        System.out.println("decodedText:" +decodedText);

        //Signing
            KeyPair kpSign = SignVerify.createAsymKeys();
            PrivateKey privateKeySign = kpSign.getPrivate();
            PublicKey publicKeySign = kpSign.getPublic();

            String sig = SignVerify.sign(plainText, privateKeySign);
            System.out.println("Signature: "+sig);


        //Verifying
        Boolean isSigned = SignVerify.verify(plainText, sig, publicKeySign);
        if (isSigned) {
            System.out.println("Dados autênticos");
        } else {
            System.out.println("Dados NÃO autênticos");
        };




    }
}
