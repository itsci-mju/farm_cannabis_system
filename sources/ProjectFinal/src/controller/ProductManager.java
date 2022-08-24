package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Order;
import bean.Product;
import bean.User;
import conn.HibernateConnection;

public class ProductManager {

	public List<Product> listAllProducts() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Product> products = session.createQuery("From Product").list();
			session.close();
			
			return products;
			
		} catch (Exception e) {
			return null;
		}
	}

	public Product getProduct(String productid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Product product = (Product) session.createQuery("From Product where productid = '" + productid + "'").getSingleResult();
			session.close();
			
			return product;
			
		} catch (Exception e) {
			return null;
		}
	}
}
