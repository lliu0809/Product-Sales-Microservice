package high_concurrency.sales.vo;

import high_concurrency.sales.domain.OrderInfo;
import high_concurrency.sales.vo.ProductVo;

public class OrderDetailVo {
	private ProductVo product;
	private OrderInfo order;
	public ProductVo getProduct() {
		return product;
	}
	public void setProduct(ProductVo product) {
		this.product = product;
	}
	public OrderInfo getOrder() {
		return order;
	}
	public void setOrder(OrderInfo order) {
		this.order = order;
	}
}
