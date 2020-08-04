package high_concurrency.sales.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	// alto processed
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	// static salt
	private static final String salt = "abcdefg1234567";
	
	// first MD5 process: user input password 
	public static String inputPassToFormPass(String inputPass) {
		String str = "" + salt.charAt(0) + salt.charAt(3) + inputPass + salt.charAt(8) + salt.charAt(4);
		System.out.println(str);
		return md5(str);
	}
	
	// second MD5 process: store to database with random salt
	public static String formPassToDBPass(String formPass, String salt) {
		String str = "" + salt.charAt(0) + salt.charAt(3) + formPass + salt.charAt(8) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDbPass(String inputPass, String randomSaltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, randomSaltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("123456"));
	}
	
}