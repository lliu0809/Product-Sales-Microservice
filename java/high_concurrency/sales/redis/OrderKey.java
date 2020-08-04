package high_concurrency.sales.redis;

import high_concurrency.sales.redis.BasePrefix;


// key for orders
public class OrderKey extends BasePrefix {

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
