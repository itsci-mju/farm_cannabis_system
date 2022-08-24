package controller;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Customer;
import bean.Order;
import bean.RequestRegister;
import bean.User;
import conn.HibernateConnection;

public class CustomerManager {
	
	public String insert_customer(Customer customer) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(customer);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}

	public Customer getCustomer(String username) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Customer customer = (Customer) session.createQuery("From Customer where username = '" + username + "'").getSingleResult();
			session.close();
			
			return customer;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Customer> listAllCustomers() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Customer> customers = session.createQuery("FROM Customer").list();
			session.close();
			
			return customers;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public Customer getCustomerByID(String id) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Customer customer = (Customer) session.createQuery("From Customer where CustomerID = '" + id + "'").getSingleResult();
			session.close();
			
			return customer;
			
		} catch (Exception e) {
			return null;
		}
	}
}