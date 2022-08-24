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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.xdevapi.JsonString;

import bean.Customer;
import bean.Order;
import bean.User;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

@Controller
public class OrderController {
	
	@RequestMapping(value = "/order/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addOrder(@RequestBody Map<String, String> map) {
		String message = "";
		Order order = null;
		try {
			int OrderID = Integer.parseInt(map.get("OrderID"));
			Date orderDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("orderDate"));
			Date receiveDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("receiveDate"));
			String status = map.get("status");
			String username = map.get("customer");
			
			/*JsonObject json = new JsonObject();
			username = (JsonObject) json.get("Customer");
			System.out.println(username);*/
			
			System.out.println(OrderID + " " + orderDate + " " + receiveDate + " " + status + " " + username);
			CustomerManager cm = new CustomerManager();
			Customer customer = cm.getCustomer(username);
			
			order = new Order(OrderID, orderDate, receiveDate, status, customer);
			OrderManager om = new OrderManager();
			message = om.insert_Order(order);
			return new ResponseObj(200, order);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, order);
		}
	}
	
	@RequestMapping(value = "/orders/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_listOrders(@RequestBody String customerid) {
		String message = "";
		List<Order> orders = null;
		try {
			OrderManager om = new OrderManager();
			orders = om.listOrders(customerid);
			
			return new ResponseObj(200, orders);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/orders/list_by_year", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_listByYear(@RequestBody String year) {
		String message = "";
		List<Order> orders = null;
		try {
			OrderManager om = new OrderManager();
			orders = om.listByYear(year);
			
			return new ResponseObj(200, orders);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/orders/alllist", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listAllOrders(HttpServletRequest request) {
		List<Order> orders = null;
		try {
			OrderManager om = new OrderManager();
			orders = om.listAllOrders();
			System.out.println(orders.toString());
			return new ResponseObj(200, orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, "0");
	}
	
	@RequestMapping(value = "/order/get_order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getOrder(@RequestBody String orderid) {
		String message = "";
		Order order = null;
		try {
			System.out.println(orderid);
			OrderManager om = new OrderManager();
			order = om.getOrder(orderid);
			
			System.out.println(order.toString());
			return new ResponseObj(200, order);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/order/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_confirmOrder(@RequestBody String orderid) {
		String message = "";
		try {
			System.out.println(orderid);
			OrderManager om = new OrderManager();
			message = om.confirmOrder(orderid);
			
			System.out.println(message);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/order/update_status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_updateOrderStatus(@RequestBody Map<String, String> map) {
		String message = "";
		Order order = null;
		try {
			int OrderID = Integer.parseInt(map.get("OrderID"));
			String status = map.get("status");
			
			System.out.println(OrderID + " " + status);
			
			Date date = new Date();
			Customer customer = new Customer();
			
			OrderManager om = new OrderManager();
			order = new Order(OrderID,date,date,status,customer);
			message = om.updateOrderStatus(order);
			
			System.out.println(message);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
}
