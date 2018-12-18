package License;

import Utils.Utils;

import Utils.Assimetrico;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class License {
    public final static String _DIR_DAT = "Dat/";

    public final static String PKS = "\u0001\u0001\u0001\u0005 \u0003�� 0��\u0002�� ��\u0003��\u0004���\u000B�Ȇ�2�0Ih\u0001B��?�I\u000F��u�C*ޏTy�\u0006��J\fɘ�?�Z\u0017�s�������\u001B��\u0003u�����%�bV�%��0D���\u00062���\t���\u0002�z��\u0002�%�QX��FP`�e+\u001Af� ~���]6,x>��\u0013��\u0002\u0003\u0001 \u0001";

    // Decifra
    public static void decifrar(String filePath) {
        // Recebe .dat
        try {
            byte[] ficheiroDatBytes = Utils.getFileInBytes(new File(_DIR_DAT+filePath+".dat"));
            // Converter para String
            String dadosDat = new String(ficheiroDatBytes, StandardCharsets.UTF_8);
            //deEncriptar64
            String dadosDatDecoded = new String(Utils.desencriptarB64(dadosDat), StandardCharsets.UTF_8);
            //chave = ( ultimaLinha de em Asymetrico )

            String [] linhas = Utils.extrairLinhas(dadosDatDecoded);
            //texto em claro = criptograma + chave
            String criptograma = linhas[0];
            String chave = linhas[1];


            //criptograma tem de ser decode em 64



            //String criptogramaDecoded = Utils.desencriptarB64();

            //textoClaro = /*TEXTOCLARO*/ - /*ENDTEXTOCLARO*/
            //Chave publica cc = /*ASS*/ - /*ENDASS*/
            //Certificado cc = /*CERT*/ - /*ENDCERT*/
            //validar(textoClaro, Chave publica cc, Certificado cc )



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
