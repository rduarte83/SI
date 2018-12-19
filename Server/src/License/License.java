package License;

import utils.Utils;

import utils.Assimetrico;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

public class License {
    public final static String _DIR_DAT = "Dat/";

    // Decifra
    public static void decifrar(String filePath) {
        try {
            // Recebe .dat
            byte[] ficheiroDatBytes = Utils.getFileInBytes(new File(_DIR_DAT+filePath+".dat"));
            // Converter para String
            String dadosDat = new String(ficheiroDatBytes, StandardCharsets.UTF_8);
            //desEncriptar64
            String dadosDatDecoded = new String(Utils.desencriptarB64(dadosDat), StandardCharsets.UTF_8);
            //chave = ( ultimaLinha de em Asymetrico )
            String [] linhas = Utils.extrairLinhas(dadosDatDecoded);
            //texto em claro = criptograma + chave
            String criptograma = linhas[0];
            String chaveEnc = linhas[1];
            System.out.println(criptograma);
            System.out.println(chaveEnc);
            Assimetrico a = new Assimetrico(1024);

            PrivateKey pk = Utils.readFilePriv("privateKey");
            System.out.println(pk);
            String chave = a.decryptText(chaveEnc, pk);///ao contrario???? Chave+Criptograma???? wrong B64 encoding????
            System.out.println(chave);
            //desencriptar ASSYM
            //SYM
            //B64

            //criptograma tem de ser decode em 64

            //String criptogramaDecoded = utils.desencriptarB64();

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
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
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
