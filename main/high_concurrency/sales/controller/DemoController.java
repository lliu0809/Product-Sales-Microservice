package high_concurrency.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.redis.UserKey;

import high_concurrency.sales.domain.User;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.result.CodeMsg;
import high_concurrency.sales.result.Result;
import high_concurrency.sales.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	
		@Autowired
		UserService userService;
		
		@Autowired
		RedisService redisService;
		
		
	 	@RequestMapping("/")
	    @ResponseBody
	    String home() {
	        return "Hello World!";
	    }
	 	
	 	
	 	// REST API JSON output 
	 	// {"code": INT, "msg": STRING, "data": T}	
	 	
	 	@RequestMapping("/hello")
	    @ResponseBody
	    public Result<String> hello() {
	 		return Result.success("hello");
	    }
	 	
	 	@RequestMapping("/helloError")
	    @ResponseBody
	    public Result<String> helloError() {
	 		return Result.error(CodeMsg.SERVER_ERROR);
	    }
	 	
	 	// thymeleaf
	 	@RequestMapping("/thymeleaf")
	    public String thymeleaf(Model model) {
	 		model.addAttribute("name", "friend");
	 		return "hello";
	    }
	 	
	 	// database
	 	@RequestMapping("/db/get")
	    @ResponseBody
	 	public Result<User> dbGet(){
	 		User user = userService.getById(1);
	 		return Result.success(user);
	 	} 
	    
	    @RequestMapping("/db/tx")
	    @ResponseBody
	    public Result<Boolean> dbTx() {
	    	userService.tx();
	        return Result.success(true);
	    }
	 	
	    
	    // redis for caching
	    @RequestMapping("/redis/get")
	    @ResponseBody
	    public Result<User> redisGet() {
	    	// get the key we desined
	    	User  user  = redisService.get(UserKey.getById, "" + 1, User.class);
	        return Result.success(user);
	    }
	    
	    @RequestMapping("/redis/set")
	    @ResponseBody
	    public Result<Boolean> redisSet() {
	    	User user  = new User();
	    	user.setId(1);
	    	user.setName("abc");
	    	redisService.set(UserKey.getById, "" + 1, user);
	        return Result.success(true);
	    }

}