
package crypto223;

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
    => 2.2.3 Cifra de um fcheiro utilizando o algoritmo DES
    Crie um programa para cifrar o conteúdo de um ficheiro usando o algoritmo
    de cifra DES. O programa deve ter os seguintes parâmetros de entrada: 
    * (i) o nome do ficheiro a cifrar, 
    * (ii) o nome pretendido para o ficheiro cifrado e
    * (iii) o nome do ficheiro com a chave simétrica a usar.
 *
 * @author Dantiii
 */
public class Crypto223 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Scanner scan = new Scanner(System.in);
        
        // Param de entrada
        // (i) nome do ficheiro a cifrar
        System.out.print("Nome do ficheiro a cifrar: ");
        String ficheiroCifrar = scan.nextLine();
        
        // (i) nome para o ficheiro cifrado
        System.out.print("Nome do ficheiro cifrado: ");
        String ficheiroCifrado = scan.nextLine();
        
        // (i) nome do ficheiro com a chave simétrica
        System.out.print("Nome do ficheiro com a chave simétrica: ");
        String chave = scan.nextLine();
        
        // Obter instancias dos files
        // ----------------------------------------------------------
        FileInputStream fi = new FileInputStream(ficheiroCifrar);
        FileInputStream fiChave = new FileInputStream(chave);
        FileOutputStream fo = new FileOutputStream(ficheiroCifrado);
        
        // Obter texto do arquivo a cifrar
        // ----------------------------------------------------------
        int tamanhoFicheiroCifrar = fi.available();
        byte[] byteFicheiroCifrar = new byte[tamanhoFicheiroCifrar];
        fi.read(byteFicheiroCifrar);
        
        // Obter chave
        // ----------------------------------------------------------
        int tamanhoChave = fiChave.available();
        byte[] byteChave = new byte[tamanhoChave];
        fiChave.read(byteChave);
        
        // Instancia do SecretKey
        SecretKey sk = new SecretKeySpec(byteChave, "DES");
        
        // Cifrar o texto original
        // ----------------------------------------------------------
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.ENCRYPT_MODE, sk);
        byte[] criptograma = c.doFinal(byteFicheiroCifrar);
        
        // gravar o texto cifrado
        // ----------------------------------------------------------
        fo.write(criptograma);
        
        // fechar todos os arquivos
        fi.close();
        fiChave.close();
        fo.flush();
        fo.close();
    }
}