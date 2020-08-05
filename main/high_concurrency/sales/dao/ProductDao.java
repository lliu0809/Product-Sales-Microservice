package high_concurrency.sales.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import high_concurrency.sales.domain.SalesProduct;
import high_concurrency.sales.vo.ProductVo;

@Mapper
public interface ProductDao {
	
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.sales_price from sales_product mg left join product g on mg.product_id = g.id")
	public List<ProductVo> listProductVo();

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.sales_price from sales_product mg left join product g on mg.v_id = g.id where g.id = #{productId}")
	public ProductVo getProductVoByProductId(@Param("productId")long productId);

	@Update("update sales_product set stock_count = stock_count - 1 where product_id = #{productId}")
	public int reduceStock(SalesProduct g);
	
}
 