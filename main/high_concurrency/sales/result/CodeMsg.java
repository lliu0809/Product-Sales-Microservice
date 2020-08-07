package high_concurrency.sales.result;

import high_concurrency.sales.result.CodeMsg;

public class CodeMsg {
	private int code;
	private String msg;
	
	public static CodeMsg SUCCESS = new CodeMsg(0, "Success!");
	
	// General Error 5001XX
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "Server Error!");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "Data Error!");
	
	
	// Login Error 5002XX
	public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session does not exist or has already ended");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "Password cannot be empty!");
	public static CodeMsg PHONE_NUM_EMPTY = new CodeMsg(500212, "Phone number cannot be empty!");
	public static CodeMsg PHONE_NUM_ERROR = new CodeMsg(500213, "Phone number is wrong.");
	public static CodeMsg PHONE_NUM_NOT_EXIST = new CodeMsg(500214, "Phone number does not exist!");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "Password is not correct.");
	
	
	// Product Error 5003XX
	
	// Order Error 5004XX
	public static CodeMsg REPEAT_SALES = new CodeMsg(500430, "Order already placed.");
	public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500431, "Order does not exist.");

	
	
	// Sales Error 5005XX
	public static CodeMsg NO_PRODUCT = new CodeMsg(500540, "Product sold out.");
	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
}
