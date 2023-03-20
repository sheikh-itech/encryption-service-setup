package base.encryption.util;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoJSExample {

	public static void main(String[] args) throws Exception {
		
		// Add the Bouncy Castle provider
        Security.addProvider(new BouncyCastleProvider());

        // The password and encrypted data
        String password = "parichai-dashboard";
        String encryptedData = "RBZjEGu+quMcLGTB9XdSnA==";

        // Decode the Base64-encoded encrypted data
        byte[] encryptedBytes = java.util.Base64.getDecoder().decode(encryptedData);

        // Derive the key from the password using PBKDF2 with HMAC-SHA512
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 1000, 256);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BC");
        byte[] keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt the data using AES-256
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(new byte[16]));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Print the decrypted data
        System.out.println(new String(decryptedBytes, "UTF-8"));

	}
}
