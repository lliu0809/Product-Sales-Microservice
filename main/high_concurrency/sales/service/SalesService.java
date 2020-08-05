package high_concurrency.sales.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public OrderInfo sales(OurUser user, ProductVo product) {
		productService.reduceStock(product);
		return orderService.createOrder(user, product);
	}
	
}
