package license;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Crypto {
    private static final String PUBLIC_KEY =  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCfeP59DDplf+EzYWYeXjLscRqXQQqnpyxG2220CR54WbK5yVVOBRH0tqxJyRWUfx1hOaLwowPm/BTnWOF9PQq7nL804a9ENxrrybm2j4q7PUT7QGUKmwmGVtmIFt1ioKN6QYglgIq00NF8h/spdTvW76taDShsNUw8XR/0dgj5hUc/1SxvlXhFedMmBg9+b+iZpDH0ZKflxXbNm94TOjjw168rgVO72fPFkDvklFp4cL8ijSK6rvMEcltDZ+4F8GBFyEUbXXwQ4fNN0S2aI0dSqrJTJTWaSR4vw5zGS20XvVuFbapDfJYte28mkGxDsHDwHaKFPaFy0pPcN6LN6+QIDAQAB";

    public static String criptogramaEncoded;
    public static String assinaturaEncoded;
    public static String publicKeyEncoded;
    public static LicencaDadosJson ldjLic;

    /**
     * Gera uma chave simétrica
     * <p>Encripta o texto em claro simetricamente (AES) e converte para Base64
     * <p>Encripta a chave simétrica assimetricamente(RSA) e converte para Base64
     * <p>Concatena os 2 elementos e converte para bytes
     * @param plainText texto em claro
     * @return byte[] criptograma+chave encriptada
     */
    public static byte[] encriptarTudo(String plainText)
    {
        String resposta = "";
        //Create Keys
        String symKey = CryptoUtils.createSymKey();

        //Encoding
        byte[] encodedKey = Base64.getDecoder().decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        //Encrypt sym AES
        byte [] symText = CryptoUtils.encryptAES(symKeyDec , plainText);

        resposta += Base64.getEncoder().encodeToString(symText);

        resposta += "\n";

        //Encrypt Assym RSA
        byte[] encryptedKey = CryptoUtils.encryptRSA(symKeyDec, CryptoUtils.loadPublicKey(PUBLIC_KEY));

        resposta += Base64.getEncoder().encodeToString(encryptedKey);
        return Base64.getEncoder().encode(resposta.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Trata o ficheiro separando os componentes necessários para o método desencriptar()
     * @param filename nome do ficheiro da licença
     * @return True se desencriptado com sucesso, False caso contrário
     */
    // Decifra
    public static Boolean desencriptarLicenca ( String filename ) {
        try {
            // Recebe .lic
            byte[] ficheiroDatBytes = Utils.getFileInBytes(new File(filename+".lic"));

            // Converter para String e Tirar o Base 64
            String dadosDatDecoded = new String(Base64.getDecoder().decode(ficheiroDatBytes), StandardCharsets.UTF_8);

            //chave = ( ultimaLinha de em Asymetrico )
            String [] linhasB64 = Utils.extrairLinhas(dadosDatDecoded);

            // Criptograma + "\n" + Chave Publica
            criptogramaEncoded  = linhasB64[0];
            assinaturaEncoded = linhasB64[1];
            publicKeyEncoded = linhasB64[2];

            byte[] criptograma = Base64.getDecoder().decode(linhasB64[0]);
            byte[] assinatura = Base64.getDecoder().decode(linhasB64[1]);

            if ( !VerificaAssinatura() ) return false;

            // Texto codificado Sym + "\n" + Chave Asym
            String criptogramaDecoded = new String ( criptograma, StandardCharsets.UTF_8);
            String [] linhasSymEAsymB64 = Utils.extrairLinhas(criptogramaDecoded);
            byte[] textoClaro = Base64.getDecoder().decode(linhasSymEAsymB64[0]);
            byte[] chaveSym = Base64.getDecoder().decode(linhasSymEAsymB64[1]);
            PrivateKey privateKey = Assinatura.loadPriKeyFromFile(""); // Load Private Key From File.

            String texto = Crypto.desencriptar(privateKey, textoClaro, chaveSym);

            // Encoded
            String jsonEncoded = texto.substring( texto.indexOf("/*TEXTOCLARO*/")+"/*TEXTOCLARO*/".length(), texto.indexOf("/*ENDTEXTOCLARO*/") );

            // Decoded
            String jsonDecoded = new String(Base64.getDecoder().decode(jsonEncoded), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            LicencaDadosJson ldLic = mapper.readValue(jsonDecoded, LicencaDadosJson.class);
            ldjLic = ldLic;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Desencripta assimetricamente a chave simétrica
     * <p>Desencripta o criptograma usando a chave anterior
     * @param privateKey chave privada usada na desencriptação assimétrica
     * @param byteCipherText texto cifrado
     * @param encryptedKey chave simétrica cifrada assimetricamente
     * @return String - texto em claro
     * @see PrivateKey
     */
    public static String desencriptar(PrivateKey privateKey, byte[] byteCipherText, byte[] encryptedKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            byte[] decryptedKey = cipher.doFinal(encryptedKey);

            //Bytes to AES SecretKey
            SecretKey originalKey = new SecretKeySpec(decryptedKey ,0, decryptedKey.length, "AES");

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
            String plainText = new String(bytePlainText);

            return plainText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Valida a assinatura
     * @return True se assinatura válida, False caso contrário
     */
    public static boolean VerificaAssinatura () {
        return Assinatura.verifica(criptogramaEncoded, assinaturaEncoded, CryptoUtils.loadPublicKey(publicKeyEncoded));
    }
}

class CryptoUtils {

    /**
     * Gera uma chave simétrica de 128bits
     * @return
     */
    public static String createSymKey() {
        try {
            KeyGenerator generator = null;
            generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            SecretKey secKey = generator.generateKey();
            String stringKey = Base64.getEncoder().encodeToString(secKey.getEncoded());

            return stringKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converte uma chave pública de Base64 para PublicKey
     * @param stored chave pública em Base64
     * @return PublicKey chave pública
     * @see PublicKey
     */
    public static PublicKey loadPublicKey(String stored) {
        try {
            byte[] data = Base64.getDecoder().decode(stored);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = kf.generatePublic(spec);
            return pubKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encripta um texto com encriptação simétrica AES
     * @param secKey chave simétrica gerada
     * @param plainText texto em claro
     * @return byte[] - texto cifrado em bytes
     * @see SecretKey
     */
    public static byte[] encryptAES(SecretKey secKey, String plainText) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
            byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return byteCipherText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Encripta uma chave com encriptação assimétrica RSA
     * @param symKey chave a ser encriptada
     * @param publicKey chave pública usada para a encriptação
     * @return
     * @see SecretKey
     * @see PublicKey
     */
    public static byte[] encryptRSA(SecretKey symKey, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            byte[] encryptedKey = cipher.doFinal(symKey.getEncoded()/*Secret Key From Step 1*/);

            return encryptedKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
