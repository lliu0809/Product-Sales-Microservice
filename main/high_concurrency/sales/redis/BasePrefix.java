package high_concurrency.sales.redis;

import high_concurrency.sales.redis.KeyPrefix;


// implementing KeyPrefix
public abstract class BasePrefix implements KeyPrefix{
	
	private int expireSeconds;
	
	private String prefix;
	
	public BasePrefix(String prefix) {
		// by default does not expire
		this(0, prefix);
	}
	
	public BasePrefix( int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	public int expireSeconds() {
		return expireSeconds;
	}

	public String getPrefix() {
		String className = getClass().getSimpleName();
		return className+ ":" + prefix;
	}

}
