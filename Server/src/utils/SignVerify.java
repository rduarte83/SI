package utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignVerify {

    /**
     * Assina um texto em claro com uma determinada chave privada
     * @param plainText texto em claro a ser assinado
     * @param privateKey chave privada responsável pela assinatura
     * @return chave privada codificada em Base64
     * @see PrivateKey
     */
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
}
