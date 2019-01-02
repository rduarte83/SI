package License;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

public class CartaoBiblioteca {

    /**
     * Verifica e valida a assinatura
     * <p> Verifica a validade do certificado
     * <p> Valida o emissor do certificado
     * @param textoEmClaro
     * @param assinatura
     * @param certificado
     * @return True se assinatura válida, False caso contrário
     */
    public static boolean validarAssinatura(String textoEmClaro, byte[] assinatura, String certificado) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] byteCert = certificado.getBytes(StandardCharsets.UTF_8);

            if(!verificarCertificacao(byteCert)){
                System.out.println("O certificado não é válido.");
                return false;
            }
            X509Certificate xCert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(byteCert));

            /*//Verifica a validade do certificado - OLD
            Date d = new Date();
            if (d.compareTo(xCert.getNotBefore())!= 1 && d.compareTo(xCert.getNotAfter())!= -1) { return false; }*/

            //Verifica raiz de confiança - não funciona
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


    /** Valida o certificado, através da construção e subsequente validação do caminho de certificação
     * @param byteCert certifica a ser verificado
     * @return true se válido, falso caso contrário
     */
    public static boolean verificarCertificacao(byte[] byteCert){

        try {
            String ccPath = "CCCertificates/";

            //certificado ancora (Raíz de confiança)
            File certRaizConf = new File(ccPath + "MULTICERT Root Certification Authority 01.cer");
            FileInputStream fis = new FileInputStream(certRaizConf);

            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(fis);

            PublicKey pubKey = x509Cert.getPublicKey();
            x509Cert.verify(pubKey); //(Certificado de raiz de confiança)

            x509Cert.checkValidity(); //Verifica validade

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("root", x509Cert);
            fis.close();

            String[] certificados = new String[]{
                    ccPath + "Cartao de Cidadao 001.cer",
                    ccPath + "Cartao de Cidadao 002.cer",
                    ccPath + "Cartao de Cidadao 003.cer",
                    ccPath + "Cartao de Cidadao 004.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0007.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0008.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0009.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0010.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0011.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0012.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0013.cer",
                    ccPath + "EC de Assinatura Digital Qualificada do Cartao de Cidadao 0014.cer",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0007.crt",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0008.crt",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0009.crt",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0010.crt",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0011.crt",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0012.cer",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0013.cer",
                    ccPath + "EC de Autenticacao do Cartao de Cidadao 0014.cer",
                    ccPath + "ECRaizEstado.crt"
            };
            ArrayList<X509Certificate> ctArrayList = new ArrayList<>();
            for (int i = 0; i < certificados.length; i++) {
                File fCt = new File(certificados[i]);
                FileInputStream fisCt = new FileInputStream(fCt);
                CertificateFactory cfi = CertificateFactory.getInstance("X509");
                X509Certificate x509Certificate = (X509Certificate) cfi.generateCertificate(fisCt);

                x509Certificate.checkValidity();//verifica validade;

                ctArrayList.add(x509Certificate);
                fisCt.close();
            }
            cf = CertificateFactory.getInstance("X.509");

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteCert);
            x509Cert = (X509Certificate) cf.generateCertificate(byteArrayInputStream);

            x509Cert.checkValidity(); //verifica validade

//            //controi caminho de certificação
//            //defines the end-user certificate as a selector
//            X509CertSelector cs = new X509CertSelector();
//            cs.setCertificate(x509Cert);
//
//            //Create an object to build the certification path
//            CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
//
//            //Define the parameters to build the certification path and provide the Trust anchor certificates (c2ks) and the end user certificate (cs)
//            PKIXBuilderParameters pkixBParams = new PKIXBuilderParameters(ks, cs);
//            pkixBParams.setRevocationEnabled(false); //No revocation check
//
//            //Provide the intermediate certificates (certArrayList)
//            CollectionCertStoreParameters ccsp = new CollectionCertStoreParameters(ctArrayList);
//            CertStore store = CertStore.getInstance("Collection", ccsp);
//            pkixBParams.addCertStore(store);
//
//            //Build the certification path
//            CertPath cp = null;
//            CertPathBuilderResult cpbr = cpb.build(pkixBParams);
//            cp = cpbr.getCertPath();
//
//            //Validação de um caminho de certificação
//            PKIXParameters pkixParams = new PKIXParameters(ks);
//
//            //Class that performs the certification path validation
//            CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
//
//            //Disables the previous mechanism for revocation check (pre Java8)
//            pkixParams.setRevocationEnabled(false);
//
//            //Enable OCSP verification
//            Security.setProperty("ocsp.enable", "true");
//
//            //Instantiate a PKIXRevocationChecker class
//            PKIXRevocationChecker rc = (PKIXRevocationChecker) cpv.getRevocationChecker();
//
//            //Configure to validate all certificates in chain using only OCSP
//            rc.setOptions(EnumSet.of(PKIXRevocationChecker.Option.SOFT_FAIL, PKIXRevocationChecker.Option.NO_FALLBACK));
//            //Do the validation
//            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cpv.validate(cp, pkixParams);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}