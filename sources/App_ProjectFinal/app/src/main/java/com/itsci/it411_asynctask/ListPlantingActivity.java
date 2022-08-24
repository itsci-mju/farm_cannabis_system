package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.PlantingModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListPlantingActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_planting);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListPlantingActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        final PlantingModel plantingModel = new PlantingModel();
        manager.listPlanting(plantingModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                PlantingModel.Planting[] list = (PlantingModel.Planting[]) response;
                if(list.length > 0){
                    for(int i = 0; i < list.length; i++){
                        View v = getLayoutInflater().inflate(R.layout.list_planting, null);
                        LinearLayout ll = findViewById(R.id.list_planting);

                        TextView id = v.findViewById(R.id.plantid);
                        id.setText(list[i].getPlantID());

                        TextView plantingdate = v.findViewById(R.id.plantingdate);
                        Date plantdate = new Date(list[i].getPlantDate());
                        String splantdate = sdf.format(plantdate);
                        plantingdate.setText(splantdate);

                        TextView exp_harvestDate = v.findViewById(R.id.exp_harvestDate);
                        Date harvestDate = new Date(list[i].getExp_harvestDate());
                        String sharvestDate = sdf.format(harvestDate);
                        exp_harvestDate.setText(sharvestDate);

                        TextView pay = v.findViewById(R.id.pay);
                        pay.setText(list[i].getPay());

                        TextView discard = v.findViewById(R.id.discard);
                        discard.setText(list[i].getDiscard());

                        TextView plant = v.findViewById(R.id.plant);
                        plant.setText(list[i].getPlant());

                        TextView area = v.findViewById(R.id.area);
                        area.setText(list[i].getArea());

                        TextView unit = v.findViewById(R.id.unit);
                        unit.setText(list[i].getUnit());

                        TextView cropid = v.findViewById(R.id.cropid);
                        cropid.setText(list[i].getCropid());

                        TextView how_plant = v.findViewById(R.id.how_plant);
                        how_plant.setText(list[i].getHow_plant());

                        TextView note = v.findViewById(R.id.note);
                        note.setText(list[i].getNote());

                        ll.addView(v);
                    }
                    loadingDialog.dismiss();

                    LinearLayout ll = findViewById(R.id.list_planting);

                    for(int i = 0; i < ll.getChildCount(); i++){
                        View v = ll.getChildAt(i);
                        CardView btnHarvest = v.findViewById(R.id.btnHarvest);
                        CardView btnListHarvest = v.findViewById(R.id.btnListHarvest);
                        CardView btnAddPlantingProgress = v.findViewById(R.id.btnAddPlantingProgress);
                        CardView btnListPlantingProgress = v.findViewById(R.id.btnListPlantingProgress);
                        CardView btnEditPlanting = v.findViewById(R.id.btnEditPlanting);
                        CardView btnDeletePlanting = v.findViewById(R.id.btnDeletePlanting);

                        final TextView id = v.findViewById(R.id.plantid);
                        final TextView plantingdate = v.findViewById(R.id.plantingdate);
                        final TextView exp_harvestDate = v.findViewById(R.id.exp_harvestDate);
                        final TextView pay = v.findViewById(R.id.pay);
                        final TextView discard = v.findViewById(R.id.discard);
                        final TextView plant = v.findViewById(R.id.plant);
                        final TextView area = v.findViewById(R.id.area);
                        final TextView unit = v.findViewById(R.id.unit);
                        final TextView cropid = v.findViewById(R.id.cropid);
                        final TextView how_plant = v.findViewById(R.id.how_plant);
                        final TextView note = v.findViewById(R.id.note);

                        btnHarvest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListPlantingActivity.this, AddHarvestActivity.class);

                                intent.putExtra("plantid", id.getText().toString());
                                intent.putExtra("plantingdate", plantingdate.getText().toString());
                                intent.putExtra("exp_harvestDate", exp_harvestDate.getText().toString());
                                intent.putExtra("pay", pay.getText().toString());
                                intent.putExtra("discard", discard.getText().toString());
                                intent.putExtra("plant", plant.getText().toString());
                                intent.putExtra("area", area.getText().toString());
                                intent.putExtra("unit", unit.getText().toString());
                                intent.putExtra("cropid", cropid.getText().toString());
                                intent.putExtra("how_plant", how_plant.getText().toString());
                                intent.putExtra("note", note.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnListHarvest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListPlantingActivity.this, ListHarvestActivity.class);

                                intent.putExtra("plantid", id.getText().toString());
                                intent.putExtra("plantingdate", plantingdate.getText().toString());
                                intent.putExtra("exp_harvestDate", exp_harvestDate.getText().toString());
                                intent.putExtra("pay", pay.getText().toString());
                                intent.putExtra("discard", discard.getText().toString());
                                intent.putExtra("plant", plant.getText().toString());
                                intent.putExtra("area", area.getText().toString());
                                intent.putExtra("unit", unit.getText().toString());
                                intent.putExtra("cropid", cropid.getText().toString());
                                intent.putExtra("how_plant", how_plant.getText().toString());
                                intent.putExtra("note", note.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnAddPlantingProgress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListPlantingActivity.this, AddProgressActivity.class);

                                intent.putExtra("plantid", id.getText().toString());
                                intent.putExtra("plantingdate", plantingdate.getText().toString());
                                intent.putExtra("exp_harvestDate", exp_harvestDate.getText().toString());
                                intent.putExtra("pay", pay.getText().toString());
                                intent.putExtra("discard", discard.getText().toString());
                                intent.putExtra("plant", plant.getText().toString());
                                intent.putExtra("area", area.getText().toString());
                                intent.putExtra("unit", unit.getText().toString());
                                intent.putExtra("cropid", cropid.getText().toString());
                                intent.putExtra("how_plant", how_plant.getText().toString());
                                intent.putExtra("note", note.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnListPlantingProgress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListPlantingActivity.this, ListProgressActivity.class);

                                intent.putExtra("plantid", id.getText().toString());
                                intent.putExtra("plantingdate", plantingdate.getText().toString());
                                intent.putExtra("exp_harvestDate", exp_harvestDate.getText().toString());
                                intent.putExtra("pay", pay.getText().toString());
                                intent.putExtra("discard", discard.getText().toString());
                                intent.putExtra("plant", plant.getText().toString());
                                intent.putExtra("area", area.getText().toString());
                                intent.putExtra("unit", unit.getText().toString());
                                intent.putExtra("cropid", cropid.getText().toString());
                                intent.putExtra("how_plant", how_plant.getText().toString());
                                intent.putExtra("note", note.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnEditPlanting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListPlantingActivity.this, EditPlantingActivity.class);

                                intent.putExtra("plantid", id.getText().toString());
                                intent.putExtra("plantingdate", plantingdate.getText().toString());
                                intent.putExtra("exp_harvestDate", exp_harvestDate.getText().toString());
                                intent.putExtra("pay", pay.getText().toString());
                                intent.putExtra("discard", discard.getText().toString());
                                intent.putExtra("plant", plant.getText().toString());
                                intent.putExtra("area", area.getText().toString());
                                intent.putExtra("unit", unit.getText().toString());
                                intent.putExtra("cropid", cropid.getText().toString());
                                intent.putExtra("how_plant", how_plant.getText().toString());
                                intent.putExtra("note", note.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnDeletePlanting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ListPlantingActivity.this);
                                builder.setMessage("คุณต้องการลบข้อมูลการเพาะปลูก รหัส " + id.getText() + " ใช่หรือไม่ ?");
                                builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        plantingModel.getPlanting().setPlantID(id.getText().toString());
                                        plantingModel.getPlanting().setPlantDate(plantingdate.getText().toString());
                                        plantingModel.getPlanting().setExp_harvestDate(exp_harvestDate.getText().toString());
                                        plantingModel.getPlanting().setPay(pay.getText().toString());
                                        plantingModel.getPlanting().setDiscard(discard.getText().toString());
                                        plantingModel.getPlanting().setPlant(plant.getText().toString());
                                        plantingModel.getPlanting().setArea(area.getText().toString());
                                        plantingModel.getPlanting().setUnit(unit.getText().toString());
                                        plantingModel.getPlanting().setCropid(cropid.getText().toString());
                                        plantingModel.getPlanting().setHow_plant(how_plant.getText().toString());
                                        plantingModel.getPlanting().setNote(note.getText().toString());

                                        manager.delete_planting(plantingModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                                Toast.makeText(ListPlantingActivity.this, "ลบข้อมูลการเพาะปลูกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ListPlantingActivity.this, ListPlantingActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(ListPlantingActivity.this, "ไม่สามารถทำการลบข้อมูลการเพาะปลูกได้", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });

                    }
                }else{
                    View v = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                    LinearLayout ll = findViewById(R.id.list_planting);
                    ll.addView(v);
                    loadingDialog.dismiss();

                    ImageView imgAddData = v.findViewById(R.id.imageAddData);

                    imgAddData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListPlantingActivity.this, AddPlantingActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(String err) {
                loadingDialog.dismiss();
            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ListPlantingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ListPlantingActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListPlantingActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListPlantingActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ListPlantingActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}