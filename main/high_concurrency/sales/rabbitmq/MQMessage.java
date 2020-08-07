package high_concurrency.sales.rabbitmq;

import high_concurrency.sales.domain.OurUser;

public class MQMessage {
	private OurUser user;
	private long productId;
	
	public OurUser getUser() {
		return user;
	}
	public void setUser(OurUser user) {
		this.user = user;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
}
