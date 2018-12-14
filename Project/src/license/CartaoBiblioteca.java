package license;

import pt.gov.cartaodecidadao.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


public class CartaoBiblioteca {

    static PTEID_ReaderContext context;

    public static String getCartaoInfo(){

        String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
        Provider[] provs = Security.getProviders();
        for (int i = 0; i < provs.length; i++) {
            System.out.println(i + " - Nome do provider: " + provs[i].getName());
        }
        Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
        try {
            KeyStore ks = KeyStore.getInstance("PKCS11", prov);

            ks.load(null, null);

            Enumeration<String> als = ks.aliases();
            while (als.hasMoreElements()) {
                System.out.println(als.nextElement());
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        try {
            System.loadLibrary("pteidlibj");
        } catch (UnsatisfiedLinkError t) {
            if (t.getMessage().contains("already loaded")) {
                System.out.println("Biblioteca do Cartão de Cidadão bloqueada.");
            } else {
                System.out.println("Middleware do Cartão de Cidadão não está instalado.");
            }
        }

        try {
            if(isCardPresent()){
                PTEID_EIDCard card = context.getEIDCard();
                PTEID_EId eid = card.getID();

                // Pôr dados na nossa classe.
                LicencaDados.setPrimeiroNome(eid.getGivenName());
                LicencaDados.setUltimoNome(eid.getSurname());
                LicencaDados.setIdentificacaoCivil(eid.getCivilianIdNumber());
                LicencaDados.setChavePublica(eid.getCardAuthKeyObj().toString());

                //System.out.println("Dados: " + LicencaDados.stringTo());
            }


        } catch (PTEID_Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    static boolean isCardPresent(){
        try {
            PTEID_EIDCard card;
            PTEID_ReaderSet readerSet;
            readerSet = PTEID_ReaderSet.instance();
            for (int i = 0; i < readerSet.readerCount(); i++) {
                context = readerSet.getReaderByNum(i);
                if (context.isCardPresent()) {
                    card = context.getEIDCard();
                    return true;
                }
            }

            return false;
        }catch (PTEID_Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void assinar(String ficheiroTextoClaro, String ficheiroAssinatura, String ficheiroCert) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException {
        String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
        Provider[] provs = Security.getProviders();
        for(int i = 0; i < provs.length; i++){
            System.out.println( i + " - Nome do provider: " + provs[i].getName() );
        }
        Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");

        KeyStore ks = KeyStore.getInstance("PKCS11", prov);
        ks.load(null, null);

        Enumeration<String> als = ks.aliases();
        while (als.hasMoreElements()){
            System.out.println( als.nextElement() );
        }

        FileInputStream tClaro = new FileInputStream(ficheiroTextoClaro);
        byte[] buffTClaro = new byte[(int)tClaro.available()];
        tClaro.read(buffTClaro);
        tClaro.close();

        PrivateKey pk = (PrivateKey) ks.getKey(alias, null);

        Signature sg = Signature.getInstance("SHA256withRSA", prov);
        sg.initSign(pk);

        sg.update(buffTClaro);
        byte[] buffSign = sg.sign();

        Certificate ct = ks.getCertificate(alias);
        byte[] buffCert = ct.getEncoded();

        FileOutputStream foSign = new FileOutputStream(ficheiroAssinatura);
        foSign.write(buffSign);
        foSign.flush();
        foSign.close();

        FileOutputStream foCert = new FileOutputStream(ficheiroCert);
        foCert.write(buffCert);
        foCert.flush();
        foCert.close();
    }

    public static void validarAssinatura (String fichTClaro, String fichAssinatura, String fichCert) throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException, javax.security.cert.CertificateException {
        InputStream is = new FileInputStream(fichCert);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        X509Certificate xCert = (X509Certificate)cf.generateCertificate(is);

        PublicKey pbk = xCert.getPublicKey();

        FileInputStream sign = new FileInputStream(fichAssinatura);
        byte[] buffSign = new byte[(int)sign.available()];
        sign.read(buffSign);

        FileInputStream tClaro = new FileInputStream(fichTClaro);
        byte[] buffTClaro = new byte[(int)tClaro.available()];
        tClaro.read(buffTClaro);

        Signature sg = Signature.getInstance("SHA256withRSA");
        sg.initVerify(pbk);
        sg.update(buffTClaro);

        boolean verifica = sg.verify(buffSign);
        if (verifica) {
            System.out.println("Assinatura confere");
        } else {
            System.out.println("ASSINATURA NÃO CONFERE!!!");
        }
    }
}