package high_concurrency.sales.vo;

import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.vo.ProductVo;

public class ProductDetailVo {
	private int salesStatus = 0;
	private int timeRemaining = 0;
	private ProductVo product ;
	private OurUser user;
	public int getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(int salesStatus) {
		this.salesStatus = salesStatus;
	}
	public int getTimeRemaining() {
		return timeRemaining;
	}
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
	public ProductVo getProduct() {
		return product;
	}
	public void setProduct(ProductVo product) {
		this.product = product;
	}
	public OurUser getUser() {
		return user;
	}
	public void setUser(OurUser user) {
		this.user = user;
	}
	
	
}
