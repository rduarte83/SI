package license;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Assinatura{

    /**
     * Obtém a chave public e privado do Keypair gerado pela função createAsymKeys()
     * <p>Grava as chaves para ficheiros
     */
    public static void generateNewAsymKeys() {
        try {
            KeyPair pair = createAsymKeys();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            savePubKeyToFile(publicKey);
            savePriKeytoFile(privateKey);
            LicencaDados.setChavePublica(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um par de chaves com um 1024bytes de tamanho 1024 com base no algoritmo RSA
     * @return Par de chaves
     * @throws NoSuchAlgorithmException
     * @see KeyPair
     */
    public static KeyPair createAsymKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair pair = kpg.generateKeyPair();
        return pair;
    }

    /**
     * Grava a chave pública para o ficheiro "publicKeySign"
     * @param publicKey chave pública
     * @see PublicKey
     */
    public static void savePubKeyToFile(PublicKey publicKey){
        try {
            byte[] key = publicKey.getEncoded();
            FileOutputStream fos = new FileOutputStream("publicKeySign");
            fos.write(key);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Grava a chave privada para o ficheiro "privateKeySign"
     * @param privateKey chave privada
     * @see PrivateKey
     */
    public static void savePriKeytoFile(PrivateKey privateKey){
        try {
            byte[] key = privateKey.getEncoded();
            FileOutputStream fos = new FileOutputStream("privateKeySign");
            fos.write(key);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê a chave privada dum ficheiro
     * @param filename nome do ficheiro
     * @return chave privada contida no ficheiro
     * @see PrivateKey
     */
    public static PrivateKey loadPriKeyFromFile(String filename) {
        try {
            if(filename.length()<1) filename = "privateKeySign";
            byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica a assinatura
     * @param plainText texto em claro
     * @param signature assinatura
     * @param publicKey chave pública
     * @return True se assinatura válida, False caso contrário
     */
    public static boolean verifica(String plainText, String signature, PublicKey publicKey) {
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
