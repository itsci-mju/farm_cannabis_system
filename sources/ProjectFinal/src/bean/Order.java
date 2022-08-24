package bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@Column(name="orderid")
	private int OrderID;
	
	@Column(name="orderdate", nullable=false)
	private Date orderDate;
	
	@Column(name="receivedate", nullable=false)
	private Date receiveDate;
	
	@Column(name="status", nullable=false)
	private String status;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customerid", nullable=false)
	private Customer customer;

	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(int orderID, Date orderDate, Date receiveDate, String status, Customer customer) {
		super();
		OrderID = orderID;
		this.orderDate = orderDate;
		this.receiveDate = receiveDate;
		this.status = status;
		this.customer = customer;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
