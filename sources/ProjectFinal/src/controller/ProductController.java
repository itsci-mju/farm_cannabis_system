package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Order;
import bean.Product;

@Controller
public class ProductController {

	@RequestMapping(value = "/product/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listProducts(HttpServletRequest request) {
		List<Product> products = null;
		try {
			ProductManager pm = new ProductManager();
			products = pm.listAllProducts();
			System.out.println(products.toString());
			return new ResponseObj(200, products);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, products);
	}
	
	@RequestMapping(value = "/product/get_product", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getProduct(@RequestBody String productid) {
		String message = "";
		Product product = null;
		try {
			System.out.println(productid);
			ProductManager om = new ProductManager();
			product = om.getProduct(productid);
			
			System.out.println(product.toString());
			return new ResponseObj(200, product);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
}
