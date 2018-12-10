package crypto222;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Dantiii
 */
public class Crypto222 {

    /**
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        // 2.2.2 => Geracao de uma chave simetrica e guardar num ficheiro
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        // kg.init(256); // DES por omissao já tem 256
        SecretKey sk = kg.generateKey();
        // neste kg pode-se indicar o tamanho da chave (128, 192 ou 256) para o 
        // caso de algoritmo AES

        // obter o array de byte
        byte[] keyEncoded = sk.getEncoded();

        // System.out.println("Chave secreta: " + keyEncoded.toString());
        // Obter o nome de um ficheiro
        Scanner sc = new Scanner(System.in);
        System.out.print("Indique o nome de um ficheiro: ");
        String nomeFicheiro = sc.nextLine();
        nomeFicheiro += ".txt";
        //System.out.println("file: " + nomeFicheiro);

        // gravar chave no ficheiro de texto
        FileOutputStream file = new FileOutputStream(nomeFicheiro);
        file.write(keyEncoded);
        file.flush();
        file.close();
    }

}

// Se quiser gerar uma chave AES e salvar num ficheiro externo
/*
FileOutputStream foChave = new FileOutputStream(nomeFicheiro, true); // true faz o append
KeyGenerator kg = KeyGenerator.getInstance(tipoAlgoritmo);

// neste kg pode-se indicar o tamanho da chave (128, 192 ou 256) para o 
// caso de algoritmo AES
if (tipoAlgoritmo.equals("AES")) {
    kg.init(tamanhoBit);
}
SecretKey sk = kg.generateKey();

// obter o array de byte
byte[] keyEncoded = sk.getEncoded();

// gravar uma copia da chave no ficheiro
// ----------------------------------------------------------
foChave.write(keyEncoded);
String lineSeparator = System.getProperty("line.separator"); // quebra de linha por causa do append
foChave.write(lineSeparator.getBytes());

foChave.flush();
foChave.close();
*/
