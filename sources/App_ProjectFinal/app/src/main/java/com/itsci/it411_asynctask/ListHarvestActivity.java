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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.PlantingProgressModel;
import com.itsci.model.PlantingProgressModel2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListHarvestActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_harvest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListHarvestActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        final String plantid = intent.getStringExtra("plantid");

        TextView txtplantid = findViewById(R.id.txtid);
        txtplantid.setText(plantid);

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        manager.listHarvest(plantid, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;

                if(list.length > 0){
                    for (int i = 0; i < list.length; i++) {
                        final View v = getLayoutInflater().inflate(R.layout.list_harvest, null);

                        LinearLayout ll = findViewById(R.id.list_harvest);

                        TextView hid = v.findViewById(R.id.hid);
                        hid.setText(list[i].getHarvestID());

                        //TextView showhid = v.findViewById(R.id.showhid);
                        //showhid.setText(String.valueOf(i+1));

                        TextView thdate = v.findViewById(R.id.hdate);
                        Date hdate = new Date(list[i].getHarvestDate());
                        String shdate = sdf.format(hdate);
                        thdate.setText(shdate);

                        TextView hnote = v.findViewById(R.id.hnote);
                        hnote.setText(list[i].getNote());

                        HarvestModel harvestModel = new HarvestModel();
                        harvestModel.getHarvest().setHarvestID(list[i].getHarvestID());
                        harvestModel.getHarvest().setPlanting(plantid);

                        if(i == 0){

                            ll.addView(v);

                            manager.listDetailHarvest(harvestModel, new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;
                                    for(int i = 0; i < list.length; i++) {
                                        final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_harvest, null);
                                        LinearLayout ll2 = v.findViewById(R.id.list_in_list_harvest);

                                        TextView partname = v2.findViewById(R.id.partname);
                                        partname.setText(list[i].getPartName());

                                        TextView qty = v2.findViewById(R.id.qty);
                                        qty.setText(list[i].getQty());

                                        TextView unit = v2.findViewById(R.id.unit);
                                        unit.setText(list[i].getUnit());

                                        ll2.addView(v2);
                                    }
                                }

                                @Override
                                public void onError(String err) {

                                }
                            });
                        }else{
                            boolean flag = false;
                            for(int j = 0; j < i; j++){
                                if(list[i].getHarvestID().equals(list[j].getHarvestID())) {
                                    flag = true;
                                }
                            }
                            if(flag == false){
                                ll.addView(v);

                                manager.listDetailHarvest(harvestModel, new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;
                                        for(int i = 0; i < list.length; i++) {
                                            final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_harvest, null);
                                            LinearLayout ll2 = v.findViewById(R.id.list_in_list_harvest);

                                            TextView partname = v2.findViewById(R.id.partname);
                                            partname.setText(list[i].getPartName());

                                            TextView qty = v2.findViewById(R.id.qty);
                                            qty.setText(list[i].getQty());

                                            TextView unit = v2.findViewById(R.id.unit);
                                            unit.setText(list[i].getUnit());

                                            ll2.addView(v2);
                                        }
                                    }

                                    @Override
                                    public void onError(String err) {

                                    }
                                });
                            }
                        }
                        if(i == list.length-1){
                            loadingDialog.dismiss();
                        }
                    }

                    LinearLayout ll = findViewById(R.id.list_harvest);

                    for (int i = 0; i < ll.getChildCount(); i++) {
                        View v = ll.getChildAt(i);
                        CardView btnEditHarvest = v.findViewById(R.id.btnEditHarvest);
                        CardView btnDeleteHarvest = v.findViewById(R.id.btnDeleteHarvest);

                        final TextView hid = v.findViewById(R.id.hid);
                        final TextView hdate = v.findViewById(R.id.hdate);
                        final TextView hnote = v.findViewById(R.id.hnote);
                        final TextView plantid = findViewById(R.id.txtid);

                        btnEditHarvest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListHarvestActivity.this, EditHarvestActivity.class);

                                intent.putExtra("hid", hid.getText().toString());
                                intent.putExtra("hdate", hdate.getText().toString());
                                intent.putExtra("hnote", hnote.getText().toString());
                                intent.putExtra("plantid", plantid.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnDeleteHarvest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ListHarvestActivity.this);
                                builder.setMessage("คุณต้องการลบข้อมูลการเก็บเกี่ยว รหัส " + hid.getText() + " ใช่หรือไม่ ?");
                                builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HarvestModel harvestModel = new HarvestModel();

                                        harvestModel.getHarvest().setHarvestID(hid.getText().toString());
                                        harvestModel.getHarvest().setHarvestDate(hdate.getText().toString());
                                        harvestModel.getHarvest().setNote(hnote.getText().toString());
                                        harvestModel.getHarvest().setPlanting(plantid.getText().toString());

                                        manager.delete_harvest(harvestModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                                Toast.makeText(ListHarvestActivity.this, "ลบข้อมูลข้อมูลการเก็บเกี่ยวเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ListHarvestActivity.this, ListHarvestActivity.class);
                                                intent.putExtra("plantid", plantid.getText().toString());
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(ListHarvestActivity.this, "ไม่สามารถทำการลบข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();
                                                //loadingDialog.dismiss();
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
                    LinearLayout ll = findViewById(R.id.list_harvest);
                    ll.addView(v);
                    loadingDialog.dismiss();

                    ImageView imgAddData = v.findViewById(R.id.imageAddData);

                    imgAddData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListHarvestActivity.this, AddHarvestActivity.class);
                            intent.putExtra("plantid", plantid);
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

    public void onClickHomePage (View view){
        Intent intent = new Intent(ListHarvestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage (View view){
        Intent intent = new Intent(ListHarvestActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile (View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if (txtusername.getText().toString().equals("Guest")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListHarvestActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListHarvestActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage (View view){
        Intent intent = new Intent(ListHarvestActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickBackPage (View view){
        Intent intent = new Intent(ListHarvestActivity.this, ListPlantingActivity.class);
        startActivity(intent);
    }
}