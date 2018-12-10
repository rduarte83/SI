package crypto224;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Dantiii
 */
public class Crypto224 {

    /**
     * => 2.2.4 Decifrar o conteúdo de um fcheiro utilizando o algoritmo DES
     * Parâmetros de entrada: 
     * (i) o nome do ficheiro a decifrar, 
     * (ii) o nome pretendido para o ficheiro depois de decifrado,
     * (iii) o nome do ficheiro com a chave a utilizar na operacao de decifra
     *
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
         Scanner scan = new Scanner(System.in);
        
        // Param de entrada
        // (i) nome do ficheiro a decifrar
        System.out.print("Nome do ficheiro a decifrar: ");
        String ficheiroDecifrar = scan.nextLine();
        
        // (i) nome para o ficheiro decifrado
        System.out.print("Nome do ficheiro decifrado: ");
        String ficheiroDecifrado = scan.nextLine();
        
        // (i) nome do ficheiro com a chave
        System.out.print("Nome do ficheiro com a chave: ");
        String chave = scan.nextLine();
        
        // Obter instancias dos files
        // ----------------------------------------------------------
        FileInputStream fi = new FileInputStream(ficheiroDecifrar);
        FileInputStream fiChave = new FileInputStream(chave);
        FileOutputStream fo = new FileOutputStream(ficheiroDecifrado);
        
        // Obter texto do arquivo a decifrar
        // ----------------------------------------------------------
        int tamanhoFicheiroDecifrar = fi.available();
        byte[] byteFicheiroDecifrar = new byte[tamanhoFicheiroDecifrar];
        fi.read(byteFicheiroDecifrar);
        
        // Obter chave
        // ----------------------------------------------------------
        int tamanhoChave = fiChave.available();
        byte[] byteChave = new byte[tamanhoChave];
        fiChave.read(byteChave);
        
        // Instancia do SecretKey
        SecretKey sk = new SecretKeySpec(byteChave, "DES");
        
        // Decifrar o texto original
        // ----------------------------------------------------------
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.DECRYPT_MODE, sk);
        byte[] criptogramaParaTextoClaro = c.doFinal(byteFicheiroDecifrar);
        
        // gravar o texto cifrado
        // ----------------------------------------------------------
        fo.write(criptogramaParaTextoClaro);
        
        // fechar todos os arquivos
        fi.close();
        fiChave.close();
        fo.flush();
        fo.close();
    }
}
