package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import bean.Customer;
import bean.RequestRegister;
import bean.User;
import service.Base64ToMultipartFile;
import service.FilesStorageService;

@Controller
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addCustomer(@RequestBody Map<String, String> map) {
		String message = "";
		Customer customer = null;
		try {
			int customerid = Integer.parseInt((map.get("CustomerID")));
			String company = map.get("company");
			String address = map.get("address");
			String mobileNo = map.get("mobileNo");
			String imgCertificate = map.get("imgCertificate");
			String imgIDCard = map.get("imgIDCard");
			String reqregid = map.get("reqreg");
			String username = map.get("user");
			
			System.out.println(customerid + " " + company + " " + address + " "
					+ mobileNo + " " + imgCertificate + " " + imgIDCard + " "
					+ reqregid + " " + username);
			
			RequestRegisterManager rrm = new RequestRegisterManager();
			RequestRegister reqreg = rrm.getRequestRegister(reqregid);
			
			UserManager um = new UserManager();
			User user = um.getUserByUsername(username);
			
			System.out.println(user.getUsername() + " " + user.getFullname() + " " + user.getPassword());
			
			customer = new Customer(customerid, company, address, mobileNo, imgCertificate, imgIDCard, reqreg, user);
			CustomerManager cm = new CustomerManager();
			
			System.out.println(customer.getCustomerID() + " " + customer.getCompany() + " "
					+ "" + customer.getAddress() + " " + customer.getMobileNo() + " " + customer.getImgIDCard() + " "
					+ "" + customer.getImgCertificate() + " " + customer.getReqreg().getReqregid() + " " + customer.getUser().getFullname());
			
			message = cm.insert_customer(customer);
			if(!imgCertificate.equals("-")) {
				uploadFile(imgCertificate);
			}
			if(!imgIDCard.equals("-")) {
				uploadFile(imgIDCard);
			}
			
			return new ResponseObj(200, customer);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, customer);
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
	
	@RequestMapping(value = "/customer/get_customer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getCustomer(@RequestBody String username) {
		String message = "";
		Customer customer = null;
		try {
			System.out.println(username);
			CustomerManager cm = new CustomerManager();
			customer = cm.getCustomer(username);
			
			System.out.println(customer.toString());
			return new ResponseObj(200, customer);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/customer/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listCustomers(HttpServletRequest request) {
		List<Customer> customers = null;
		try {
			CustomerManager cm = new CustomerManager();
			customers = cm.listAllCustomers();
			System.out.println(customers.toString());
			return new ResponseObj(200, customers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, customers);
	}
	
	@RequestMapping(value = "/customer/get_customerbyid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getCustomerByID(@RequestBody String id) {
		String message = "";
		Customer customer = null;
		try {
			System.out.println(id);
			CustomerManager cm = new CustomerManager();
			customer = cm.getCustomerByID(id);
			
			System.out.println(customer.toString());
			return new ResponseObj(200, customer);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
}