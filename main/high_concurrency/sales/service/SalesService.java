package high_concurrency.sales.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.domain.SalesOrder;
import high_concurrency.sales.redis.SalesKey;
import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.domain.OrderInfo;
import high_concurrency.sales.service.ProductService;
import high_concurrency.sales.service.OrderService;
import high_concurrency.sales.vo.ProductVo;

@Service
public class SalesService {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo order(OurUser user, ProductVo Product) {
		boolean success = ProductService.reduceStock(Product);
		if(success) {
			return orderService.createOrder(user, Product);
		}
		else {
			setProductOver(Product.getId());
			return null;
		}
	}

	public long getSalesResult(Long userId, long ProductId) {
		SalesOrder order = orderService.getSalesOrderByUserIdProductId(userId, ProductId);
		if(order != null) {
			return order.getOrderId();
		}else {
			boolean SalesEnded = getProductOver(ProductId);
			if(SalesEnded) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private void setProductOver(Long ProductId) {
		redisService.set(SalesKey.isProductOver, ""+ProductId, true);
	}
	
	private boolean getProductOver(long ProductId) {
		return redisService.exists(SalesKey.isProductOver, ""+ProductId);
	}
	
}
