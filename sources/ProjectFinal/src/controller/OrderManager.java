package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Customer;
import bean.Order;
import bean.User;
import conn.HibernateConnection;

public class OrderManager {
	
	public String insert_Order(Order order) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(order);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public List<Order> listOrders(String customerid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Order> order = session.createQuery("From Order where CustomerID = '" + customerid + "'").list();
			session.close();
			
			return order;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Order> listByYear(String year) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Order> order = session.createQuery("From Order where YEAR(orderdate) = " + year + "").list();
			session.close();
			
			return order;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Order> listAllOrders() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Order> orders = session.createQuery("FROM Order").list();
			session.close();
			
			return orders;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public Order getOrder(String orderid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Order order = (Order) session.createQuery("From Order where orderid = '" + orderid + "'").getSingleResult();
			session.close();
			
			return order;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String confirmOrder(String orderid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE Order SET status = 'ยืนยันแล้ว' "
					+ "where orderid = '" + orderid + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully updated";
		} catch (Exception e) {
			return "failed to updated student";
		}
	}
	
	public String updateOrderStatus(Order order) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE Order SET status = '" + order.getStatus() +"' "
					+ "where orderid = '" + order.getOrderID() + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully updated";
		} catch (Exception e) {
			return "failed to updated student";
		}
	}
}
