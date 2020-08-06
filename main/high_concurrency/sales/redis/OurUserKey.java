package high_concurrency.sales.redis;

import high_concurrency.sales.redis.BasePrefix;
import high_concurrency.sales.redis.OurUserKey;

public class OurUserKey extends BasePrefix{

	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
	private OurUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static OurUserKey token = new OurUserKey(TOKEN_EXPIRE, "tk");
	public static OurUserKey getById = new OurUserKey(0, "id");

}
