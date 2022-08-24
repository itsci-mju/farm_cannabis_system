package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Order;
import bean.Payment;
import bean.User;
import conn.HibernateConnection;

public class PaymentManager {
	public String insert_Payment(Payment payment) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(payment);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public List<Payment> listPayment() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Payment> payment = session.createQuery("From Payment").list();
			session.close();
			
			return payment;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public Payment getPaymentByOrderID(String orderid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Payment payment = (Payment) session.createQuery("From Payment where orderid = '" + orderid + "'").getSingleResult();
			session.close();
			
			return payment;
			
		} catch (Exception e) {
			return null;
		}
	}
}
