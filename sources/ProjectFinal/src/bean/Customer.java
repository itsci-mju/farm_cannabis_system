package bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {
	
	@Id
	@Column(name="CustomerID", insertable=false, updatable=false)	
	private int CustomerID;
	
	@Column(name="company", nullable=false)
	private String company;
	
	@Column(name="address", nullable=false)
	private String address;
	
	@Column(name="mobileNo", nullable=false)
	private String mobileNo;
	
	@Column(name="imgCertificate", nullable=false)
	private String imgCertificate;
	
	@Column(name="imgIDcard", nullable=false)
	private String imgIDCard;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="reqregid")
	private RequestRegister reqreg;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="username")
	private User user;
	
	public Customer() {
	}

	public Customer(int customerID, String company, String address, String mobileNo, String imgCertificate,
			String imgIDCard, RequestRegister reqreg, User user) {
		super();
		CustomerID = customerID;
		this.company = company;
		this.address = address;
		this.mobileNo = mobileNo;
		this.imgCertificate = imgCertificate;
		this.imgIDCard = imgIDCard;
		this.reqreg = reqreg;
		this.user = user;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getImgCertificate() {
		return imgCertificate;
	}

	public void setImgCertificate(String imgCertificate) {
		this.imgCertificate = imgCertificate;
	}

	public String getImgIDCard() {
		return imgIDCard;
	}

	public void setImgIDCard(String imgIDCard) {
		this.imgIDCard = imgIDCard;
	}

	public RequestRegister getReqreg() {
		return reqreg;
	}

	public void setReqreg(RequestRegister reqreg) {
		this.reqreg = reqreg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
