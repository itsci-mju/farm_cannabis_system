package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

public class PlantingModel {
    private Planting planting;

    public PlantingModel() {
        planting = new Planting();
    }

    public PlantingModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        planting = gson.fromJson(jsonResponse, Planting.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.planting);
    }

    public Planting getPlanting() {
        return planting;
    }

    public class Planting {
        String plantID;
        String plantDate;
        String exp_harvestDate;
        String pay;
        String discard;
        String plant;
        String area;
        String unit;
        String cropid;
        String how_plant;
        String note;

        public String getPlantID() {
            return plantID;
        }

        public void setPlantID(String plantID) {
            this.plantID = plantID;
        }

        public String getPlantDate() {
            return plantDate;
        }

        public void setPlantDate(String plantDate) {
            this.plantDate = plantDate;
        }

        public String getExp_harvestDate() {
            return exp_harvestDate;
        }

        public void setExp_harvestDate(String exp_harvestDate) {
            this.exp_harvestDate = exp_harvestDate;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

        public String getDiscard() {
            return discard;
        }

        public void setDiscard(String discard) {
            this.discard = discard;
        }

        public String getPlant() {
            return plant;
        }

        public void setPlant(String plant) {
            this.plant = plant;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
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
}
