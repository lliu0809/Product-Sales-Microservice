package high_concurrency.sales.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import high_concurrency.sales.rabbitmq.MQConfig;
import high_concurrency.sales.rabbitmq.MQSender;
import high_concurrency.sales.rabbitmq.MQMessage;
import high_concurrency.sales.redis.RedisService;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;
	
	public void sendMQMessage(MQMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.SALES_QUEUE, msg);
	}

	
}
