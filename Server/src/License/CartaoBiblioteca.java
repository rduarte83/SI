package License;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class CartaoBiblioteca {

    public static boolean validarAssinatura(String textoEmClaro, byte[] assinatura, String certificado) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] byteCert = certificado.getBytes(StandardCharsets.UTF_8);

            X509Certificate xCert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(byteCert));

            //Verifica a validade do certificado
            Date d = new Date();
            if (d.compareTo(xCert.getNotBefore())!= 1 && d.compareTo(xCert.getNotAfter())!= -1) { return false; }

            //Verifica raiz de confiança
//            Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;
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

            //Verifica a entidade emissora
            String emissor = xCert.getIssuerX500Principal().getName();
            if (!(emissor.contains("OU=subECEstado"))) {
                return false;
            }

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
}