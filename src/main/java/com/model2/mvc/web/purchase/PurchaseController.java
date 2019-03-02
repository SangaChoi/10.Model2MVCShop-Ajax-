package com.model2.mvc.web.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	//@RequestMapping("/addPurchaseView.do")
	//public ModelAndView addPurchaseView(@RequestParam("prodNo") int prodNo) throws Exception{
	@RequestMapping(value="addPurchase", method=RequestMethod.GET)
	public ModelAndView addPurchase(@RequestParam("prodNo") int prodNo) throws Exception{
	
		System.out.println("/addPurchaseView");
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("product", productService.getProduct(prodNo));
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		
		return modelAndView;
	}
	
	//@RequestMapping("/addPurchase.do")
	//public ModelAndView addPurchase(@RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId, @ModelAttribute("purchase")Purchase purchase) throws Exception{
	@RequestMapping(value="addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId, @ModelAttribute("purchase")Purchase purchase) throws Exception{
	
		System.out.println("/addPurchase");
		
		purchase.setBuyer(userService.getUser(userId));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setTranCode("1");
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("product",productService.getProduct(prodNo));
		//modelAndView.addObject("user", userService.getUser(userId));
		purchaseService.addPurchase(purchase);
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		
		return modelAndView;
	}
	

	//@RequestMapping("/listPurchase.do")
	//public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpServletRequest request) throws Exception{
	@RequestMapping(value="listPurchase")	
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpServletRequest request) throws Exception{
		
		System.out.println("/listPurchase");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String,Object> map = purchaseService.getPurchaseList(search, request.getParameter("buyerId"));
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		
		return modelAndView;
	}

	//@RequestMapping("getPurchase.do")
	@RequestMapping(value="getPurchase")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
	
		System.out.println("/getPurchase");
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("purchase", purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		
		return modelAndView;
	}

	//@RequestMapping("/updatePurchaseView.do")
	//public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception{
	@RequestMapping(value="updatePurchase", method=RequestMethod.GET)
	public ModelAndView updatePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
	
		System.out.println("/updatePurchaseView");
		
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("purchase", purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		
		return modelAndView;
	}
	
	//@RequestMapping("updatePurchase.do")
	//public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo")int tranNo) throws Exception{
	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)	
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo")int tranNo) throws Exception{
		
		System.out.println("/updatePurchase");
		System.out.println("업데이트 전 : "+purchase);
		
		purchaseService.updatePurcahse(purchase);
		
		System.out.println("업데이트 후 : "+purchase);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/getPurchase?tranNo="+tranNo);
		
		return modelAndView;
	}
	
	//@RequestMapping("updateTranCodeByProd.do")
	@RequestMapping(value="updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@RequestParam("prodNo") int prodNo, @RequestParam("tranCode") String tranCode) throws Exception{
		
		System.out.println("/updateTranCodeByProd.do");
		
		Product product=new Product();
		product.setProdNo(prodNo);
		
		Purchase purchase=new Purchase();
		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);
		
		ModelAndView modelAndView=new ModelAndView();
		purchaseService.updateTranCode(purchase);
		modelAndView.setViewName("redirect:/product/listProduct?menu=manage");
		
		return modelAndView;
	}
	
	//@RequestMapping("updateTranCode.do")
	@RequestMapping(value="updateTranCode")
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase, @RequestParam("tranCode") String tranCode,
			@RequestParam("buyerId") String buyerId) throws Exception{
		
		purchase.setTranCode(tranCode);
		
		ModelAndView modelAndView=new ModelAndView();
		purchaseService.updateTranCode(purchase);
		modelAndView.setViewName("redirect:/purchase/listPurchase?buyerId="+buyerId);
		
		return modelAndView;
	}
	
	
	
}
