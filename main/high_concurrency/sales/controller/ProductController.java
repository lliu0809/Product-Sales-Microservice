package high_concurrency.sales.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import high_concurrency.sales.redis.ProductKey;
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
    
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    
    @Autowired
    ApplicationContext applicationContext;
    
    
    @RequestMapping(value="/productlist", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, OurUser user) {
        
        model.addAttribute("user", user);
        
        // fetch cache
        String html = redisService.get(ProductKey.getProductList, "", String.class);
        if(!StringUtils.isEmpty(html)) 
            // exist in cache
            return html;
        
        List<ProductVo> productList = productService.listProductVo();
        model.addAttribute("productList", productList);

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext );
        
        // does not exist in cache, manually render data
        html = thymeleafViewResolver.getTemplateEngine().process("product_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            // store template into cache
            redisService.set(ProductKey.getProductList, "", html);
        }
        
        return html;
    }
    
    
    @RequestMapping(value="/productdetail/{productId}",produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, OurUser user, @PathVariable("productId")long productId) {

        model.addAttribute("user", user);
        
        // fetch cache
        String html = redisService.get(ProductKey.getProductDetail, "" + productId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        
        // manually rendering
        model.addAttribute("user", user);
        
        ProductVo product = productService.getProductVoByProductId(productId);
        model.addAttribute("product", product);
        
        
        long start = product.getStartDate().getTime();
        long end = product.getEndDate().getTime();
        long curr = System.currentTimeMillis();
        
        int salesStatus = 0;
        int timeRemaining = 0;
        if(curr < start ) {
            //product sale not yet started, count down
            salesStatus = 0;
            timeRemaining = (int)((start - curr)/1000);
        }
        else  if(curr > end){
            //product sale ended
            salesStatus = 2;
            timeRemaining = -1;
        }
        else {
            //product sale in progress
            salesStatus = 1;
            timeRemaining = 0;
        }
        model.addAttribute("salesStatus", salesStatus);
        model.addAttribute("timeRemaining", timeRemaining);
        
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext );
        html = thymeleafViewResolver.getTemplateEngine().process("procuct_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(ProductKey.getProductDetail, "" + productId, html);
        } 
        return html;
    }
    
}