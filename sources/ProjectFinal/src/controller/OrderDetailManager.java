package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Customer;
import bean.Order;
import bean.OrderDetail;
import conn.HibernateConnection;

public class OrderDetailManager {
	
	public String insert_OrderDetail(OrderDetail orderdetail) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.save(orderdetail);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}

	public List<OrderDetail> listOrderDetailByOrderID(String orderid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<OrderDetail> odd = session.createQuery("FROM OrderDetail where OrderID = '" + orderid + "'").list();
			session.close();
			
			return odd;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<OrderDetail> listOrderDetailByYear(String year) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			List<OrderDetail> odd = session.createQuery("FROM OrderDetail where OrderID IN (SELECT OrderID "
					+ "FROM Order where YEAR(orderdate) = " + year + ")").list();
			session.close();
			
			return odd;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<OrderDetail> listAllOrderDetail() {
		List<OrderDetail> list = null;
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			List<OrderDetail> od  = session.createQuery("FROM OrderDetail").list();
			
			session.close();
			
			return od;
			
		} catch (Exception e) {
			return null;
		}
	}
}
