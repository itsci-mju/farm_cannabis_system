package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlantingProgressModel {
    private PlantingProgress plantingprogress;

    public PlantingProgressModel(){
        plantingprogress = new PlantingProgress();
    }

    public PlantingProgressModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        plantingprogress = gson.fromJson(jsonResponse, PlantingProgress.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.plantingprogress);
    }

    public PlantingProgress getPlantingProgress() {
        return plantingprogress;
    }

    public class PlantingProgress {
        String progressID;
        String progressDate;
        String imgProgress;
        String note;
        String planting;

        public String getProgressID() {
            return progressID;
        }

        public void setProgressID(String progressID) {
            this.progressID = progressID;
        }

        public String getProgressDate() {
            return progressDate;
        }

        public void setProgressDate(String progressDate) {
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

        public String getPlanting() {
            return planting;
        }

        public void setPlanting(String planting) {
            this.planting = planting;
        }
    }
}
