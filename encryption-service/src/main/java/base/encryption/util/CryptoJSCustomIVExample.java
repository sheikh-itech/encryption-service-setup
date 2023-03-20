package base.encryption.util;

import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoJSCustomIVExample {

	public static void main(String[] args) throws Exception {
		
		// Add the Bouncy Castle provider
        Security.addProvider(new BouncyCastleProvider());

        // The password and encrypted data
        String password = "encryption-service";
        String encryptedData = "RsK4es6bSbNq+rYe4aEwCQ==";
        String iv = "0123456789abcdef0123456789abcdef";

        // Decode the Base64-encoded encrypted data
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // Decode the hexadecimal IV string
        byte[] ivBytes = DatatypeConverter.parseHexBinary(iv);

        // Derive the key from the password using PBKDF2 with HMAC-SHA512
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 1000, 256);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BC");
        byte[] keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt the data using AES-256 with a custom IV
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(ivBytes));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Print the decrypted data
        System.out.println(new String(decryptedBytes, "UTF-8"));

	}
}
