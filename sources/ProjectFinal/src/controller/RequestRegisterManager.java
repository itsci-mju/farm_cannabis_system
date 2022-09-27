package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Customer;
import bean.RequestRegister;
import bean.User;
import conn.HibernateConnection;


public class RequestRegisterManager {
	
	public String insert_register(RequestRegister reqreg) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(reqreg);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}

	public List<RequestRegister> listAllRequestsRegister() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<RequestRegister> requestsregister = session.createQuery("FROM RequestRegister").list();
			session.close();
			
			return requestsregister;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public RequestRegister getRequestRegister(String id) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			RequestRegister reqreg = (RequestRegister) session.createQuery("From RequestRegister where reqregid = '" + id + "'").getSingleResult();
			session.close();
			
			return reqreg;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String confirmRequestRegister(String registerid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE RequestRegister SET status = 'ยืนยันแล้ว' "
					+ "where reqregid = '" + registerid + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully updated";
		} catch (Exception e) {
			return "failed to updated student";
		}
	}
	
	public String denyRequestRegister(String registerid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE RequestRegister SET status = 'ถูกปฏิเสธ' "
					+ "where reqregid = '" + registerid + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully updated";
		} catch (Exception e) {
			return "failed to updated student";
		}
	}
}
