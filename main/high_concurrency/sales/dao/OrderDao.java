package high_concurrency.sales.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import high_concurrency.sales.domain.SalesOrder;
import high_concurrency.sales.domain.OrderInfo;

@Mapper
public interface OrderDao {
	
	@Select("select * from sales_order where user_id=#{userId} and product_id=#{productId}")
	public SalesOrder getSalesOrderByUserIdProductId(@Param("userId")long userId, @Param("productId")long productId);

	@Insert("insert into order_info(user_id, product_id, product_name, product_count, product_price, product_channel, status, create_date)values("
			+ "#{userId}, #{productId}, #{productName}, #{productCount}, #{productPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	public long insert(OrderInfo orderInfo);
	
	@Insert("insert into sales_order (user_id, product_id, order_id)values(#{userId}, #{productId}, #{orderId})")
	public int insertSalesOrder(SalesOrder salesOrder);
	
	@Select("select * from order_info where id = #{orderId}")
	public OrderInfo getOrderById(@Param("orderId")long orderId);

}
