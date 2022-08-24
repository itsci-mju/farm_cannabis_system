package bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="plantingprogress")
public class PlantingProgress implements Serializable{
	
	@Id
	@Column(name="progressID")	
	private int progressID;
	
	@Column(name="progressDate", nullable=false)
	private Date progressDate;
	
	@Column(name="imgProgress", nullable=false)
	private String imgProgress;
	
	@Column(name="note")
	private String note;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="plantID")
	private Planting planting;

	public PlantingProgress() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlantingProgress(int progressID, Date progressDate, String imgProgress, String note, Planting planting) {
		super();
		this.progressID = progressID;
		this.progressDate = progressDate;
		this.imgProgress = imgProgress;
		this.note = note;
		this.planting = planting;
	}

	public int getProgressID() {
		return progressID;
	}

	public void setProgressID(int progressID) {
		this.progressID = progressID;
	}

	public Date getProgressDate() {
		return progressDate;
	}

	public void setProgressDate(Date progressDate) {
		this.progressDate = progressDate;
	}

	public String getImgProgress() {
		return imgProgress;
	}

	public void setImgProgress(String imgProgress) {
		this.imgProgress = imgProgress;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Planting getPlanting() {
		return planting;
	}

	public void setPlanting(Planting planting) {
		this.planting = planting;
	}

}
