package high_concurrency.sales.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import high_concurrency.sales.domain.SalesOrder;
import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.service.ProductService;
import high_concurrency.sales.service.SalesService;
import high_concurrency.sales.service.OrderService;
import high_concurrency.sales.vo.ProductVo;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
		@Autowired
		ProductService productService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		SalesService salesService;
		
		@RabbitListener(queues=MQConfig.SALES_QUEUE)
		public void receive(String message) {
			log.info("receive message:" + message);
			MQMessage mm  = RedisService.stringToBean(message, MQMessage.class);
			OurUser user = mm.getUser();
			long productId = mm.getProductId();
			
			ProductVo product = productService.getProductVoByProductId(productId);
	    	int stock = product.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}

	    	SalesOrder order = orderService.getSalesOrderByUserIdProductId(user.getId(), productId);
	    	if(order != null) {
	    		return;
	    	}
	    	
	    	((Object) salesService).sales(user, product);
		}
		
}