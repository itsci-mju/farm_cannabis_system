package bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="officer")
public class Officer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="officerid", insertable=false, updatable=false)
	private int officerid;
	
	@Column(name="address", nullable=false)
	private String address;
	
	@Column(name="mobileno", nullable=false)
	private String mobileno;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="username")
	private User user;
	
	public Officer() {
		// TODO Auto-generated constructor stub
	}

	
	public Officer(int officerid, String address, String mobileno, User user) {
		super();
		this.officerid = officerid;
		this.address = address;
		this.mobileno = mobileno;
		this.user = user;
	}



	public int getOfficerid() {
		return officerid;
	}

	public void setOfficerid(int officerid) {
		this.officerid = officerid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
}
