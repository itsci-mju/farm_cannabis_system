package bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="requestregister")
public class RequestRegister {

	@Id
	@Column(name="reqregid")
	private int reqregid;
	
	@Column(name="persontype", nullable=false)
	private String persontype;
	
	@Column(name="status", nullable=false)
	private String status;
	
	public RequestRegister() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestRegister(int reqregid, String persontype, String status) {
		this.reqregid = reqregid;
		this.persontype = persontype;
		this.status = status;
	}

	public int getReqregid() {
		return reqregid;
	}

	public void setReqregid(int reqregid) {
		this.reqregid = reqregid;
	}

	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
