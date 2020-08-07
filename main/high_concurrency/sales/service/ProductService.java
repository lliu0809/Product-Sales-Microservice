package high_concurrency.sales.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import high_concurrency.sales.dao.ProductDao;
import high_concurrency.sales.domain.SalesProduct;
import high_concurrency.sales.vo.ProductVo;

@Service
public class ProductService {
	
	@Autowired
	static
	ProductDao productDao;
	
	public List<ProductVo> listProductVo(){
		return productDao.listProductVo();
	}

	public ProductVo getProductVoByProductId(long productId) {
		return productDao.getProductVoByProductId(productId);
	}

	public static boolean reduceStock(ProductVo product) {
		SalesProduct sp = new SalesProduct();
		sp.setProductId(product.getId());
		productDao.reduceStock(sp);
	}
	
}
