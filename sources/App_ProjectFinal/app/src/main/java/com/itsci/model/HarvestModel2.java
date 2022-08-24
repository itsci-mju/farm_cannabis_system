package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HarvestModel2 {
    private Harvest harvest;

    public HarvestModel2(){
        harvest = new Harvest();
    }

    public HarvestModel2(String jsonResponse) {
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
        PlantingModel.Planting planting;

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

        public PlantingModel.Planting getPlanting() {
            return planting;
        }

        public void setPlanting(PlantingModel.Planting planting) {
            this.planting = planting;
        }
    }
}
