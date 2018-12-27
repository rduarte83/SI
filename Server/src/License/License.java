package License;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Assimetrico;
import utils.SignVerify;
import utils.Utils;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static License.Crypto.desencriptarLicencaDados;

public class License {
    // Cores
    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001b[032m";
    public static final String ANSI_YELLOW = "\u001b[033m";

    public final static String _DIR_DAT = "Dat/";
    static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCfeP59DDplf+EzYWYeXjLscRqXQQqnpyxG2220CR54WbK5yVVOBRH0tqxJyRWUfx1hOaLwowPm/BTnWOF9PQq7nL804a9ENxrrybm2j4q7PUT7QGUKmwmGVtmIFt1ioKN6QYglgIq00NF8h/spdTvW76taDShsNUw8XR/0dgj5hUc/1SxvlXhFedMmBg9+b+iZpDH0ZKflxXbNm94TOjjw168rgVO72fPFkDvklFp4cL8ijSK6rvMEcltDZ+4F8GBFyEUbXXwQ4fNN0S2aI0dSqrJTJTWaSR4vw5zGS20XvVuFbapDfJYte28mkGxDsHDwHaKFPaFy0pPcN6LN6+QIDAQAB";
    static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwJ94/n0MOmV/4TNhZh5eMuxxGpdBCqenLEbbbbQJHnhZsrnJVU4FEfS2rEnJFZR/HWE5ovCjA+b8FOdY4X09CrucvzThr0Q3GuvJubaPirs9RPtAZQqbCYZW2YgW3WKgo3pBiCWAirTQ0XyH+yl1O9bvq1oNKGw1TDxdH/R2CPmFRz/VLG+VeEV50yYGD35v6JmkMfRkp+XFds2b3hM6OPDXryuBU7vZ88WQO+SUWnhwvyKNIrqu8wRyW0Nn7gXwYEXIRRtdfBDh803RLZojR1KqslMlNZpJHi/DnMZLbRe9W4VtqkN8li17byaQbEOwcPAdooU9oXLSk9w3os3r5AgMBAAECggEASWtke1nyxfvw/vlwVuhnptU5tMxZX9+XvPaWIyBtCdJ/AC85Ig0a3KPby+h1Ti0WsKxCie6agcvV7OStP3OiAmYJn2fGc1F/j2vNrW7vFoTLjc5DR7P68xtfHdP/E+rUs6wHu4Sy1+Ee2BEqhxprB4TuHLPpppJd3Fd22Z9KlESgoG5GMdsBoRecp0msoFX7URpUc8Q60auSpPbd66aXXCE1t/xvjKOO8dOAStcLPfshAYisvBEXr3CmqT4CbwzqPmpqSIznyXYCJakS/lf2WC7OES8o4rBkNvKiwTwjJ7Gr1Kox8CXQvMe2eUA+ncUwKMYaiITF06u49/dRQ5CXAQKBgQDUlXbXgr+jH1D/A/II/nzprvgVr/Xhn2qFZCTxSZi1jSRVaXA0KnHyTF+XnwoXmiPMDC46e3KyQ3WhAE469Kg9f7XDGsppk0C7ij64zonL8aU2RTmfP0mv5JAOV5BZ5ai0E9ib90GYlco+Goza9yJ0iCxXscj7sWE/t+2WMtFoGQKBgQDUIdSgO3UvImhohfkIgU/0l2YE4NQLVXO68bKlh1d7ygucOzfcACwQUEymWuPjwuC4+Dj2+FkFZkj18TUgQjVesQ6SgmQ+4CZi9jS30b8DJYwTxIgj+VnXdJ+qeri2/PkS08lgjeaXBAopo9SPM59PiBTKlA2CuwtrvoBqqmKF4QKBgAf8bXB/Ku/X11UdMtR/qvWkaxF1gMkvEfNc5b5iw2fem4TR5zMufQVbNSQfB2QHmFysAHiV9qMXwa2As2+njUJyL8Pal8wLih0BfoW2zJpqw4gcZaPD3uLKtVa0l2mpJQNO045YZZBz4BshKDV5n0O9cd0BgslhyoN4R2ajFhbRAoGAWSRuSXcy4z++SE4kPGK6yrnkcSBZevnJzEFNJSoepTJedqSb8KNR4PkdfLqtEUUPUitdJMtv/UjiWBeoj4nDC6uzx/VrUtC20NdNiAFoF1Zr6tKnsxZRnqyve+BeRuc/c53z/dMjl0pnSuBBrnuu8qjos8hLdShMwFYpeKlpBYECgYEArxjX0ZhVdVDQWetTSZEPgs2zgzrQb3lCnhG+8ui01raD+3e2hX0uP0nfvLqo7t4vbls/EsQYhj9tFL47vFv7MPoY/UGg461l6rAJcjRab0HZczW1ZSyNPm49YJi39Vu+qx38oOhdXMmSYwW/EKweEZho5+/OvDLUb67fDizTWYM=";


    // Decifra
    public static void decifrar(String filePath) {
        try {
            System.out.print(ANSI_YELLOW + "A descodificar ficheiro..." + ANSI_RESET);
            // Recebe .dat
            new File(_DIR_DAT).mkdirs();
            byte[] ficheiroDatBytes = Utils.getFileInBytes(new File(_DIR_DAT + filePath + ".dat"));
            // Converter para String
            String dadosDat = new String(ficheiroDatBytes, StandardCharsets.UTF_8);
            //desEncriptar64
            String dadosDatDecoded = new String(Base64.getDecoder().decode(dadosDat), StandardCharsets.UTF_8);
            //chave = ( ultimaLinha de em Asymetrico )
            String[] linhas = Utils.extrairLinhas(dadosDatDecoded);
            //texto em claro = criptograma + chave
            byte[] criptograma = Base64.getDecoder().decode(linhas[0]);
            byte[] chaveEnc = Base64.getDecoder().decode(linhas[1]);

            //System.out.println("ChaveDecode:"+ new String( Utils.desencriptarB64(chaveEnc), StandardCharsets.UTF_8));
            String texto = Crypto.desencriptar(Crypto.loadPrivateKey(PRIVATE_KEY), criptograma, chaveEnc);
            String textoDecoded = new String(Base64.getDecoder().decode(texto), StandardCharsets.UTF_8);
            System.out.println(ANSI_GREEN + "OK" + ANSI_RESET);
            // Encoded
            String jsonEncoded = textoDecoded.substring(textoDecoded.indexOf("/*TEXTOCLARO*/") + "/*TEXTOCLARO*/".length(), textoDecoded.indexOf("/*ENDTEXTOCLARO*/"));
            String assEncoded = textoDecoded.substring(textoDecoded.indexOf("/*ASS*/") + "/*ASS*/".length(), textoDecoded.indexOf("/*ENDASS*/"));
            String certEncoded = textoDecoded.substring(textoDecoded.indexOf("/*CERT*/") + "/*CERT*/".length(), textoDecoded.indexOf("/*ENDCERT*/"));
            // Decoded
            String jsonDecoded = new String(Base64.getDecoder().decode(jsonEncoded), StandardCharsets.UTF_8);
            byte[] assDecoded = Base64.getDecoder().decode(assEncoded);
            String certDecoded = certEncoded;

            // TODO: Verificar se Certificado Valido.
            // Verificar se Assinatura Valida.
            System.out.print(ANSI_YELLOW + "A validar assinatura..." + ANSI_RESET);
            boolean isValid = CartaoBiblioteca.validarAssinatura(jsonEncoded, assDecoded, certDecoded);
            if (!isValid) {
                System.out.println(ANSI_RED + "ERROR!" + ANSI_RESET);
                return;
            }
            System.out.println(ANSI_GREEN + "OK" + ANSI_RESET);


            // Inserir validade do ficheiro
            String finalLicense = insereValidade(jsonDecoded);

            // Comparar tamanho dos ficheiros.
            System.out.print(ANSI_YELLOW + "A tamanho do cliente..." + ANSI_RESET);
            File file = new File("Cliente.jar");
            long fileLength = file.length();

            if (fileLength != LicencaDados.getTamanhoPrograma()) {
                System.out.println(ANSI_RED + "ERROR!" + ANSI_RESET);
                return;
            }
            System.out.println(ANSI_GREEN + "OK" + ANSI_RESET);

            // Gerar .LIC
            gerarLic(finalLicense);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarChavesAsimetricas() {
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

    private static String insereValidade(String jsonDecoded) {
        // Parse to Json the LicençaDados class.
        ObjectMapper mapper = new ObjectMapper();
        try {

            LicencaDadosJson ldj = mapper.readValue(jsonDecoded, LicencaDadosJson.class);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String start = df.format(cal.getTime());
            cal.add(Calendar.YEAR, 1);
            String end = df.format(cal.getTime());
            ldj.setInicioValidadeLicenca(start);
            ldj.setFimValidadeLicenca(end);
            ldj.getDadosClass();

            return mapper.writeValueAsString(ldj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void gerarLic(String jsonDados) {
        System.out.print(ANSI_YELLOW + "A gerar Licença..." + ANSI_RESET);

        //B64 jsonDados
        String jsonDadosEncoded = "/*TEXTOCLARO*/" + Base64.getEncoder().encodeToString(jsonDados.getBytes(StandardCharsets.UTF_8)) + "/*ENDTEXTOCLARO*/";

        byte[] b = Crypto.encriptarTudo(jsonDadosEncoded, LicencaDados.getChavePublica());
        String dadosEncriptados = new String(b, StandardCharsets.UTF_8);
        // Encriptar
        String textoAssinatura = SignVerify.sign(dadosEncriptados, Crypto.loadPrivateKey(PRIVATE_KEY));
        String finalString = dadosEncriptados + "\n" + textoAssinatura + "\n" + PUBLIC_KEY;
        try {
            // Busca Ficheiro e adiciona valor

            new File("lic/").mkdirs();
            FileOutputStream fos = new FileOutputStream("lic/" + LicencaDados.getIdentificacaoCivil() + ".lic");
            fos.write(Base64.getEncoder().encode(finalString.getBytes(StandardCharsets.UTF_8)));
            fos.flush();
            fos.close();
            System.out.println(ANSI_GREEN + "OK" + ANSI_RESET);
            addToFile(jsonDados);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "ERROR!" + ANSI_RESET);
            e.printStackTrace();
        }
    }

    private static void addToFile(String json){

        try {
            FileOutputStream fos = new FileOutputStream("lic/all.lic",true);
            fos.write(Base64.getEncoder().encode(json.getBytes(StandardCharsets.UTF_8)));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void listarTodosLic() {
        new File("lic/").mkdirs();
        try {
            File file = new File("lic/all.lic");
            ArrayList<LicencaDadosJson> arrayLics = new ArrayList<LicencaDadosJson>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String linhaDecoded = new String ( Base64.getDecoder().decode(line), StandardCharsets.UTF_8);
                    ObjectMapper mapper = new ObjectMapper();
                    arrayLics.add(mapper.readValue(linhaDecoded, LicencaDadosJson.class));
                }
            }
            printDataTable(arrayLics);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static void printDataTable(ArrayList<LicencaDadosJson> arrLics) {
        String leftAlignFormat = "| %-15s | %-19s | %-8s | %-40s | %-16s |%n";

        System.out.format("+-----------------+---------------------+----------+------------------------------------------+------------------+%n");
        System.out.format("| Ficheiro        | Nome da App         | Versão   | Nome                                     | Validade         |%n");
        System.out.format("+-----------------+---------------------+----------+------------------------------------------+------------------+%n");
        for ( LicencaDadosJson atual : arrLics ) {
            System.out.format(leftAlignFormat,
                    atual.getIdentificacaoCivil()+".lic",
                    atual.getNomeDaApp(),
                    atual.getVersao(),
                    atual.getPrimeiroNome() + " " + atual.getUltimoNome()
                    , getValidade(atual));
        }
        System.out.format("+-----------------+---------------------+----------+------------------------------------------+------------------+%n");
    }

    private static String getValidade(LicencaDadosJson atual) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String start = format.format(cal.getTime());
            Date now = format.parse(start);
            Date fim = format.parse(atual.getFimValidadeLicenca());
            Long diferenca =  fim.getTime() - now.getTime();
            Long difFinal = (diferenca/86400000); // Calculo da divisão por / Milisegundos / segundos / minutos / horas / Dias

            return difFinal.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}