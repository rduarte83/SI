/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cartaocidadao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;
import java.security.cert.X509Certificate;

/**
 *
 * @author Licinio
 */
public class CartaoCidadaoAux {
    
    public static void assinar(String fichTClaro, String fichAssinatura, String fichCert) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException {
        String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
        Provider[] provs = Security.getProviders();
        for(int i = 0; i < provs.length; i++){
            System.out.println( i + " - Nome do provider: " + provs[i].getName() );
        }
        Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
        
        KeyStore ks = KseyStore.getIntance("PKCS11", prov);
        ks.load(null, null);
        
        Enumeration<String> als = ks.aliases();
        while (als.hasMoreElements()){
            System.out.println( als.nextElement() );
        }
        
        FileInputStream tClaro = new FileInputStream(fichTClaro);
        byte[] buffTClaro = new byte[(int)tClaro.available()];
        tClaro.read(buffTClaro);
        
        PrivateKey pk = (PrivateKey) ks.getKey(alias, null);
        
        Signature sg = Signature.getInstance("SHA256withRSA", prov);
        sg.initSign(pk);
        
        sg.update(buffTClaro);
        byte[] buffSign = sg.sign();
        
        Certificate ct = ks.getCertificate(alias);
        byte[] buffCert = ct.getEncoded();
        
        FileOutputStream foSign = new FileOutputStream(fichAssinatura);
        foSign.write(buffSign);
        foSign.flush();
        foSign.close();
        
        FileOutputStream foCert = new FileOutputStream(fichCert);
        foCert.write(buffCert);
        foCert.flush();
        foCert.close();
    }
    
    public static void validarAssinatura (String fichTClaro, String fichAssinatura, String fichCert) throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException, javax.security.cert.CertificateException, InvalidKeySpecException {
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
