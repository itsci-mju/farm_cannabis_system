package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import conn.HibernateConnection;
import bean.Customer;
import bean.Executive;
import bean.Officer;
import bean.User;

public class UserManager {
	private static String SALT = "123456";

	public String insertUser(User user) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(user);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String editUser(User user) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.update(user);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}

	public String deleteUser(User user) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.delete(user);
			t.commit();
			session.close();
			return "successfully delete";
		} catch (Exception e) {
			return "failed to delete student";
		}
	}
	
	public String doHibernateLogin(User login) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<User> user = session.createQuery("From User where username = '" + login.getUsername() + "'").list();
			session.close();
			
			if (user.size() == 1) {
				//String password = PasswordUtil.getInstance().createPassword(user.get(0).getPassword(), SALT);
				if (login.getPassword().equals(user.get(0).getPassword())) {
					return "login success";
				} else {
					return "username or password does't match";
				}
			} else {
				return "username or password does't match";
			}
		} catch (Exception e) {
			return "Please try again...";
		}
	}
	
	public User getUser(User login) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			User users = (User) session.createQuery("From User where username = '" + login.getUsername() + "'").getSingleResult();
			session.close();
			
			return users;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public User getUserByUsername(String username) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			User user = (User) session.createQuery("From User where username = '" + username + "'").getSingleResult();
			session.close();
			
			return user;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getUserType(String username) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			String type = "";
			List<Customer> customer = session.createQuery("From Customer where username = '" + username + "'").list();
			List<Officer> officer = session.createQuery("From Officer where username = '" + username + "'").list();
			List<Executive> executive = session.createQuery("From Executive where username = '" + username + "'").list();
			session.close();
			
			if(customer.size() > 0) {
				type = "3";
			}else if(officer.size() > 0) {
				type = "2";
			}else if(executive.size() > 0) {
				type = "1";
			}else {
				type = "0";
			}
			System.out.println("type " + type);
			
			return type;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<User> listAllUsers() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<User> users = session.createQuery("From User").list();
			session.close();
			
			return users;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public User getUserProfile(String id) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<User> users = session.createQuery("From User where id = " + id).list();
			session.close();
			
			return users.get(0);
			
		} catch (Exception e) {
			return null;
		}
	}
}
