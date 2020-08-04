package high_concurrency.sales.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {
	// phone number should be of 10 digits
	private static final Pattern phone_pattern = Pattern.compile("d{10}");
	
	public static boolean validNumber(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = phone_pattern.matcher(src);
		return m.matches();
	}
}
