package license;

import pt.gov.cartaodecidadao.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

public class CartaoBiblioteca {

    static {
        try {
            System.loadLibrary("pteidlibj");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. \n" + e);
            System.exit(1);
        }
    }
    static PTEID_ReaderContext context;

    /**
     * Verifica se existe algum leitor de CC válido
     * @return True se existir, False em caso contrário
     */
    public static boolean isCardReaderPresent() {
        PTEID_ReaderSet readerSet;
        try {
            readerSet = PTEID_ReaderSet.instance();
            for (int i = 0; i < readerSet.readerCount(); i++) {
                context = readerSet.getReaderByNum(i);
                return true;
            }
        } catch (PTEID_Exception e) {
            e.printStackTrace();
        } return false;
    }

    /**
     * Verifica se existe algum CC inserido no leitor
     * @return True se existir, False em caso contrário
     */
    public static boolean isCardPresent(){
        try {
            PTEID_EIDCard card;
            if (context.isCardPresent()) {
                card = context.getEIDCard();
                return true;
            }
        } catch (PTEID_Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Recolhe os dados do cartão de cidadão
     */
    public static void getCartaoInfo(){
        try {
            Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
            KeyStore ks = KeyStore.getInstance("PKCS11", prov);
            ks.load(null, null);

            if(isCardPresent()){
                PTEID_EIDCard card = context.getEIDCard();
                PTEID_EId eid = card.getID();

                // Pôr dados na nossa classe.
                LicencaDados.setPrimeiroNome(eid.getGivenName());
                LicencaDados.setUltimoNome(eid.getSurname());
                LicencaDados.setIdentificacaoCivil(eid.getCivilianIdNumber());
            }
        } catch (PTEID_Exception e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsatisfiedLinkError t) {
            if (t.getMessage().contains("already loaded")) {
                System.out.println("Biblioteca do Cartão de Cidadão bloqueada.");
            } else {
                System.out.println("Middleware do Cartão de Cidadão não está instalado.");
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assina um texto digitalmente com o cartão de cidadão
     * <p>Grava o texto em claro, a assinatura e o certificado num ficheiro
     * @param stringB64 Texto a assinar
     * @return texto em claro+assinatura+certificado em Base64
     */
    public static String assinar(String stringB64) {
        try {
            String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
            Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
            KeyStore ks = KeyStore.getInstance("PKCS11", prov);
            ks.load(null, null);

            byte[] byteTextoEncriptado = stringB64.getBytes(StandardCharsets.UTF_8);

            PrivateKey pk = (PrivateKey) ks.getKey(alias, null);

            Signature sg = Signature.getInstance("SHA256withRSA", prov);
            sg.initSign(pk);

            sg.update(byteTextoEncriptado); // Assina com private Key
            byte[] buffSign = sg.sign();
            String textoSignB64 = Base64.getEncoder().encodeToString(buffSign);

            Certificate ct = ks.getCertificate(alias);
            byte[] buffCert = ct.getEncoded();
            String textoCertB64 = Base64.getEncoder().encodeToString(buffCert);

            // Construir string para encriptar
            String resposta =  "/*TEXTOCLARO*/";
            resposta+= stringB64;
            resposta+= "/*ENDTEXTOCLARO*/";
            resposta+= "/*ASS*/";
            resposta+= textoSignB64;
            resposta+= "/*ENDASS*/";
            resposta+= "/*CERT*/";
            resposta+= "-----BEGIN CERTIFICATE-----\n"+textoCertB64+"-----END CERTIFICATE-----";
            resposta+= "/*ENDCERT*/";

            return Base64.getEncoder().encodeToString(resposta.getBytes(StandardCharsets.UTF_8));
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}