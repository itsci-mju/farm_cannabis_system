package bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="orderdetails")
public class OrderDetail implements Serializable{
	
	@Id
	@Column(name="orderdetailid")
	private int orderDetailID;
	
	@Column(name="qty", nullable=false)
	private int qty;
	
	@Column(name="totalprice", nullable=false)
	private double totalprice;
	
	@ManyToOne
    @JoinColumn(name="productid", nullable=false)
	private Product product;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="orderid", nullable=false)
	private Order order;

	public OrderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderDetail(int orderDetailID, int qty, double totalprice, Product product, Order order) {
		super();
		this.orderDetailID = orderDetailID;
		this.qty = qty;
		this.totalprice = totalprice;
		this.product = product;
		this.order = order;
	}

	public int getOrderDetailID() {
		return orderDetailID;
	}

	public void setOrderDetailID(int orderDetailID) {
		this.orderDetailID = orderDetailID;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
