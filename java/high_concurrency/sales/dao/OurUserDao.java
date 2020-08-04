package high_concurrency.sales.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import high_concurrency.sales.domain.OurUser;

@Mapper
public interface OurUserDao {
	
	@Select("select * from our_user where id = #{id}")
	public OurUser getById(@Param("id")long id);
}