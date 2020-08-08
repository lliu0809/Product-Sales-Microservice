package high_concurrency.sales.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import high_concurrency.sales.controller.LoginController;
import high_concurrency.sales.redis.RedisService;
import high_concurrency.sales.result.Result;
import high_concurrency.sales.service.OurUserService;
import high_concurrency.sales.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	OurUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/tologin")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/usrlogin")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
    	log.info(loginVo.toString());
    	//Login
    	userService.login(response, loginVo);
    	return Result.success(true);
    }
}
