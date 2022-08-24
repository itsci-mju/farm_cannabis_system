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
	
	@Column(name="area", nullable=false)
	private int area;
	
	@Column(name="unit", nullable=false)
	private String unit;
	
	@Column(name="cropid", nullable=false)
	private String cropid;
	
	@Column(name="how_plant", nullable=false)
	private String how_plant;
	
	@Column(name="note")
	private String note;


	public Planting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Planting(int plantID, Date plantDate, Date exp_harvestDate, double pay, double discard, double plant, int area, String unit,
			String cropid, String how_plant, String note) {
		super();
		this.plantID = plantID;
		this.plantDate = plantDate;
		this.exp_harvestDate = exp_harvestDate;
		this.pay = pay;
		this.discard = discard;
		this.plant = plant;
		this.area = area;
		this.unit = unit;
		this.cropid = cropid;
		this.how_plant = how_plant;
		this.note = note;
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

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
	
	
}
