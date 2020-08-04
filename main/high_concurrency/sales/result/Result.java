package high_concurrency.sales.result;

public class Result<T> {
	private int code;
	private String msg;
	private T data;
	

	public static <T> Result<T> success(T data){
		return new  Result<T>(data);
	}
	
	
	public static <T> Result<T> error(CodeMsg errMsg){
		return new Result<T>(errMsg);
	}
	
	private Result(T data) {
		this.code = 0;
		this.msg = "Success!";
		this.data = data;
	} 
	
	private Result(CodeMsg mMsg) {
		if(mMsg == null) {
			return;
		}
		this.code = mMsg.getCode();
		this.msg = mMsg.getMsg();
	}
	
	public int getCode() {
		return code;
	}
	
	
	public String getMsg() {
		return msg;
	}
	
	
	public T getData() {
		return data;
	}
}
