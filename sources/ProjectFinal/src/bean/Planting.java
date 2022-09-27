package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="planting")
public class Planting {

	@Id
	@Column(name="plantID")
	private int plantID;
	
	@Column(name="plantDate", nullable=false)
	private Date plantDate;
	
	@Column(name="exp_harvestDate", nullable=false)
	private Date exp_harvestDate;
	
	@Column(name="pay", nullable=false)
	private double pay;
	
	@Column(name="discard", nullable=false)
	private double discard;
	
	@Column(name="plant", nullable=false)
	private double plant;
	
	@Column(name="sqaure_meters", nullable=false)
	private int sqaure_meters;
	
	@Column(name="sqaure_wa", nullable=false)
	private int sqaure_wa;
	
	@Column(name="ngar", nullable=false)
	private int ngar;
	
	@Column(name="rai", nullable=false)
	private int rai;
	
	@Column(name="cropid", nullable=false)
	private String cropid;
	
	@Column(name="how_plant", nullable=false)
	private String how_plant;
	
	@Column(name="note")
	private String note;
	
	@Column(name="status")
	private String status;


	public Planting() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Planting(int plantID, Date plantDate, Date exp_harvestDate, double pay, double discard, double plant,
			int sqaure_meters, int sqaure_wa, int ngar, int rai, String cropid, String how_plant, String note,
			String status) {
		super();
		this.plantID = plantID;
		this.plantDate = plantDate;
		this.exp_harvestDate = exp_harvestDate;
		this.pay = pay;
		this.discard = discard;
		this.plant = plant;
		this.sqaure_meters = sqaure_meters;
		this.sqaure_wa = sqaure_wa;
		this.ngar = ngar;
		this.rai = rai;
		this.cropid = cropid;
		this.how_plant = how_plant;
		this.note = note;
		this.status = status;
	}


	public int getPlantID() {
		return plantID;
	}


	public void setPlantID(int plantID) {
		this.plantID = plantID;
	}


	public Date getPlantDate() {
		return plantDate;
	}


	public void setPlantDate(Date plantDate) {
		this.plantDate = plantDate;
	}


	public Date getExp_harvestDate() {
		return exp_harvestDate;
	}


	public void setExp_harvestDate(Date exp_harvestDate) {
		this.exp_harvestDate = exp_harvestDate;
	}


	public double getPay() {
		return pay;
	}


	public void setPay(double pay) {
		this.pay = pay;
	}


	public double getDiscard() {
		return discard;
	}


	public void setDiscard(double discard) {
		this.discard = discard;
	}


	public double getPlant() {
		return plant;
	}


	public void setPlant(double plant) {
		this.plant = plant;
	}


	public int getSqaure_meters() {
		return sqaure_meters;
	}


	public void setSqaure_meters(int sqaure_meters) {
		this.sqaure_meters = sqaure_meters;
	}


	public int getSqaure_wa() {
		return sqaure_wa;
	}


	public void setSqaure_wa(int sqaure_wa) {
		this.sqaure_wa = sqaure_wa;
	}


	public int getNgar() {
		return ngar;
	}


	public void setNgar(int ngar) {
		this.ngar = ngar;
	}


	public int getRai() {
		return rai;
	}


	public void setRai(int rai) {
		this.rai = rai;
	}


	public String getCropid() {
		return cropid;
	}


	public void setCropid(String cropid) {
		this.cropid = cropid;
	}


	public String getHow_plant() {
		return how_plant;
	}


	public void setHow_plant(String how_plant) {
		this.how_plant = how_plant;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
}
