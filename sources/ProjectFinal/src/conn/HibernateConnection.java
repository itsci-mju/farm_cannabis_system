package conn;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import bean.Customer;
import bean.Executive;
import bean.Harvest;
import bean.Officer;
import bean.Order;
import bean.OrderDetail;
import bean.Payment;
import bean.Planting;
import bean.PlantingProgress;
import bean.Product;
import bean.RequestRegister;
import bean.User;

public class HibernateConnection {
	public static SessionFactory sessionFactory;
	static String url = "jdbc:mysql://localhost:3306/projectfinal?characterEncoding=UTF-8"; 
	static String uname = "root";
	static String pwd = "1234";
	
	public static SessionFactory doHibernateConnection(){
		Properties database = new Properties();
		//database.setProperty("hibernate.hbm2ddl.auto", "create"); //เธซเธฅเธฑเธ�เธ�เธฒเธ�เธชเธฃเน�เธฒเธ�เธ•เธฒเธฃเธฒเธ�เน�เธฅเน�เธงเน�เธซเน�เน€เธญเธฒเธญเธญเธ�
		database.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
		database.setProperty("hibernate.connection.username",uname);
		database.setProperty("hibernate.connection.password",pwd);
		database.setProperty("hibernate.connection.url",url);
		database.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5InnoDBDialect");
		Configuration cfg = new Configuration()
							.setProperties(database)
							.addPackage("bean")
							.addAnnotatedClass(User.class)
							.addAnnotatedClass(RequestRegister.class)
							.addAnnotatedClass(Customer.class)
							.addAnnotatedClass(Officer.class)
							.addAnnotatedClass(Executive.class)
							.addAnnotatedClass(Order.class)
							.addAnnotatedClass(Product.class)
							.addAnnotatedClass(OrderDetail.class)
							.addAnnotatedClass(Payment.class)
							.addAnnotatedClass(Planting.class)
							.addAnnotatedClass(Harvest.class)
							.addAnnotatedClass(PlantingProgress.class);
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
		sessionFactory = cfg.buildSessionFactory(ssrb.build());
		return sessionFactory;
	}
}
