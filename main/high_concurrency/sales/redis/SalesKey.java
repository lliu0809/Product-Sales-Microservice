package high_concurrency.sales.redis;

import high_concurrency.sales.redis.BasePrefix;

public class SalesKey extends BasePrefix{

	private SalesKey(String prefix) {
		super(prefix);
	}
	public static SalesKey isProductOver = new SalesKey("go");
}
