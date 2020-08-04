package high_concurrency.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.service.OurUserService;


@Controller
@RequestMapping("/products")
public class ProductsController {
	
	@Autowired
	OurUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/product_list")
    public String list(Model model, OurUser user) {
    	model.addAttribute("user", user);
        return "product_list";
    }
}
 