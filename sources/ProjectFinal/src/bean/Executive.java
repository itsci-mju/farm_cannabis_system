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
@Table(name="executive")
public class Executive {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="executiveid", insertable=false, updatable=false)
	private int executiveid;
	
	@Column(name="mobileno", nullable=false)
	private String mobileno;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="username")
	private User user;
	

	public Executive(int executiveid, String mobileno, User user) {
		super();
		this.executiveid = executiveid;
		this.mobileno = mobileno;
		this.user = user;
	}

	public Executive() {
	}

	public int getExecutiveid() {
		return executiveid;
	}

	public void setExecutiveid(int executiveid) {
		this.executiveid = executiveid;
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
