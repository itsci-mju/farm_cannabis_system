package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Product;
import bean.RequestRegister;
import bean.User;

@Controller
public class RequestRegisterController {
	
	@RequestMapping(value = "/requestregister/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addRegister(@RequestBody Map<String, String> map) {
		String message = "";
		RequestRegister reqreg = null;
		try {
			int reqregid = Integer.parseInt((map.get("reqregid")));
			String persontype = map.get("persontype");
			String status = map.get("status");
			
			System.out.println(reqregid + " " + persontype + " " + status);
			reqreg = new RequestRegister(reqregid, persontype, status);
			RequestRegisterManager rrm = new RequestRegisterManager();
			message = rrm.insert_register(reqreg);
			return new ResponseObj(200, reqreg);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, reqreg);
		}
	}

	@RequestMapping(value = "/requestregister/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listRequestsRegister(HttpServletRequest request) {
		List<RequestRegister> requestsregister = null;
		try {
			RequestRegisterManager rrm = new RequestRegisterManager();
			requestsregister = rrm.listAllRequestsRegister();
			System.out.println(requestsregister.toString());
			return new ResponseObj(200, requestsregister);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, requestsregister);
	}
	
	@RequestMapping(value = "/requestregister/get_requestregister", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getRequestRegister(@RequestBody String id) {
		String message = "";
		RequestRegister requestregister = null;
		try {
			System.out.println(id);
			RequestRegisterManager rrm = new RequestRegisterManager();
			requestregister = rrm.getRequestRegister(id);
			
			System.out.println(requestregister.toString());
			return new ResponseObj(200, requestregister);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/requestregister/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_confirmRequestRegister(@RequestBody String registerid) {
		String message = "";
		RequestRegister requestregister = null;
		try {
			System.out.println(registerid);
			RequestRegisterManager rrm = new RequestRegisterManager();
			message = rrm.confirmRequestRegister(registerid);
			
			System.out.println(message);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/requestregister/deny", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_denyRequestRegister(@RequestBody String registerid) {
		String message = "";
		RequestRegister requestregister = null;
		try {
			System.out.println(registerid);
			RequestRegisterManager rrm = new RequestRegisterManager();
			message = rrm.denyRequestRegister(registerid);
			
			System.out.println(message);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
}
