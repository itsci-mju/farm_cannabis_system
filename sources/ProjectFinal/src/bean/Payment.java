package bean;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="payment")
public class Payment {
	
	@Id
	@Column(name="paymentid")
	private int paymentID;
	
	@Column(name="paydate", nullable=false)
	private Date paydate;
	
	@Column(name="amount", nullable=false)
	private double amount;
	
	@Column(name="imgpayment", nullable=false)
	private String imgPayment;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="orderid", nullable=false)
	private Order order;

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(int paymentID, Date paydate, double amount, String imgPayment, Order order) {
		super();
		this.paymentID = paymentID;
		this.paydate = paydate;
		this.amount = amount;
		this.imgPayment = imgPayment;
		this.order = order;
	}

	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public Date getPaydate() {
		return paydate;
	}

	public void setPaydate(Date paydate) {
		this.paydate = paydate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getImgPayment() {
		return imgPayment;
	}

	public void setImgPayment(String imgPayment) {
		this.imgPayment = imgPayment;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
