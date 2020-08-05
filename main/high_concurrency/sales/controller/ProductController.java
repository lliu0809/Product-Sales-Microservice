package high_concurrency.sales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.service.ProductService;
import high_concurrency.sales.service.OurUserService;
import high_concurrency.sales.vo.ProductVo;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	OurUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	ProductService productService;
	
    @RequestMapping("/product_list")
    public String list(Model model,OurUser user) {
    	model.addAttribute("user", user);
    	List<ProductVo> productList = productService.listProductVo();
    	model.addAttribute("productList", productList);
        return "product_list";
    }
    
    @RequestMapping("/product_detail/{productId}")
    public String detail(Model model,OurUser user,
    		@PathVariable("productId")long productId) {
    	model.addAttribute("user", user);
    	
    	ProductVo product = productService.getProductVoByProductId(productId);
    	model.addAttribute("product", product);
    	

    	long start = product.getStartDate().getTime();
    	long end = product.getEndDate().getTime();
    	long curr = System.currentTimeMillis();
    	
    	int salesStatus = 0;  // 0 for not begin, 1 for in process, 2 for ended
    	int remainSeconds = 0;
    	if(curr < start ) {
    		//product sale not yet started, count down
    		salesStatus = 0;
    		remainSeconds = (int)((start - curr)/1000);
    	}
    	else  if(curr > end){
    		//product sale ended
    		salesStatus = 2;
    		remainSeconds = -1;
    	}
    	else {
    		//product sale in progress
    		salesStatus = 1;
    		remainSeconds = 0;
    	}
    	model.addAttribute("salesStatus", salesStatus);
    	model.addAttribute("remainSeconds", remainSeconds);
        return "product_detail";
    }
}