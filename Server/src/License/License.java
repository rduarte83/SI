package License;

import utils.Assimetrico;
import utils.Crypto;
import utils.Utils;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class License {
    public final static String _DIR_DAT = "Dat/";
    static final String PUBLIC_KEY =  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCfeP59DDplf+EzYWYeXjLscRqXQQqnpyxG2220CR54WbK5yVVOBRH0tqxJyRWUfx1hOaLwowPm/BTnWOF9PQq7nL804a9ENxrrybm2j4q7PUT7QGUKmwmGVtmIFt1ioKN6QYglgIq00NF8h/spdTvW76taDShsNUw8XR/0dgj5hUc/1SxvlXhFedMmBg9+b+iZpDH0ZKflxXbNm94TOjjw168rgVO72fPFkDvklFp4cL8ijSK6rvMEcltDZ+4F8GBFyEUbXXwQ4fNN0S2aI0dSqrJTJTWaSR4vw5zGS20XvVuFbapDfJYte28mkGxDsHDwHaKFPaFy0pPcN6LN6+QIDAQAB";
    static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwJ94/n0MOmV/4TNhZh5eMuxxGpdBCqenLEbbbbQJHnhZsrnJVU4FEfS2rEnJFZR/HWE5ovCjA+b8FOdY4X09CrucvzThr0Q3GuvJubaPirs9RPtAZQqbCYZW2YgW3WKgo3pBiCWAirTQ0XyH+yl1O9bvq1oNKGw1TDxdH/R2CPmFRz/VLG+VeEV50yYGD35v6JmkMfRkp+XFds2b3hM6OPDXryuBU7vZ88WQO+SUWnhwvyKNIrqu8wRyW0Nn7gXwYEXIRRtdfBDh803RLZojR1KqslMlNZpJHi/DnMZLbRe9W4VtqkN8li17byaQbEOwcPAdooU9oXLSk9w3os3r5AgMBAAECggEASWtke1nyxfvw/vlwVuhnptU5tMxZX9+XvPaWIyBtCdJ/AC85Ig0a3KPby+h1Ti0WsKxCie6agcvV7OStP3OiAmYJn2fGc1F/j2vNrW7vFoTLjc5DR7P68xtfHdP/E+rUs6wHu4Sy1+Ee2BEqhxprB4TuHLPpppJd3Fd22Z9KlESgoG5GMdsBoRecp0msoFX7URpUc8Q60auSpPbd66aXXCE1t/xvjKOO8dOAStcLPfshAYisvBEXr3CmqT4CbwzqPmpqSIznyXYCJakS/lf2WC7OES8o4rBkNvKiwTwjJ7Gr1Kox8CXQvMe2eUA+ncUwKMYaiITF06u49/dRQ5CXAQKBgQDUlXbXgr+jH1D/A/II/nzprvgVr/Xhn2qFZCTxSZi1jSRVaXA0KnHyTF+XnwoXmiPMDC46e3KyQ3WhAE469Kg9f7XDGsppk0C7ij64zonL8aU2RTmfP0mv5JAOV5BZ5ai0E9ib90GYlco+Goza9yJ0iCxXscj7sWE/t+2WMtFoGQKBgQDUIdSgO3UvImhohfkIgU/0l2YE4NQLVXO68bKlh1d7ygucOzfcACwQUEymWuPjwuC4+Dj2+FkFZkj18TUgQjVesQ6SgmQ+4CZi9jS30b8DJYwTxIgj+VnXdJ+qeri2/PkS08lgjeaXBAopo9SPM59PiBTKlA2CuwtrvoBqqmKF4QKBgAf8bXB/Ku/X11UdMtR/qvWkaxF1gMkvEfNc5b5iw2fem4TR5zMufQVbNSQfB2QHmFysAHiV9qMXwa2As2+njUJyL8Pal8wLih0BfoW2zJpqw4gcZaPD3uLKtVa0l2mpJQNO045YZZBz4BshKDV5n0O9cd0BgslhyoN4R2ajFhbRAoGAWSRuSXcy4z++SE4kPGK6yrnkcSBZevnJzEFNJSoepTJedqSb8KNR4PkdfLqtEUUPUitdJMtv/UjiWBeoj4nDC6uzx/VrUtC20NdNiAFoF1Zr6tKnsxZRnqyve+BeRuc/c53z/dMjl0pnSuBBrnuu8qjos8hLdShMwFYpeKlpBYECgYEArxjX0ZhVdVDQWetTSZEPgs2zgzrQb3lCnhG+8ui01raD+3e2hX0uP0nfvLqo7t4vbls/EsQYhj9tFL47vFv7MPoY/UGg461l6rAJcjRab0HZczW1ZSyNPm49YJi39Vu+qx38oOhdXMmSYwW/EKweEZho5+/OvDLUb67fDizTWYM=";


    // Decifra
    public static void decifrar(String filePath) {
        try {
            // Recebe .dat
            byte[] ficheiroDatBytes = Utils.getFileInBytes(new File(_DIR_DAT+filePath+".dat"));
            // Converter para String
            String dadosDat = new String(ficheiroDatBytes, StandardCharsets.UTF_8);
            //desEncriptar64
            String dadosDatDecoded = new String(Base64.getDecoder().decode(dadosDat), StandardCharsets.UTF_8);
            //chave = ( ultimaLinha de em Asymetrico )
            String [] linhas = Utils.extrairLinhas(dadosDatDecoded);
            //texto em claro = criptograma + chave
            byte[] criptograma = Base64.getDecoder().decode(linhas[0]);
            byte[] chaveEnc = Base64.getDecoder().decode(linhas[1]);

            System.out.println("criptograma:"+criptograma);
            System.out.println("chaveEnc:"+chaveEnc);

            //System.out.println("ChaveDecode:"+ new String( Utils.desencriptarB64(chaveEnc), StandardCharsets.UTF_8));
            String texto = Crypto.desencriptar(Crypto.loadPrivateKey(PRIVATE_KEY), criptograma, chaveEnc);
            String textoDecoded = new String ( Base64.getDecoder().decode(texto), StandardCharsets.UTF_8) ;
            System.out.println("json:"+texto);

            String jsonEncoded = textoDecoded.substring( textoDecoded.indexOf("/*TEXTOCLARO*/")+"/*TEXTOCLARO*/".length(), textoDecoded.indexOf("/*ENDTEXTOCLARO*/") );
            String assEncoded = textoDecoded.substring( textoDecoded.indexOf("/*ASS*/")+"/*ASS*/".length(), textoDecoded.indexOf("/*ENDASS*/") );
            String certEncoded = textoDecoded.substring( textoDecoded.indexOf("/*CERT*/")+"/*CERT*/".length(), textoDecoded.indexOf("/*ENDCERT*/") );

            System.out.println("códigoFinal:"+new String ( Base64.getDecoder().decode(nT), StandardCharsets.UTF_8));

            File file = new File("Cliente.jar");
            long fileLength = file.length();
            System.out.println("tamanho:"+fileLength);

            //System.out.println(chave);

            //

            //desencriptar ASSYM
            //SYM
            //B64

            //criptograma tem de ser decode em 64

            //String criptogramaDecoded = Utils.desencriptarB64();

            //textoClaro = /*TEXTOCLARO*/ - /*ENDTEXTOCLARO*/
            //Chave publica cc = /*ASS*/ - /*ENDASS*/
            //Certificado cc = /*CERT*/ - /*ENDCERT*/
            //validar(textoClaro, Chave publica cc, Certificado cc )

//            // Parse to Json the LicençaDados class.
//            ObjectMapper mapper = new ObjectMapper();
//            LicencaDadosJson ldj = mapper.reader(texto,LicencaDadosJson.class);
//            ldj.getDadosClass();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarChavesAsimetricas(){
        Assimetrico ac;
        try {
            ac = new Assimetrico(1024);
            ac.createKeys();
            ac.writeToFile("KeyPair/publicKey", ac.getPublicKey().getEncoded());
            ac.writeToFile("KeyPair/privateKey", ac.getPrivateKey().getEncoded());
            String str = new String(ac.getPublicKey().getEncoded(), StandardCharsets.UTF_8);
            System.out.println(str);
            //System.out.println("PublicKey:"+ac.getPublicKey().getEncoded());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
