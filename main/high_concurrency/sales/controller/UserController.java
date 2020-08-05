package high_concurrency.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import high_concurrency.sales.domain.OurUser;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.result.Result;
import high_concurrency.sales.service.OurUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	OurUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/info")
    @ResponseBody
    public Result<OurUser> info(Model model, OurUser user) {
        return Result.success(user);
    }
    
}
