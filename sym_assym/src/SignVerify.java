import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignVerify {

    public static KeyPair createAsymKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();
//        PrivateKey privateKey = pair.getPrivate();
//        PublicKey publicKey = pair.getPublic();
        return pair;
    }



    public static String sign(String plainText, PrivateKey privateKey) {
        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signature = privateSignature.sign();

            return Base64.getEncoder().encodeToString(signature);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) {
        Signature publicSignature = null;
        try {
            publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            return publicSignature.verify(signatureBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }


}
