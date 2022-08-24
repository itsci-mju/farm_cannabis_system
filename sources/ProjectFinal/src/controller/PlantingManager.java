package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Customer;
import bean.Order;
import bean.Planting;
import bean.User;
import conn.HibernateConnection;

public class PlantingManager {
	
	public List<Planting> listAllPlanting() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Planting> planting = session.createQuery("From Planting").list();
			session.close();
		
			return planting;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Planting> listPlantingByYear(String year) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Planting> planting = session.createQuery("From Planting where YEAR(plantdate) = " + year + "").list();
			session.close();
		
			return planting;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String insert_planting(Planting planting) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.saveOrUpdate(planting);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String update_planting(Planting planting) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.update(planting);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String delete_planting(Planting planting) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.delete(planting);
			t.commit();
			session.close();
			return "successfully delete";
		} catch (Exception e) {
			return "failed to delete student";
		}
	}
	
	public Planting getPlanting(String plantid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			Planting planting = (Planting) session.createQuery("From Planting where plantid = '" + plantid + "'").getSingleResult();
			session.close();
			
			return planting;
			
		} catch (Exception e) {
			return null;
		}
	}
}
