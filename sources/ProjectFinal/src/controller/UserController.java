package controller;

import java.util.HashMap;
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
import bean.Product;
import bean.User;

@Controller
public class UserController {
	private static String SALT = "123456";
	
	@RequestMapping(value = "/user/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addUser(@RequestBody Map<String, String> map) {
		String message = "";
		User user = null;
		try {
			String fullname = map.get("fullname");
			String username = map.get("username");
			String password = map.get("password");
			//password = PasswordUtil.getInstance().createPassword(password, SALT);
			
			System.out.println(fullname + " " + username + " " + password);
			user = new User(fullname, username, password);
			UserManager rm = new UserManager();
			message = rm.insertUser(user);
			return new ResponseObj(200, user);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, user);
		}
	}
	
	@RequestMapping(value = "/user/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_editprofile(@RequestBody Map<String, String> map) {
		String message = "";
		User user = null;
		try {
			String fullname = map.get("fullname");
			String username = map.get("username");
			String password = map.get("password");
			password = PasswordUtil.getInstance().createPassword(password, SALT);
			
			System.out.println(fullname + " " + username + " " + password);
			user = new User(fullname, username, password);
			UserManager rm = new UserManager();
			message = rm.editUser(user);
			return new ResponseObj(200, user);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, user);
		}
	}
	
	@RequestMapping(value = "/user/get_user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getUser(@RequestBody Map<String, String> map) {
		String message = "";
		User user = null;
		try {
			String username = map.get("username");
			String password = map.get("password");
			
			System.out.println(username + " " + password);
			user = new User("", username, password);
			UserManager rm = new UserManager();
			user = rm.getUser(user);
			
			System.out.println(user.toString());
			return new ResponseObj(200, user);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/user/check_duplicate_username", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_checkDuplicateUsername(@RequestBody String username) {
		String message = "";
		User user = null;
		try {
			System.out.println(username);
			UserManager um = new UserManager();
			user = um.checkDuplicateUsername(username);
			
			System.out.println(user.toString());
			return new ResponseObj(200, "duplicate");
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(200, "0");
		}
	}
	
	@RequestMapping(value = "/user/get_user_type", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getUserType(@RequestBody String username) {
		String message = "";
		String type = "";
		try {
			UserManager um = new UserManager();
			type = um.getUserType(username);
			
			return new ResponseObj(200, type);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/user/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listUsers(HttpServletRequest request) {
		List<User> users = null;
		try {
			UserManager rm = new UserManager();
			users = rm.listAllUsers();
			System.out.println(users.toString());
			return new ResponseObj(200, users);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, users);
	}
	
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_login(@RequestBody Map<String, String> map) {
		String message = "";
		User user = null;
		try {
			String username = map.get("username");
			String password = map.get("password");
			
			System.out.println(username + " " + password);
			user = new User("", username, password);
			UserManager rm = new UserManager();
			message = rm.doHibernateLogin(user);
			if ("login success".equals(message)) {
				return new ResponseObj(200, "1");
			}else {
				return new ResponseObj(200, "0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/user/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_delete(@RequestBody Map<String, String> map) {
		String message = "";
		User user = null;
		try {
			String id = map.get("id");
			
			UserManager rm = new UserManager();
			user = rm.getUserProfile(id);
			message = rm.deleteUser(user);
			if ("successfully delete".equals(message)) {
				return new ResponseObj(200, "1");
			}else {
				return new ResponseObj(200, "0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/user/getprofile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getProfile(@RequestBody Map<String, String> map) {
		User user = null;
		try {
			String id = map.get("id");
			
			UserManager rm = new UserManager();
			user = rm.getUserProfile(id);
			
			if (user != null) {
				return new ResponseObj(200, user);
			}else {
				return new ResponseObj(200, null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObj(500, null);
		}
	}

	

}
