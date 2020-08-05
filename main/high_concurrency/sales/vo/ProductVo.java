package high_concurrency.sales.vo;

import java.util.Date;

public class ProductVo {
	private Double salesPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
