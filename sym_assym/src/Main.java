import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {

    static final String TEXT_FILENAME = "texto.txt";
    static final String PUBLIC_KEY =  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCfeP59DDplf+EzYWYeXjLscRqXQQqnpyxG2220CR54WbK5yVVOBRH0tqxJyRWUfx1hOaLwowPm/BTnWOF9PQq7nL804a9ENxrrybm2j4q7PUT7QGUKmwmGVtmIFt1ioKN6QYglgIq00NF8h/spdTvW76taDShsNUw8XR/0dgj5hUc/1SxvlXhFedMmBg9+b+iZpDH0ZKflxXbNm94TOjjw168rgVO72fPFkDvklFp4cL8ijSK6rvMEcltDZ+4F8GBFyEUbXXwQ4fNN0S2aI0dSqrJTJTWaSR4vw5zGS20XvVuFbapDfJYte28mkGxDsHDwHaKFPaFy0pPcN6LN6+QIDAQAB";
    static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwJ94/n0MOmV/4TNhZh5eMuxxGpdBCqenLEbbbbQJHnhZsrnJVU4FEfS2rEnJFZR/HWE5ovCjA+b8FOdY4X09CrucvzThr0Q3GuvJubaPirs9RPtAZQqbCYZW2YgW3WKgo3pBiCWAirTQ0XyH+yl1O9bvq1oNKGw1TDxdH/R2CPmFRz/VLG+VeEV50yYGD35v6JmkMfRkp+XFds2b3hM6OPDXryuBU7vZ88WQO+SUWnhwvyKNIrqu8wRyW0Nn7gXwYEXIRRtdfBDh803RLZojR1KqslMlNZpJHi/DnMZLbRe9W4VtqkN8li17byaQbEOwcPAdooU9oXLSk9w3os3r5AgMBAAECggEASWtke1nyxfvw/vlwVuhnptU5tMxZX9+XvPaWIyBtCdJ/AC85Ig0a3KPby+h1Ti0WsKxCie6agcvV7OStP3OiAmYJn2fGc1F/j2vNrW7vFoTLjc5DR7P68xtfHdP/E+rUs6wHu4Sy1+Ee2BEqhxprB4TuHLPpppJd3Fd22Z9KlESgoG5GMdsBoRecp0msoFX7URpUc8Q60auSpPbd66aXXCE1t/xvjKOO8dOAStcLPfshAYisvBEXr3CmqT4CbwzqPmpqSIznyXYCJakS/lf2WC7OES8o4rBkNvKiwTwjJ7Gr1Kox8CXQvMe2eUA+ncUwKMYaiITF06u49/dRQ5CXAQKBgQDUlXbXgr+jH1D/A/II/nzprvgVr/Xhn2qFZCTxSZi1jSRVaXA0KnHyTF+XnwoXmiPMDC46e3KyQ3WhAE469Kg9f7XDGsppk0C7ij64zonL8aU2RTmfP0mv5JAOV5BZ5ai0E9ib90GYlco+Goza9yJ0iCxXscj7sWE/t+2WMtFoGQKBgQDUIdSgO3UvImhohfkIgU/0l2YE4NQLVXO68bKlh1d7ygucOzfcACwQUEymWuPjwuC4+Dj2+FkFZkj18TUgQjVesQ6SgmQ+4CZi9jS30b8DJYwTxIgj+VnXdJ+qeri2/PkS08lgjeaXBAopo9SPM59PiBTKlA2CuwtrvoBqqmKF4QKBgAf8bXB/Ku/X11UdMtR/qvWkaxF1gMkvEfNc5b5iw2fem4TR5zMufQVbNSQfB2QHmFysAHiV9qMXwa2As2+njUJyL8Pal8wLih0BfoW2zJpqw4gcZaPD3uLKtVa0l2mpJQNO045YZZBz4BshKDV5n0O9cd0BgslhyoN4R2ajFhbRAoGAWSRuSXcy4z++SE4kPGK6yrnkcSBZevnJzEFNJSoepTJedqSb8KNR4PkdfLqtEUUPUitdJMtv/UjiWBeoj4nDC6uzx/VrUtC20NdNiAFoF1Zr6tKnsxZRnqyve+BeRuc/c53z/dMjl0pnSuBBrnuu8qjos8hLdShMwFYpeKlpBYECgYEArxjX0ZhVdVDQWetTSZEPgs2zgzrQb3lCnhG+8ui01raD+3e2hX0uP0nfvLqo7t4vbls/EsQYhj9tFL47vFv7MPoY/UGg461l6rAJcjRab0HZczW1ZSyNPm49YJi39Vu+qx38oOhdXMmSYwW/EKweEZho5+/OvDLUb67fDizTWYM=";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String plainText = SymAssym.readFile(TEXT_FILENAME);

        String symKey = SymAssym.createSymKey();

        System.out.println("SymKey: "+symKey);
        System.out.println("TClaro: "+ plainText);

        //Encoding
        byte[] encodedKey = Base64.getDecoder().decode(symKey);
        SecretKey symKeyDec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        //Encrypt sym AES
        byte [] symText = SymAssym.encryptAES(symKeyDec , plainText);
        System.out.println("TCifrado: "+symText);

        //Criar par de chaves
        SymAssym.createAsymKeys();
        KeyPair kp = SymAssym.createAsymKeys();

        PublicKey publicKey = kp.getPublic();
        //Public key to file
        SymAssym.savePubKeyToFile(publicKey);
        System.out.println("pubkey:" +publicKey);

        //Public key to string
        String b64pubKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("Encoded Public Key: "+b64pubKey);

        //Ler a pub do ficheiro
        publicKey = SymAssym.loadPubKeyFromFile("pubKey");

        //Encrypt Assym RSA
        byte[] encryptedKey = SymAssym.encryptRSA(symKeyDec, SymAssym.loadPublicKey(PUBLIC_KEY));
        System.out.println("encryptedKey: "+encryptedKey);

        PrivateKey privateKey = kp.getPrivate();
        //Private key to file
        SymAssym.savePriKeytoFile(privateKey);
        System.out.println("privKey: "+privateKey);

        //Private key to string
        String b64priKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("Encoded Private Key: "+b64priKey);

        //Ler a pri do ficheiro
        privateKey = SymAssym.loadPriKeyFromFile("priKey");
        System.out.println("privKey: "+privateKey);

        //Decoding
        String decodedText = SymAssym.decrypt (SymAssym.loadPrivateKey(PRIVATE_KEY), symText, encryptedKey);
        System.out.println("decodedText:" +decodedText);

        //Signing
            KeyPair kpSign = SignVerify.createAsymKeys();
            PrivateKey privateKeySign = kpSign.getPrivate();
            PublicKey publicKeySign = kpSign.getPublic();

            String sig = SignVerify.sign(plainText, privateKeySign);
            System.out.println("Signature: "+sig);

        //Verifying
        Boolean isSigned = SignVerify.verify(plainText, sig, publicKeySign);
        if (isSigned) {
            System.out.println("Dados autênticos");
        } else {
            System.out.println("Dados NÃO autênticos");
        }
    }
}
