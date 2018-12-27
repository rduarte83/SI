package utils;

import java.io.*;

public class Utils {

    /**
     * Lê ficheito em bytes
     * @param f ficheiro a ser lido
     * @return bytes do ficheiro lido
     * @throws IOException
     * @see File
     */
    public static byte[] getFileInBytes(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fbytes = new byte[(int) f.length()];
        fis.read(fbytes);
        fis.close();
        return fbytes;
    }

    /**
     * Trato o texto dividindo-o por linhas
     * @param texto texto a ser lido
     * @return linhas separadas
     * @throws IOException
     */
    public static String[] extrairLinhas(String texto) throws IOException {
        LineNumberReader reader = new LineNumberReader(new StringReader(texto));
        String[] lines = texto.split("\n");
        String currentLine = null;
        String[] linhas = new String[2];
        while ((currentLine = reader.readLine()) != null) {
            if (reader.getLineNumber() == 1) {
                linhas[0] = currentLine;
            }
            if (reader.getLineNumber() == 2) {
                linhas[1] = currentLine;
            }
        }
        return linhas;
    }
}
