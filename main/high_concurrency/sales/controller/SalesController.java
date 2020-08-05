package high_concurrency.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import high_concurrency.sales.domain.SalesOrder;
import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.domain.OrderInfo;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.result.CodeMsg;
import high_concurrency.sales.service.ProductService;
import high_concurrency.sales.service.SalesService;
import high_concurrency.sales.service.OurUserService;
import high_concurrency.sales.service.OrderService;
import high_concurrency.sales.vo.ProductVo;

@Controller
@RequestMapping("/sales")
public class SalesController {

	@Autowired
	OurUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SalesService salesService;
	
    @RequestMapping("/sales")
    public String list(Model model, OurUser user, @RequestParam("productId")long productId) {
    	model.addAttribute("user", user);
    	if(user == null)
    		// login first to begin shopping
    		return "login";
    	
    	// check product remaining in warehouse
    	ProductVo product = productService.getProductVoByProductId(productId);
    	int stock = product.getStockCount();
    	if(stock <= 0) {
    		// if no remaining products
    		model.addAttribute("errmsg", CodeMsg.NO_PRODUCT.getMsg());
    		return "product_fail";
    	}

    	SalesOrder order = orderService.getSalesOrderByUserIdProductId(user.getId(), productId);
    	if(order != null) {
    		model.addAttribute("errmsg", CodeMsg.REPEAT_SALES.getMsg());
    		return "product_fail";
    	}
    	
    	// decrement stock number and make order
    	OrderInfo orderInfo = salesService.order(user, product);
    	model.addAttribute("orderInfo", orderInfo);
    	model.addAttribute("product", product);
        return "order_detail";
    }
}