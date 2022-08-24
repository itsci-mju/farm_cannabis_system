package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Harvest;
import bean.PlantingProgress;
import conn.HibernateConnection;

public class PlantingProgressManager {
	
	public List<PlantingProgress> listAllPlantingProgress() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<PlantingProgress> pp = session.createQuery("From PlantingProgress").list();
			session.close();
		
			return pp;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<PlantingProgress> listPlantingProgress(String plantid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<PlantingProgress> pp = session.createQuery("From PlantingProgress where plantid = '" + plantid + "'").list();
			session.close();
		
			return pp;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String insert_plantingProgress(PlantingProgress pp) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.save(pp);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String update_plantingProgress(PlantingProgress pp) {
		String pattern = "yyyy-MM-dd";
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			String progressDate = sdf.format(pp.getProgressDate());
			
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			System.out.println(pp.getProgressID() + " " + pp.getProgressDate().toString() + " " + pp.getImgProgress() + " "
					+ "" + pp.getNote() + " " + pp.getPlanting().getPlantID());
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE PlantingProgress "
					+ "SET progressDate = '" + progressDate + "', "
					+ "imgProgress = '" + pp.getImgProgress() + "', "
					+ "note = '" + pp.getNote() + "' "
					+ "where progressID = '" + pp.getProgressID() + "' "
					+ "and plantid = '" + pp.getPlanting().getPlantID() + "'").executeUpdate();
			session.update(pp);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String delete_plantingProgress(PlantingProgress pp) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("DELETE FROM PlantingProgress where progressID = '" + pp.getProgressID() + "' "
					+ "and plantid = '" + pp.getPlanting().getPlantID() + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully delete";
		} catch (Exception e) {
			return "failed to delete student";
		}
	}
}
