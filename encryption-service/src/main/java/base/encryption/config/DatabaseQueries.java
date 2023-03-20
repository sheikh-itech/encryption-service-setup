package base.encryption.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/encrypt-config/database-queries.properties")
@ConfigurationProperties(prefix = "encryption")
public class DatabaseQueries {
	
	private String userDetail;

	public String getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}
}
