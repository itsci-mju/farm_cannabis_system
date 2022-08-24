package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
import bean.Payment;
import bean.User;

@Controller
public class PaymentController {
	
	@RequestMapping(value = "/payment/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addPayment(@RequestBody Map<String, String> map) {
		String message = "";
		Payment payment = null;
		try {
			int paymentID = Integer.parseInt(map.get("paymentID"));
			Date paydate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("paydate"));
			double amount = Double.parseDouble(map.get("amount"));
			String imgpayment = map.get("imgPayment");
			String orderid = map.get("order");
			
			System.out.println(paymentID + " " + paydate + " " + amount + " " + imgpayment + " " + orderid);
			
			OrderManager om = new OrderManager();
			Order order = om.getOrder(orderid);
			
			payment = new Payment(paymentID, paydate, amount, imgpayment, order);
			PaymentManager pm = new PaymentManager();
			message = pm.insert_Payment(payment);
			if(!imgpayment.equals("-")) {
				uploadFile(imgpayment);
			}
			
			return new ResponseObj(200, payment);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, payment);
		}
	}
	
	public Boolean uploadFile(String base64) {
		 String root = "/uploads";
		 final String[] base64Array = base64.split(",");
	     String dataUir, data;
	     //Build according to the specific file you represent
        dataUir = "data:image/jpg;base64";
        data = base64Array[0];
        byte[] decoded = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        try (FileOutputStream stream = new FileOutputStream(root)) {
       	    stream.write(decoded);
       	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     return true;
	 }
	
	@RequestMapping(value = "/payment/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listPayment(HttpServletRequest request) {
		List<Payment> payment = null;
		try {
			PaymentManager pm = new PaymentManager();
			payment = pm.listPayment();
			System.out.println(payment.toString());
			return new ResponseObj(200, payment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, payment);
	}
	
	@RequestMapping(value = "/payment/get_payment_by_orderid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getPaymentByOrderID(@RequestBody String orderid) {
		String message = "";
		Payment payment = null;
		try {
			System.out.println(orderid);
			PaymentManager pm = new PaymentManager();
			payment = pm.getPaymentByOrderID(orderid);
			
			return new ResponseObj(200, payment);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, payment);
		}
	}

}
