package high_concurrency.sales.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import high_concurrency.sales.domain.OurUser;

@Mapper
public interface OurUserDao {
	
	@Select("select * from our_user where id = #{id}")
	public OurUser getById(@Param("id")long id);
	
	@Update("update our_user set password = #{password} where id = #{id}")
	public void update(OurUser toBeUpdate);
}