package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Utils {


    public static String encriptarB64(String msg) {
        String base64encodedString = Base64.getEncoder().encodeToString(msg.getBytes(StandardCharsets.UTF_8));
        return base64encodedString;
    }

    public static byte[] desencriptarB64(String msg) {
        // Decode
        byte[] base64decodedBytes = Base64.getDecoder().decode(msg);
        return base64decodedBytes;
        //return new String(base64decodedBytes, StandardCharsets.UTF_8);
    }


    public static byte[] getFileInBytes(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fbytes = new byte[(int) f.length()];
        fis.read(fbytes);
        fis.close();
        return fbytes;
    }

    public static String[] extrairLinhas(String texto) throws IOException {
        LineNumberReader reader = new LineNumberReader(new StringReader(texto));
        String[] lines = texto.split("\n");
        String currentLine = null;
        String[] linhas = new String[2];
        while ((currentLine = reader.readLine()) != null) {
            System.out.println("currentLine:"+currentLine);
            if (reader.getLineNumber() == 1) {
                linhas[0] = currentLine;
            }
            if (reader.getLineNumber() == 2) {
                linhas[1] = currentLine;
            }
        }
        return linhas;
    }

    public static PrivateKey readFilePriv(String path)  {
        try {
            InputStream in = Utils.class.getResourceAsStream("/keys/"+path);
            byte[] fbytes = new byte[(int) in.available()];
            in.read(fbytes);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(fbytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PublicKey readFilePub(String path)  {
        try {
            InputStream in = Utils.class.getResourceAsStream("/keys/"+path);
            byte[] fbytes = new byte[(int) in.available()];
            in.read(fbytes);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(fbytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }


}
