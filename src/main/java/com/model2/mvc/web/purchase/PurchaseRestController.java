package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseRestController() {
		System.out.println(this.getClass());
	}
	
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping(value="json/addPurchase/{prodNo}/{buyerId}", method=RequestMethod.GET)
	public Map addPurchase(@PathVariable int prodNo, @PathVariable String buyerId) throws Exception{
	
		System.out.println("/addPurchaseView");
		
		Map map=new HashMap();
		map.put("product", productService.getProduct(prodNo));
		map.put("user", userService.getUser(buyerId));
		
		return map;
	}
	
	
	@RequestMapping(value="json/addPurchase", method=RequestMethod.POST)
	public Purchase addPurchase(@RequestBody Purchase purchase) throws Exception{
	
		System.out.println("POST /addPurchase");
		
		//productService.getProduct(prodNo);
		//modelAndView.addObject("user", userService.getUser(userId));
		purchaseService.addPurchase(purchase);
		
		return purchase;
	}
		
	
	@RequestMapping(value="json/listPurchase/{buyerId}", method=RequestMethod.GET)	
	public Map listPurchase(@PathVariable String buyerId) throws Exception{
		
		System.out.println("RestController /listPurchase GET");
		
		Search search=new Search();
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String,Object> map = purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}
	
	
	@RequestMapping(value="json/getPurchase/{tranNo}")
	public Purchase getPurchase(@PathVariable int tranNo) throws Exception{
	
		System.out.println("RestController /getPurchase GET");
		
		return purchaseService.getPurchase(tranNo);
	}
	
	
	@RequestMapping(value="json/updatePurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase updatePurchase(@PathVariable int tranNo) throws Exception{
	
		System.out.println("RestController /updatePurchaseView");
		
		return purchaseService.getPurchase(tranNo);
	}
	
	
	@RequestMapping(value="json/updatePurchase", method=RequestMethod.POST)	
	public Purchase updatePurchase(@RequestBody Purchase purchase) throws Exception{
		
		System.out.println("RestController /updatePurchase POST");
		
		purchaseService.updatePurcahse(purchase);
		
		return purchase;
	}
	
	
	@RequestMapping(value="/json/updateTranCodeByProd/{prodNo}/{tranCode}")
	public Purchase updateTranCodeByProd(@PathVariable int prodNo, @PathVariable String tranCode) throws Exception{
		
		System.out.println("RestController /updateTranCodeByProd");
		
		Product product=new Product();
		product.setProdNo(prodNo);
		
		Purchase purchase=new Purchase();
		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);

		purchaseService.updateTranCode(purchase);
			
		return purchase;
	}
	
	
	@RequestMapping(value="/json/updateTranCode/{tranNo}/{tranCode}")
	public Purchase updateTranCode(@PathVariable int tranNo, @PathVariable String tranCode) throws Exception{
		
		System.out.println("RestController /updateTranCode");
		
		Purchase purchase=new Purchase();
		purchase.setTranCode(tranCode);
		purchase.setTranNo(tranNo);
	
		purchaseService.updateTranCode(purchase);
		
		return purchase;
	}
}
