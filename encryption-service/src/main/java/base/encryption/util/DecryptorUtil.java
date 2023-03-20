package base.encryption.util;

import java.lang.reflect.Field;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DecryptorUtil {

	private static final Logger logger = LoggerFactory.getLogger(DecryptorUtil.class);
	
	private String password = "encryption-service";
    private Cipher cipher;
	private String prevEncKey;
	
	public DecryptorUtil() {
		// Add the Bouncy Castle provider
        Security.addProvider(new BouncyCastleProvider());
	}
	
	public void init(String password, String encKey) {
		try {
			if(this.cipher!=null && prevEncKey==encKey)
				return;
			
			prevEncKey = encKey;
			
			if(password==null)
				password = this.password;
	
			// Decode the hexadecimal IV string
			byte[] ivBytes = DatatypeConverter.parseHexBinary(encKey);
			
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 1000, 256);
	        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BC");
	        byte[] keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
	        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
	
	        // Decrypt the data using AES-256 with a custom IV
	        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
	     
	        this.cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(ivBytes));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Object decryptObjectFields(Object bean) {
		
		if(bean==null)
			return null;
		
		try {
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				
				field.setAccessible(true);
				
				switch(field.getType().toString().replace("class ", "")) {
				
					case "java.lang.String":
						field.set(bean, decryptKey(field.get(bean).toString()));
						break;
					case "byte":
					field.setByte(bean, Byte.parseByte(decryptKey(field.get(bean).toString())));
						break;
					case "short":
						field.setShort(bean, Short.parseShort(decryptKey(field.get(bean).toString())));
						break;
					case "int":
						field.setInt(bean, Integer.parseInt(field.get(bean).toString()));
						break;
					case "long":
						field.setLong(bean, Long.parseLong(decryptKey(field.get(bean).toString())));
						break;
					case "float":
						field.setFloat(bean, Float.parseFloat(decryptKey(field.get(bean).toString())));
						break;
					case "double":
						field.setDouble(bean, Double.parseDouble(decryptKey(field.get(bean).toString())));
						break;
					case "char":
						field.setChar(bean, decryptKey(field.get(bean).toString()).charAt(0));
						break;
					case "boolean":
						field.setBoolean(bean, Boolean.getBoolean(decryptKey(field.get(bean).toString())));
						break;
					default: logger.error("Data type not matched for: "+field.getName());;
				}
				field.setAccessible(false);
			}
		} catch (Exception ex) {
			logger.error("Bean decryption issue, "+ex.getMessage());
		}

		return bean;		
	}
	
	private String decryptKey(String encryptedText) throws Exception {
		
		if(encryptedText==null)
			return encryptedText;
		
		// Decode the Base64-encoded encrypted data
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        
        byte[] decryptedBytes = this.cipher.doFinal(encryptedBytes);

        // return decrypted data
        return new String(decryptedBytes, "UTF-8");
	}
	/*
	 private String decryptKey(String encryptedText) throws Exception {
		
		// Decode the Base64-encoded encrypted data
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

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

        // return decrypted data
        return new String(decryptedBytes, "UTF-8");
	}
	 */
}
