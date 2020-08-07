package high_concurrency.sales.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import high_concurrency.sales.rabbitmq.MQSender;
import high_concurrency.sales.rabbitmq.MQMessage;
import high_concurrency.sales.redis.ProductKey;
import high_concurrency.sales.redis.SalesKey;
import high_concurrency.sales.redis.OrderKey;
import high_concurrency.sales.result.Result;
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
public class SalesController implements InitializingBean {

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
    
    @Autowired
    MQSender sender;
    
    
    // if pre-stored stock count is already less than 0, there's no need to query for Redis
    // Therefore, use a Map to keep track
    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();
    
    
    // when the system is initialized, store stock count into Redis
    public void afterPropertiesSet() throws Exception {
        List<ProductVo> productList = productService.listProductVo();
        if(productList == null) {
            return;
        }
        for(ProductVo product : productList) {
            redisService.set(ProductKey.getSalesProductStock, "" + product.getId(), product.getStockCount());
            localOverMap.put(product.getId(), false);
        }
    }
    
    
    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<ProductVo> productList = productService.listProductVo();
        for(ProductVo product : productList) {
            product.setStockCount(10);
            redisService.set(ProductKey.getSalesProductStock, "" + product.getId(), 10);
            localOverMap.put(product.getId(), false);
        }
        redisService.delete(OrderKey.getSalesOrderByUidGid);
        redisService.delete(SalesKey.isProductOver);
        salesService.reset(productList);
        return Result.success(true);
    }
    
    
    @RequestMapping("/sales")
    public Result<Integer> sales(Model model, OurUser user, @RequestParam("productId")long productId) {
        model.addAttribute("user", user);
        if(user == null)
            // login first to begin shopping
            return "login";
        
        // mark in redis
        boolean event_end = localOverMap.get(productId);
        if(event_end) {
            return Result.error(CodeMsg.EVENT_OVER);
        }
        // decrease in redis
        long stock = redisService.decr(ProductKey.getSalesProductStock, "" + productId);
        if(stock < 0) {
            localOverMap.put(productId, true);
            return Result.error(CodeMsg.EVENT_OVER);
        }
        // order placed successfully?
        SalesOrder order = orderService.getSalesOrderByUserIdProductId(user.getId(), productId);
        if(order != null) {
            return Result.error(CodeMsg.MULTIPLE_ORDER);
        }
        // push into queue
        MQMessage mm = new MQMessage();
        mm.setUser(user);
        mm.setProductId(productId);
        sender.sendSalesMessage(mm);
        return Result.success(0);//排队中
        
        
    }
    
    
    
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> salesResult(Model model, OurUser user, @RequestParam("productId")long productId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = salesService.getSalesResult(user.getId(), productId);
        return Result.success(result);
    }
}