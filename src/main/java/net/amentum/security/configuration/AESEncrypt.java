package net.amentum.security.configuration;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.*;
import java.util.Map;

public class AESEncrypt {

    private SecureRandom secureRandom;
    SecretKey secretKey;

    public AESEncrypt(String fileLocation) throws Exception {
        secureRandom = new SecureRandom();
        //this.generateKey();
        this.loadKey(fileLocation);
    }

    public void generateKey() throws IOException {
        byte key[] = new byte[16];
        secureRandom.nextBytes(key);
        secretKey = new SecretKeySpec(key, "AES");
        //SAVE TO FILE
        try (FileOutputStream fos = new FileOutputStream("secret.key")){
            fos.write(secretKey.getEncoded());
            fos.close();
        }
    }

    public void loadKey(String fileLocation) throws IOException {
        byte[] keyBytes = Files.readAllBytes(new File(fileLocation).toPath());
        secretKey = new SecretKeySpec(keyBytes, 0, 16, "AES");
        System.out.println("Key was load");
    }

    public String encrypt(String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,NoSuchProviderException {
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);// 128 bit auth tag length
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding","SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte [] encrypted = cipher.doFinal(message.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(1+iv.length + encrypted.length);
        byteBuffer.put((byte) iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);
        return  new String(Base64.encodeBase64(byteBuffer.array()));
    }

    public String decrypt(String message) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.decodeBase64(message));

        int ivLength = byteBuffer.get();
        byte [] iv = new byte[ivLength];
        byteBuffer.get(iv);
        byte [] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        final Cipher cipherDec = Cipher.getInstance("AES/GCM/NoPadding","SunJCE");
        cipherDec.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        return new String(cipherDec.doFinal(encrypted));
    }
}
