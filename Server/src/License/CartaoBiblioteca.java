package License;

import pt.gov.cartaodecidadao.*;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class CartaoBiblioteca {

    static {
        try {
            System.loadLibrary("pteidlibj");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. \n" + e);
            System.exit(1);
        }
    }

    final static String alias = "CITIZEN AUTHENTICATION CERTIFICATE";

    public static String assinar(String stringB64) {
        try {
            String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
            Provider[] provs = Security.getProviders();
            Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");

            KeyStore ks = KeyStore.getInstance("PKCS11", prov);
            ks.load(null, null);

            byte[] byteTextoEncriptado = stringB64.getBytes(StandardCharsets.UTF_8);

            PrivateKey pk = (PrivateKey) ks.getKey(alias, null);

            Signature sg = Signature.getInstance("SHA256withRSA", prov);
            sg.initSign(pk);

            sg.update(byteTextoEncriptado); // Quando assina com private Key...
            byte[] buffSign = sg.sign();
            String textoSignB64 = Base64.getEncoder().encodeToString(buffSign);

            Certificate ct = ks.getCertificate(alias);
            byte[] buffCert = ct.getEncoded();
            String textoCertB64 = Base64.getEncoder().encodeToString(buffCert);

            // Construir string para encriptar
            String resposta =  "/*TEXTOCLARO*/";
            resposta+= stringB64;
            resposta+= "/*ENDTEXTOCLARO*/";
            resposta+= "/ASS/";
            resposta+= textoSignB64;
            resposta+= "/*ENDASS*/";
            resposta+= "/*CERT*/";
            resposta+= textoCertB64;
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

    public static boolean validarAssinatura(String textoEmClaro, byte[] assinatura, String certificado) {
        try {
            Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;


            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] byteCert = certificado.getBytes(StandardCharsets.UTF_8);

            X509Certificate xCert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(byteCert));

            //Verifica a validade do certificado
            Date d = new Date();
            if (d.compareTo(xCert.getNotBefore())!= 1 && d.compareTo(xCert.getNotAfter())!= -1) { return false; }

            //Verifica raiz de confiança
//            subjectToCaCerts = new LinkedHashMap<>();
//            X500Principal issuer = xCert.getIssuerX500Principal();
//            Set<X509Certificate> subjectCaCerts = subjectToCaCerts.get(issuer);
//
//            if ( subjectCaCerts != null )
//                for (X509Certificate caCert : subjectCaCerts) {
//                    PublicKey publicKey = caCert.getPublicKey();
//                    try {
//                        xCert.verify(publicKey);
//                    } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | CertificateException e) {
//                        e.printStackTrace();
//                    }
//                }

            PublicKey pbk = xCert.getPublicKey();
            //xCert.verify(pbk);

            byte[] buffSign = assinatura;
            byte[] byteTClaro = textoEmClaro.getBytes(StandardCharsets.UTF_8);

            //Verifica assinatura
            Signature sg = Signature.getInstance("SHA256withRSA");
            sg.initVerify(pbk);
            sg.update(byteTClaro);

            boolean verifica = sg.verify(buffSign);
            return verifica;

        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }


//    public static PteidRSAPublicKey GetCVCRoot(){
//        PteidRSAPublicKey key = null;
//
//        if (readerContext != null) {
//            try {
//                PTEID_PublicKey rootCAKey = idCard.getRootCAPubKey();
//                key = new PteidRSAPublicKey();
//                key.exponent = new byte[(int) rootCAKey.getCardAuthKeyExponent().Size()];
//                key.modulus = new byte[(int) rootCAKey.getCardAuthKeyModulus().Size()];
//                Array.Copy(rootCAKey.getCardAuthKeyExponent().GetBytes(), 0, key.exponent, 0, key.exponent.Length);
//                Array.Copy(rootCAKey.getCardAuthKeyModulus().GetBytes(), 0, key.modulus, 0, key.modulus.Length);
//            }
//            catch (PTEID_Exception ex)
//            {
//                throw new PteidException(ex.GetError());
//            }
//        }
//
//        return key;
//    }
}