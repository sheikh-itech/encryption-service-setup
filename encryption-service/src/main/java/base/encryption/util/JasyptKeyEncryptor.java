package base.encryption.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Write key in 'encryption-key.txt' file for encryption
 * Keep single key per line only
 * Password should be same for both KeyEncryptor and
 * bean defined for Jasypt
 * 
 * @author Hapheej Sheikh
 */

public class JasyptKeyEncryptor {

	public static void main(String[] args) throws Exception {
		
		StringEncryptor encryptor = initEncryptor();
        
		File inputFile = new File("encryption-key.txt");
		File tempFile = new File("encrypted-key.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
		String line = null;
		
		while((line=reader.readLine()) != null && line.trim()!="") {
			
			writer.write(line + ": ENC(" + encryptor.encrypt(line.trim())+")" + System.getProperty("line.separator"));
		}
		
		writer.flush();
		writer.close();
		reader.close();
		inputFile.delete();
		tempFile.renameTo(new File("encryption-key.txt"));
		
		System.out.println("Key encryption complete...");
	}
	
	private static StringEncryptor initEncryptor() {
		
	    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
	    config.setPassword("encryption-service");//18 length
	    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
	    config.setKeyObtentionIterations("1000");
	    config.setPoolSize("1");
	    config.setProviderName("SunJCE");
	    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
	    config.setStringOutputType("base64");
	    encryptor.setConfig(config);
	    
	    return encryptor;
	}
}
 