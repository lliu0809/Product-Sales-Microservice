package high_concurrency.sales.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import high_concurrency.sales.dao.OrderDao;
import high_concurrency.sales.domain.SalesOrder;
import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.domain.OrderInfo;
import high_concurrency.sales.vo.ProductVo;

@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;
	
	public SalesOrder getSalesOrderByUserIdProductId(long userId, long productId) {
		return orderDao.getSalesOrderByUserIdProductId(userId, productId);
	}

	@Transactional
	public OrderInfo createOrder(OurUser user, ProductVo product) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setProductCount(1);
		orderInfo.setProductId(product.getId());
		orderInfo.setProductName(product.getProductName());
		orderInfo.setProductPrice(product.getSalesPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		long orderId = orderDao.insert(orderInfo);
		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setProductId(product.getId());
		salesOrder.setOrderId(orderId);
		salesOrder.setUserId(user.getId());
		orderDao.insertSalesOrder(salesOrder);
		return orderInfo;
	}
	