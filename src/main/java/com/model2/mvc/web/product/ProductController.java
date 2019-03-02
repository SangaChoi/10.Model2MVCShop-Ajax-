package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	//@RequestMapping("/addProductView.do")
	//public String addProductView() throws Exception {	
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProduct() throws Exception{
		System.out.println("/addProductView");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	//@RequestMapping("/addProduct.do")
	//public String addUser( @ModelAttribute("product") Product product) throws Exception {
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product")Product product, HttpServletRequest request) throws Exception{
		
		System.out.println("/addProduct");
		
		
		/*if(FileUpload.isMultipartContent(request)) {
			String temDir="C:\\workspace\\10.Model2MVCShop(Ajax)\\WebContent\\images\\uploadFiles";
			
			DiskFileUpload fileUpload=new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			
			fileUpload.setSizeMax(1024*1024*10);
			fileUpload.setSizeThreshold(1024*100);
			
			if(request.getContentLength()<fileUpload.getSizeMax()) {
				//Product product=new Product();
				
				StringTokenizer token=null;
				
				List fileItemList=fileUpload.parseRequest(request);
				int Size=fileItemList.size();
				for(int i=0; i<Size; i++) {
					FileItem fileItem=(FileItem) fileItemList.get(i);
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("manuDate")) {
							token=new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String manuDate=token.nextToken()+token.nextToken()+token.nextToken();
							product.setManuDate(manuDate);
						}else if(fileItem.getFieldName().equals("prodName"))
							product.setProdName(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("prodDetail"))
							product.setProdDetail(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("price"))
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
					}else {
						if(fileItem.getSize()>0) {
							int idx=fileItem.getName().lastIndexOf("\\");
							if(idx==-1) {
								idx=fileItem.getName().lastIndexOf("/");
							}
							String fileName=fileItem.getName().substring(idx+1);
							product.setFileName(fileName);
							try {
								File uploadedFile=new File(temDir, fileName);
								fileItem.write(uploadedFile);
							}catch(IOException e) {
								System.out.println(e);
							}
						}else {
							product.setFileName("../../images/empty.GIF");
						}
					}//else
				}//for
				
				productService.addProduct(product);
				
			}else {
				int overSize=(request.getContentLength()/1000000);
				System.out.println("<script>alert ('파일의 크기는 1MB까지입니다. 올리신 파일 용량은 "+overSize+"MB입니다.');");
				System.out.println("history.back();</script>");
			}
		}else {
			System.out.println("인코딩 타입이 multipart/form-data가 아닙니다.");
		}*/

		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	//@RequestMapping("/listProduct.do")
	//public String listProduct(@ModelAttribute("search") Search search,Model model , HttpServletRequest request) throws Exception{
	@RequestMapping(value="listProduct")
	public String listProduct(@ModelAttribute("search") Search search, Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
			
		if(search.getSearchCondition()==null && search.getSearchKeyword()==null) {
			search.setSearchCondition("0");
			search.setSearchKeyword("");
		}
		
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
	
	//@RequestMapping("/updateProductView.do")
	//public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception{
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct(@RequestParam("prodNo") int prodNo, Model model) throws Exception{
		System.out.println("/updateProductView");
		
		Product product=productService.getProduct(prodNo);
		model.addAttribute("product",product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	//@RequestMapping("/updateProduct.do")
	//public String updateProduct(@ModelAttribute("product")Product product, Model model , HttpSession session) throws Exception{
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product")Product product, Model model , HttpSession session, HttpServletRequest request) throws Exception{
		System.out.println("/updateProduct");
				
		productService.updateProduct(product);
		
		int prodNo=product.getProdNo();
		
		return "redirect:/product/getProduct?prodNo="+prodNo+"&menu=manage";
	}
	
	//@RequestMapping("/getProduct.do")
	//public String getProduct(@RequestParam("prodNo") int prodNo, Model model) throws Exception{
	@RequestMapping(value="getProduct")	
	public String getProduct(@RequestParam("prodNo") String prodNo, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("/getProduct.do");
		
		Product product=productService.getProduct(Integer.parseInt(prodNo));
		
		model.addAttribute("product",product);
		
/*		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0) {
		  for(int i=0;i<cookies.length;i++) {	
			  Cookie cookie = cookies[i];
			if(cookie.getName().equals("history")) {
				cookie.setValue(cookie.getValue()+","+prodNo);
				cookie.setMaxAge(60*60);
				response.addCookie(cookie);
			}else{
			cookie = new Cookie("history",prodNo);
			cookie.setMaxAge(60*60);
			cookie.setPath("/");
			response.addCookie(cookie);		
			}
		  }
		}
*/
		String history=null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0) {
			  for(int i=0;i<cookies.length;i++) {	
				  Cookie cookie = cookies[i];
				if(cookie.getName().equals("history")) {
					history=cookie.getValue();
				}
			  }
		}
		Cookie cookie=new Cookie("history",history+","+prodNo);
		cookie.setPath("/");
		response.addCookie(cookie);	
		
		
		return "forward:/product/getProduct.jsp";
	}

}
