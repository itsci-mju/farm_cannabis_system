package controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import bean.Harvest;
import bean.Order;
import bean.Planting;
import conn.HibernateConnection;

public class HarvestManager {
	
	public List<Harvest> listAllHarvest() {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Harvest> harvest = session.createQuery("From Harvest").list();
			session.close();
		
			return harvest;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Harvest> listHarvest(String plantid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Harvest> harvest = session.createQuery("From Harvest where plantid = '" + plantid + "'").list();
			session.close();
		
			return harvest;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Harvest> listHarvestByYear(String year) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Harvest> harvest = session.createQuery("From Harvest where plantID IN (SELECT plantID "
					+ "From Planting where YEAR(plantdate) = " + year + ")").list();
			session.close();
		
			return harvest;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Harvest> listDetailHarvest(String harvestid, String plantid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Harvest> harvest = session.createQuery("From Harvest where harvestid = '" + harvestid + "' "
					+ "and plantid = '" + plantid + "'").list();
			session.close();
		
			return harvest;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Harvest> listHarvestGroupByHarvestID(String plantid) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
			Session session = sessionFactory.openSession();
			
			session.beginTransaction();
			List<Harvest> harvest = session.createQuery("From Harvest where plantid = '" + plantid + "' group by harvestid").list();
			session.close();
		
			return harvest;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String insert_harvest(Harvest harvest) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.save(harvest);
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String update_harvest(Harvest harvest) {
		String pattern = "yyyy-MM-dd";
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			String harvestDate = sdf.format(harvest.getHarvestDate());
			
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();
				
			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("UPDATE Harvest "
					+ "SET harvestDate = '" + harvestDate + "', "
					+ "qty = '" + harvest.getQty() + "', "
					+ "unit = '" + harvest.getUnit() + "', "
					+ "note = '" + harvest.getNote() + "' "
					+ "where HarvestID = '" + harvest.getHarvestID() + "' "
					+ "and partName = '" + harvest.getPartName() + "' "
					+ "and plantid = '" + harvest.getPlanting().getPlantID() + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully saved";
		} catch (Exception e) {
			return "failed to save student";
		}
	}
	
	public String delete_harvest(Harvest harvest) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("DELETE FROM Harvest where HarvestID = '" + harvest.getHarvestID() + "' "
					+ "and plantid = '" + harvest.getPlanting().getPlantID() + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully delete";
		} catch (Exception e) {
			return "failed to delete student";
		}
	}
	
	public String delete_Detailharvest(Harvest harvest) {
		try {
			SessionFactory sessionFactory = HibernateConnection.doHibernateConnection();

			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			session.createQuery("DELETE FROM Harvest where HarvestID = '" + harvest.getHarvestID() + "' "
					+ "and partName = '" + harvest.getPartName() + "' "
					+ "and plantid = '" + harvest.getPlanting().getPlantID() + "'").executeUpdate();
			t.commit();
			session.close();
			return "successfully delete";
		} catch (Exception e) {
			return "failed to delete student";
		}
	}
}
