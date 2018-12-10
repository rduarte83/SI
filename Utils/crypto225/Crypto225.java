package crypto225;

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
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Dantiii
 */
public class Crypto225 {

    static Scanner scan = new Scanner(System.in);

    /**
     * Links:
     * https://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException {

        int op = 0;
        do {
            System.out.println("1 - Gerar chave simétrica (DES ou AES)");
            System.out.println("2 - Enciptar (DES ou AES)");
            System.out.println("3 - Deciptar (DES ou AES)");
            System.out.println("0 - SAIR");
            System.out.print("\nSelecione uma opção: ");
            op = Integer.parseInt(scan.nextLine());

            switch (op) {
                case 1:
                    gerarChave();
                    break;
                case 2:
                    encriptar();
                    break;
                case 3:
                    decriptar();
                    break;
            }
        } while (op != 0);
    }
    
    // -------------------------------------------------------------------------
    // Funcao para gerar chave AES ou DES e guardar num ficheiro TXT
    // -------------------------------------------------------------------------
    static void gerarChave() throws FileNotFoundException, NoSuchAlgorithmException, IOException {

        System.out.print("Nome do ficheiro para a chave: ");
        String nomeFicheiroChave = scan.nextLine();
        nomeFicheiroChave += ".txt";

        System.out.print("Tipo de algoritmo que vai usar (DES ou AES): ");
        String tipoAlgoritmo = scan.nextLine();

        int tamanhoBit = 0;
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            System.out.print("Tamanho do algoritmo AES (128, 192 ou 256): ");
            tamanhoBit = Integer.parseInt(scan.nextLine());
        }

        FileOutputStream foChave = new FileOutputStream(nomeFicheiroChave); // true como 2º param faz o append
        KeyGenerator kg = KeyGenerator.getInstance(tipoAlgoritmo);

        // neste kg pode-se indicar o tamanho da chave (128, 192 ou 256) para o 
        // caso de algoritmo AES
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            kg.init(tamanhoBit);
        }
        SecretKey sk = kg.generateKey();

        // obter o array de byte
        byte[] keyEncoded = sk.getEncoded();

        // gravar uma copia da chave no ficheiro
        // ----------------------------------------------------------
        foChave.write(keyEncoded);
        //String lineSeparator = System.getProperty("line.separator"); // quebra de linha por causa do append
        //foChave.write(lineSeparator.getBytes());
        foChave.flush();
        foChave.close();
        
        System.out.println("Chave gerada com sucesso!");
    }

    // -------------------------------------------------------------------------
    // Funcao para encriptar, pedindo o algoritmo (AES ou DES)
    // -------------------------------------------------------------------------
    static void encriptar() throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Param de entrada
        // (i) nome do ficheiro a cifrar
        System.out.print("Nome do ficheiro a cifrar: ");
        String ficheiroCifrar = scan.nextLine();
        ficheiroCifrar += ".txt";

        // (i) nome para o ficheiro cifrado
        System.out.print("Nome do ficheiro cifrado: ");
        String ficheiroCifrado = scan.nextLine();
        ficheiroCifrado += ".txt";

        System.out.print("Tipo de algoritmo que vai usar (DES ou AES): ");
        String tipoAlgoritmo = scan.nextLine();
        int tamanhoBit = 0;
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            System.out.print("Tamanho do algoritmo AES (128, 192 ou 256): ");
            tamanhoBit = Integer.parseInt(scan.nextLine());
            
            // (i) nome do ficheiro com a chave simétrica
            System.out.print("Nome do ficheiro com a chave do tipo " + tipoAlgoritmo + "//" + tamanhoBit + ": ");
        }
        
        // (i) nome do ficheiro com a chave simétrica
        System.out.print("Nome do ficheiro com a chave do tipo " + tipoAlgoritmo + ": ");
        String chave = scan.nextLine();
        chave += ".txt";

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
        
        KeyGenerator kg = KeyGenerator.getInstance(tipoAlgoritmo); // DES ou AES
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            kg.init(tamanhoBit); // DES por omissao já tem 256 bits
        }
        // Instancia do SecretKey
        SecretKey sk = new SecretKeySpec(byteChave, tipoAlgoritmo); // para usar no caso de buscar a informacao de um ficheiro (eu acho)
        
        // Cifrar o texto original
        //   ----------------------------------------------------------
        Cipher c = Cipher.getInstance(tipoAlgoritmo);
        c.init(Cipher.ENCRYPT_MODE, sk);
        byte[] criptograma = c.doFinal(byteFicheiroCifrar);

        // gravar o texto cifrado
        // ----------------------------------------------------------
        fo.write(criptograma);

        // fechar todos os arquivos
        fi.close();
        fo.flush();
        fo.close();
        
        System.out.println("Ficheiro encriptado com sucesso eom o algoritmo " + tipoAlgoritmo + "!");
    }

    // -------------------------------------------------------------------------
    // Funcao para decriptar, pedindo o algoritmo (AES ou DES)
    // -------------------------------------------------------------------------
    static void decriptar() throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Param de entrada
        // (i) nome do ficheiro a decifrar
        System.out.print("Nome do ficheiro a decifrar: ");
        String ficheiroDecifrar = scan.nextLine();
        ficheiroDecifrar += ".txt";

        // (i) nome para o ficheiro decifrado
        System.out.print("Nome do ficheiro decifrado: ");
        String ficheiroDecifrado = scan.nextLine();
        ficheiroDecifrado += ".txt";

        System.out.print("Tipo de algoritmo que vai usar (DES ou AES): ");
        String tipoAlgoritmo = scan.nextLine();

        int tamanhoBit = 0;
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            System.out.print("Tamanho do algoritmo AES (128, 192 ou 256): ");
            tamanhoBit = Integer.parseInt(scan.nextLine());
        }

        // (i) nome do ficheiro com a chave simétrica
        System.out.print("Nome do ficheiro que contém a chave " + tipoAlgoritmo + " gerada anteriormente: ");
        String chave = scan.nextLine();
        chave += ".txt";

        // Obter instancias dos files
        // ----------------------------------------------------------
        FileInputStream fi = new FileInputStream(ficheiroDecifrar);
        FileInputStream fiChave = new FileInputStream(chave);
        FileOutputStream fo = new FileOutputStream(ficheiroDecifrado);

        // Obter texto do arquivo a cifrar
        // ----------------------------------------------------------
        int tamanhoFicheiroDecifrar = fi.available();
        byte[] byteFicheiroDecifrar = new byte[tamanhoFicheiroDecifrar];
        fi.read(byteFicheiroDecifrar);

        // Obter chave
        // ----------------------------------------------------------
        int tamanhoChave = fiChave.available();
        byte[] byteChave = new byte[tamanhoChave];
        fiChave.read(byteChave);

        KeyGenerator kg = KeyGenerator.getInstance(tipoAlgoritmo); // DES ou AES
        if (tipoAlgoritmo.toUpperCase().equals("AES")) {
            kg.init(tamanhoBit); // DES por omissao já tem 256
        }

        // Instancia do SecretKey
        // para usar no caso de buscar a informacao de um ficheiro (eu acho)
        SecretKey sk = new SecretKeySpec(byteChave, tipoAlgoritmo);

        // Decifrar o texto original
        // ----------------------------------------------------------
        Cipher c = Cipher.getInstance(tipoAlgoritmo);
        c.init(Cipher.DECRYPT_MODE, sk);
        byte[] criptogramaTextoClaro = c.doFinal(byteFicheiroDecifrar);

        // gravar o texto cifrado
        // ----------------------------------------------------------
        fo.write(criptogramaTextoClaro);

        // fechar todos os arquivos
        fi.close();
        fo.flush();
        fo.close();
        
        System.out.println("Ficheiro decriptado com sucesso eom o algoritmo " + tipoAlgoritmo + "!");
    }
}
