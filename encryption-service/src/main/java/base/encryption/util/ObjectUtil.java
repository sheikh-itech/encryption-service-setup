package base.encryption.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ObjectUtil {

	private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);
		
	/**
	 * @param bean object need to check for SQL Injection Possibility
	 * @return Map<String, String> holds method name and it's returned value<br />
	 *         If Returned Map is empty that means 'No Sql Injection' found
	 */
	public Map<String, String> validateInputObject(Object bean) {

		Map<String, String> outcome = new HashMap<>();

		checkSqlInjection(bean, outcome);

		return outcome;
	}

	private void validateObject(String fieldName, Object fieldValue, Map<String, String> outcome) {
		
		if(fieldValue==null)
			return;
		
		String value = fieldValue.toString().trim();
		
		if(fieldName.startsWith("name") || validAlphaNum(value) || !alphaNumeric(value))
			System.out.println("checked");
			
	}
	
	private boolean alphaNumeric(String text) {
		
		if("".equals(text))
			return true;
		
		return text.matches("^[\\pL\\pN]+$");
	}
	
	private boolean validAlphaNum(String text) {
		
		if("".equals(text))
			return true;
		
		return text.matches("^[\\pL\\pN\\.\\:\\_]+$");
	}
	
	private void checkSqlInjection(Object bean, Map<String, String> outcome) {

		try {

			if (bean == null)
				return;

			Method[] a = bean.getClass().getMethods();
			for (Method method : a) {

				if (isGetter(method)) {
					
					if (chechReturnType(method.getReturnType().getName()))
						checkSqlInjection(method.invoke(bean), outcome);

					Object value = method.invoke(bean);

					String field = null;
					if (method.getName().startsWith("get"))
						field = method.getName().substring(3);
					else if (method.getName().startsWith("is"))
						field = method.getName().substring(2);
					else
						continue;
					
					//field = StringUtils.uncapitalize(field);
					
					validateObject(field, value, outcome);
					
					if (value!=null && isSQLInjection(value))
						outcome.put(field, "Sql Injection Possibility");
				}
			}
		} catch (Exception ex) {
			logger.error("Sql Injection Check error, "+ex.getMessage());
		}
	}

	private boolean chechReturnType(String name) {

		return name.startsWith("com");
	}

	private boolean isGetter(Method method) {

		if (method.getName().startsWith("get") || method.getName().startsWith("is"))
			return true;

		return false;
	}

	private boolean isSQLInjection(Object object) {

		String REGEX = "\\s*and\\s*|\\s*or\\s*|\\s*between\\*+|\\s*union\\s*|\\s*join\\s*|\\s*sleep\\s*|\\s*shutdown\\s*|\\s*select\\s*|\\s*drop\\s*|\\s*delete\\s*|[\\(\\)<>;'\"`#=%*]|[-]{2,}/i";
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(object.toString().trim().toLowerCase());

		if (m.matches())
			return true;
		else
			return false;
	}
}
