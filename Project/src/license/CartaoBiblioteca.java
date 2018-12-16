package license;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import pt.gov.cartaodecidadao.*;
import pteidlib.PTEID_RSAPublicKey;
import pteidlib.PteidException;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;


public class CartaoBiblioteca {

    static PTEID_ReaderContext context;


    public static String getCartaoInfo(){

        String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
        Provider[] provs = Security.getProviders();
        Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");
        try {
            KeyStore ks = KeyStore.getInstance("PKCS11", prov);

            ks.load(null, null);

            /*Enumeration<String> als = ks.aliases();
            while (als.hasMoreElements()) {
                System.out.println(als.nextElement());
            }*/
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
                PTEID_PublicKey pk = eid.getCardAuthKeyObj();

                //card.getRootCAPubKey();
                System.out.println("getCardAuthKeyExponent-"+pk.getCardAuthKeyExponent());
                System.out.println("getCardAuthKeyModulus-"+pk.getCardAuthKeyModulus());
                // Pôr dados na nossa classe.
                LicencaDados.setPrimeiroNome(eid.getGivenName());
                LicencaDados.setUltimoNome(eid.getSurname());
                LicencaDados.setIdentificacaoCivil(eid.getCivilianIdNumber());
                GetCardAuthenticationKey();
                //LicencaDados.setChavePublica();

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

    public static void assinar(String ficheiroTextoClaro, String pathFicheiroAssinado) {
        try {
            String alias = "CITIZEN AUTHENTICATION CERTIFICATE";
            Provider[] provs = Security.getProviders();
            Provider prov = Security.getProvider("SunPKCS11-CartaoCidadao");

            KeyStore ks = KeyStore.getInstance("PKCS11", prov);
            ks.load(null, null);

            /*Enumeration<String> als = ks.aliases();
            while (als.hasMoreElements()){
                System.out.println( als.nextElement() );
            }*/


            // ler Primeira vez
            FileInputStream tClaro = new FileInputStream(ficheiroTextoClaro);
            byte[] buffTClaro = new byte[(int)tClaro.available()];
            tClaro.read(buffTClaro);
            tClaro.close();

            // Base 64
            FileOutputStream FOSFileBase64 = new FileOutputStream(ficheiroTextoClaro);
            FOSFileBase64.write(Base64.encode(buffTClaro).getBytes());
            FOSFileBase64.flush();
            FOSFileBase64.close();

            // ler Segunda vez
            FileInputStream textoEncriptado = new FileInputStream(ficheiroTextoClaro);
            byte[] buffTextoEncriptado = new byte[(int)textoEncriptado.available()];
            textoEncriptado.read(buffTextoEncriptado);
            textoEncriptado.close();

            PrivateKey pk = (PrivateKey) ks.getKey(alias, null);

            Signature sg = Signature.getInstance("SHA256withRSA", prov);
            sg.initSign(pk);

            sg.update(buffTextoEncriptado); // Pegar no texto em claro e encriptar com a private key do cartão de cidadao
            byte[] buffSign = sg.sign();

            Certificate ct = ks.getCertificate(alias);
            byte[] buffCert = ct.getEncoded();

            // Escrever para ficheiro assinado com todos os dados.
            FileOutputStream FOSFileAssinado = new FileOutputStream(ficheiroTextoClaro);
            FOSFileAssinado.write("/*TEXTOCLARO*/".getBytes());
            FOSFileAssinado.write(buffTextoEncriptado);
            FOSFileAssinado.write("/*ENDTEXTOCLARO*/".getBytes());
            FOSFileAssinado.write("/*ASS*/".getBytes());
            FOSFileAssinado.write(buffSign);
            FOSFileAssinado.write("/*ENDASS*/".getBytes());
            FOSFileAssinado.write("/*CERT*/".getBytes());
            FOSFileAssinado.write(buffCert);
            FOSFileAssinado.write("/*ENDCERT*/".getBytes());
            FOSFileAssinado.flush();
            FOSFileAssinado.close();

            // ler Segunda vez
            FileInputStream textoEncriptadoUltimo = new FileInputStream(ficheiroTextoClaro);
            byte[] buffTextoEncriptadoUltimo = new byte[(int)textoEncriptadoUltimo.available()];
            textoEncriptadoUltimo.read(buffTextoEncriptadoUltimo);
            textoEncriptadoUltimo.close();

            // Base 64
            FileOutputStream FOSFileBase64Ultimo = new FileOutputStream(ficheiroTextoClaro);
            FOSFileBase64Ultimo.write(Utils.encriptarB64(new String( buffTextoEncriptadoUltimo, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
            FOSFileBase64Ultimo.flush();
            FOSFileBase64Ultimo.close();




//            // Escrever para o ficheiro destino assinado.
//            FileOutputStream foSign = new FileOutputStream(ficheiroAssinatura);
//            foSign.write(buffSign);
//            foSign.flush();
//            foSign.close();
//
//            // Escrever para o fihciero destino certificado.
//            FileOutputStream foCert = new FileOutputStream(ficheiroCert);
//            foCert.write(buffCert);
//            foCert.flush();
//            foCert.close();
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



    public static PTEID_RSAPublicKey GetCardAuthenticationKey(){
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
                String stringVarMod =longVarMod.toString();
                String stringVarExp =longVarExp.toString();

                // Convert to BigInteger. The BigInteger(byte[] val) expects a binary representation of
                // the number, whereas the BigInteger(string val) expects a decimal representation.
                BigInteger bigIntVarMod = new BigInteger( stringVarMod );
                BigInteger bigIntVarExp = new BigInteger( stringVarExp );

                // See if the conversion worked. But the output from this step is not
                // anything like the original value I put into longVar

                RSAPublicKeySpec spec = new RSAPublicKeySpec(bigIntVarMod, bigIntVarExp);
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