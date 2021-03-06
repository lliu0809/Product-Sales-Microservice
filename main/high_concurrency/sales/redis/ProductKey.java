package high_concurrency.sales.redis;

import high_concurrency.sales.redis.BasePrefix;
import high_concurrency.sales.redis.ProductKey;

public class ProductKey extends BasePrefix{

	private ProductKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static ProductKey getProductList = new ProductKey(60, "product_list");
	public static ProductKey getProductDetail = new ProductKey(60, "product_detail");
	public static ProductKey getSalesProductcount = new ProductKey(0, "product_stock");
}
