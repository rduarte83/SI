package license;

import pt.gov.cartaodecidadao.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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
    final static String alias = "CITIZEN AUTHENTICATION CERTIFICATE";

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

    public static String getCartaoInfo(){


        try {
            Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
            KeyStore ks = KeyStore.getInstance("PKCS11", prov);
            ks.load(null, null);

            if(isCardPresent()){
                PTEID_EIDCard card = context.getEIDCard();
                PTEID_EId eid = card.getID();
                PTEID_PublicKey pk = eid.getCardAuthKeyObj();

                Certificate cert = ks.getCertificate(alias);
                PublicKey key = cert.getPublicKey();
                byte[] buffCert = cert.getEncoded();

                System.loadLibrary("pteidlibj");

                // Pôr dados na nossa classe.
                LicencaDados.setPrimeiroNome(eid.getGivenName());
                LicencaDados.setUltimoNome(eid.getSurname());
                LicencaDados.setIdentificacaoCivil(eid.getCivilianIdNumber());
                //LicencaDados.setChavePublica(Base64.getEncoder().encodeToString(key.getEncoded()));
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


        return "";
    }

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
            resposta+= "/*ASS*/";
            resposta+= textoSignB64;
            resposta+= "/*ENDASS*/";
            resposta+= "/*CERT*/";
            resposta+= "-----BEGIN CERTIFICATE-----\n"+textoCertB64+"-----END CERTIFICATE-----";
            resposta+= "/*ENDCERT*/";

            System.out.println("sign..."+textoSignB64);
            System.out.println("signString..."+new String ( buffSign, StandardCharsets.UTF_8));


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

    public static void validarAssinatura (String fichTClaro, String fichAssinatura, String fichCert) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

    }



   /* public static PTEID_RSAPublicKey GetCardAuthenticationKey(){
        PTEID_RSAPublicKey key = null;

        if (context != null) {
            try {
                PTEID_EIDCard card = context.getEIDCard();
                PTEID_PublicKey cardKey = card.getID().getCardAuthKeyObj();
                key = new PTEID_RSAPublicKey();
                key.exponent = new byte[(int) cardKey.getCardAuthKeyExponent().Size()];
                key.modulus = new byte[(int) cardKey.getCardAuthKeyModulus().Size()];
                // Create the Long variable.
                Long longVarMod = cardKey.getCardAuthKeyModulus().Size();
                Long longVarExp = cardKey.getCardAuthKeyExponent().Size();

                // Convert Long to String.
                String stringVarMod = longVarMod.toString();
                String stringVarExp = longVarExp.toString();

                System.out.println("stringVarMod:"+stringVarMod);
                System.out.println("stringVarExp:"+stringVarExp);
                System.out.println(":------------:");

                // Convert to BigInteger. The BigInteger(byte[] val) expects a binary representation of
                // the number, whereas the BigInteger(string val) expects a decimal representation.
                BigInteger bigIntVarMod = new BigInteger( stringVarMod );
                BigInteger bigIntVarExp = new BigInteger( stringVarExp );

                BigInteger bigIntMod = new BigInteger(stringVarMod, 16);
                BigInteger bigIntExp = new BigInteger(stringVarExp, 16);

                System.out.println("bigIntVarMod:"+bigIntVarMod);
                System.out.println("bigIntVarExp:"+bigIntVarExp);
                System.out.println(":------------:");
                System.out.println("bigIntMod:"+bigIntMod);
                System.out.println("bigIntExp:"+bigIntExp);

                // See if the conversion worked. But the output from this step is not
                // anything like the original value I put into longVar

                RSAPublicKeySpec spec = new RSAPublicKeySpec(bigIntMod, bigIntExp);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PublicKey pub = factory.generatePublic(spec);

                System.out.println("PK-"+pub.getEncoded());
                //verifier.update(url.getBytes("UTF-8")); // Or whatever interface specifies.


//                Array.Copy(cardKey.getCardAuthKeyExponent().GetBytes(), 0, key.exponent, 0, key.exponent.Length);
//                Array.Copy(cardKey.getCardAuthKeyModulus().GetBytes(), 0, key.modulus, 0, key.modulus.Length);
            }
            catch (PTEID_Exception ex)
            {
                try {
                    throw new PteidException(ex.GetError());
                } catch (PteidException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        return key;
    }*/


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