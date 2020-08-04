package high_concurrency.sales.redis;


// for key value we designed to access database
public interface KeyPrefix {
	
	public int expireSeconds();
	
	public String getPrefix();
	
}
