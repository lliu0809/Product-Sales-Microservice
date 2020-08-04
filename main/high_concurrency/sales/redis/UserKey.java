package high_concurrency.sales.redis;

import high_concurrency.sales.redis.BasePrefix;
import high_concurrency.sales.redis.UserKey;


// key for user
public class UserKey extends BasePrefix{

	private UserKey(String prefix) {
		super(prefix);
	}
	public static UserKey getById = new UserKey("id");
	public static UserKey getByName = new UserKey("name");
}
