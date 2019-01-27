package utils;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSA {
    final static int keySize = 2048;

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes());
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

    public static void generateKeyFiles(PrivateKey privateKey, PublicKey publicKey, String uniqueName) {
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
                    RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
                    RSAPrivateKeySpec.class);

            saveToFile(uniqueName + "public.key", pub.getModulus(),
                    pub.getPublicExponent());
            saveToFile(uniqueName + "private.key", priv.getModulus(),
                    priv.getPrivateExponent());

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            outputStream.writeObject(mod);
            outputStream.writeObject(exp);
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            outputStream.close();
        }
    }

    public static Key readKeyFromFile(String keyFileName, boolean returnPublicKey) throws IOException {
        InputStream in =
                RSA.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin =
                new ObjectInputStream(new BufferedInputStream(in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();

            if (returnPublicKey) {
                RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
                KeyFactory fact = KeyFactory.getInstance("RSA");
                return fact.generatePublic(keySpec);
            } else {
                RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
                KeyFactory fact = KeyFactory.getInstance("RSA");
                return fact.generatePrivate(keySpec);
            }
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
}
