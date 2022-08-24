package bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="harvest")
public class Harvest implements Serializable{
	
	@Id
	@Column(name="HarvestID")	
	private int HarvestID;
	
	@Column(name="harvestDate", nullable=false)
	private Date harvestDate;
	
	@Id
	@Column(name="partName", nullable=false)
	private String partName;
	
	@Column(name="qty", nullable=false)
	private double qty;
	
	@Column(name="unit", nullable=false)
	private String unit;
	
	@Column(name="note")
	private String note;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="plantID")
	private Planting planting;

	public Harvest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Harvest(int harvestID, Date harvestDate, String partName, double qty, String unit, String note,
			Planting planting) {
		super();
		HarvestID = harvestID;
		this.harvestDate = harvestDate;
		this.partName = partName;
		this.qty = qty;
		this.unit = unit;
		this.note = note;
		this.planting = planting;
	}

	public int getHarvestID() {
		return HarvestID;
	}

	public void setHarvestID(int harvestID) {
		HarvestID = harvestID;
	}

	public Date getHarvestDate() {
		return harvestDate;
	}

	public void setHarvestDate(Date harvestDate) {
		this.harvestDate = harvestDate;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
