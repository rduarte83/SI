package license;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class CartaoBiblioteca {
    public static String getCartaoInfo() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException {

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

        return "";
    }
}
