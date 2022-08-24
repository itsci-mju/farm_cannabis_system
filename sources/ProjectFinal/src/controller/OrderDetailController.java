package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Customer;
import bean.Order;
import bean.OrderDetail;
import bean.Product;

@Controller
public class OrderDetailController {
	
	@RequestMapping(value = "/orderdetail/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addOrderDetail(@RequestBody Map<String, String> map) {
		String message = "";
		OrderDetail orderdetail = null;
		try {
			int orderDetailID = Integer.parseInt(map.get("orderDetailID"));
			int qty = Integer.parseInt(map.get("qty"));
			double totalprice = Double.parseDouble(map.get("totalprice"));
			String productid = map.get("product");
			String orderid = map.get("order");
			
			System.out.println(orderDetailID + " " + qty + " " + totalprice + " " + productid + " " + orderid);
			
			OrderManager om = new OrderManager();
			Order order = om.getOrder(orderid);
			
			ProductManager pm = new ProductManager();
			Product product = pm.getProduct(productid);
			
			orderdetail = new OrderDetail(orderDetailID, qty, totalprice, product, order);
			OrderDetailManager dm = new OrderDetailManager();
			message = dm.insert_OrderDetail(orderdetail);
			return new ResponseObj(200, orderdetail);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, orderdetail);
		}
	}
	
	@RequestMapping(value = "/orderdetail/list_by_orderid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_listOrderDetailByOrderID(@RequestBody String orderid) {
		String message = "";
		List<OrderDetail> odd = null;
		try {
			OrderDetailManager odm = new OrderDetailManager();
			odd = odm.listOrderDetailByOrderID(orderid);
			return new ResponseObj(200, odd);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/orderdetail/list_by_year", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_listOrderDetailByYear(@RequestBody String year) {
		String message = "";
		List<OrderDetail> odd = null;
		try {
			OrderDetailManager odm = new OrderDetailManager();
			odd = odm.listOrderDetailByYear(year);
			return new ResponseObj(200, odd);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/orderdetail/alllist", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listAllOrderDetail(HttpServletRequest request) {
		List<OrderDetail> odd = null;
		try {
			OrderDetailManager odm = new OrderDetailManager();
			odd = odm.listAllOrderDetail();
			
			return new ResponseObj(200, odd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, "0");
	}
}
