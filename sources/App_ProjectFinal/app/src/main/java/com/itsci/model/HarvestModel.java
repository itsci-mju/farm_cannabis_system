package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HarvestModel {
    private Harvest harvest;

    public HarvestModel(){
        harvest = new Harvest();
    }

    public HarvestModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        harvest = gson.fromJson(jsonResponse, Harvest.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.harvest);
    }

    public Harvest getHarvest() {
        return harvest;
    }

    public class Harvest {
        String HarvestID;
        String harvestDate;
        String partName;
        String qty;
        String unit;
        String note;
        String planting;

        public String getHarvestID() {
            return HarvestID;
        }

        public void setHarvestID(String harvestID) {
            HarvestID = harvestID;
        }

        public String getHarvestDate() {
            return harvestDate;
        }

        public void setHarvestDate(String harvestDate) {
            this.harvestDate = harvestDate;
        }

        public String getPartName() {
            return partName;
        }

        public void setPartName(String partName) {
            this.partName = partName;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
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

        public String getPlanting() {
            return planting;
        }

        public void setPlanting(String planting) {
            this.planting = planting;
        }
    }
}
