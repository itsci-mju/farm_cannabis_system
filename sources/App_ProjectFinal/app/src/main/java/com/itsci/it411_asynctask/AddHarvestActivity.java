package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;

import java.util.Calendar;

public class AddHarvestActivity extends AppCompatActivity {
    String[] list_partName = {"ใบ","ราก","ลำต้น","ยอดอ่อน"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_harvest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddHarvestActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        String plantid = intent.getStringExtra("plantid");

        TextView txtid = findViewById(R.id.txtid);
        txtid.setText(plantid);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        EditText edt = findViewById(R.id.txtharvestdate);
        if((month+1) < 10 && dayOfMonth < 10) {
            edt.setText("0" + dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) < 10 && dayOfMonth >= 10){
            edt.setText(dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) >= 10 && dayOfMonth < 10){
            edt.setText("0" + dayOfMonth + "-" + (month+1) + "-" + year);
        }else {
            edt.setText(dayOfMonth + "-" + (month+1) + "-" + year);
        }

        for(int i = 0; i < list_partName.length; i++){
            final View v = getLayoutInflater().inflate(R.layout.list_in_add_and_edit_harvest, null);
            LinearLayout ll = findViewById(R.id.list_in_add_and_edit_harvest);

            TextView partname = v.findViewById(R.id.partname);
            partname.setText(list_partName[i]);

            TextView txtunit = v.findViewById(R.id.sumprice);

            txtunit.setText("กิโลกรัม");

            ll.addView(v);
        }
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(AddHarvestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(AddHarvestActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHarvestActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHarvestActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(AddHarvestActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(AddHarvestActivity.this, ListPlantingActivity.class);
        startActivity(intent);
    }

    public void onClickHarvestDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddHarvestActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtharvestdate);
                if((month+1) < 10 && dayOfMonth < 10) {
                    edt.setText("0" + dayOfMonth + "-0" + (month+1) + "-" + year);
                }else if((month+1) < 10 && dayOfMonth >= 10){
                    edt.setText(dayOfMonth + "-0" + (month+1) + "-" + year);
                }else if((month+1) >= 10 && dayOfMonth < 10){
                    edt.setText("0" + dayOfMonth + "-" + (month+1) + "-" + year);
                }else {
                    edt.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                }
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpd.show();
    }

    public void onSubmitAddHarvest(View view) {
        EditText txtharvestdate = null;
        EditText txtnote = null;
        boolean ck = false;
        LinearLayout ll = findViewById(R.id.list_in_add_and_edit_harvest);
        for (int i = 0; i < list_partName.length; i++) {
            View v = ll.getChildAt(i);

            EditText txtqty = v.findViewById(R.id.qty);

            if(!txtqty.getText().toString().equals("0") && !txtqty.getText().toString().equals("")){
                ck = true;
            }
            txtharvestdate = findViewById(R.id.txtharvestdate);
            txtnote = findViewById(R.id.txtnote);

            txtharvestdate.setError(null);
            txtnote.setError(null);
        }
        String harvestdate = txtharvestdate.getText().toString().trim();
        String note = txtnote.getText().toString().trim();

        if(ck == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHarvestActivity.this);
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("กรุณากรอกจำนวนอย่างน้อย 1 รายการ");
            builder.setCancelable(false);
            builder.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            if(harvestdate.equals("")){
                if(harvestdate.equals("")){
                    txtharvestdate.setError("กรุณากรอกวันที่เก็บเกี่ยว");
                }else{
                    txtharvestdate.setError(null);
                }

                if(note.length() > 255){
                    txtnote.setError("หมายเหตุไม่สามารถมากกว่า 255 ตัวอักษรได้");
                }else{
                    txtnote.setError(null);
                }
            }else{
                txtharvestdate.setError(null);
                txtnote.setError(null);

                final WSManager manager = WSManager.getWsManager(this);
                HarvestModel harvestModel = new HarvestModel();
                Intent intent = getIntent();
                String plantid = intent.getStringExtra("plantid");

                manager.listHarvestGroupByHarvestID(plantid, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;
                        LinearLayout ll = findViewById(R.id.list_in_add_and_edit_harvest);
                        for (int i = 0; i < list_partName.length; i++) {
                            View v = ll.getChildAt(i);

                            EditText txtharvestdate = findViewById(R.id.txtharvestdate);
                            TextView partname = v.findViewById(R.id.partname);
                            EditText qty = v.findViewById(R.id.qty);
                            TextView unit = v.findViewById(R.id.sumprice);
                            EditText txtnote = findViewById(R.id.txtnote);
                            TextView txtplantid = findViewById(R.id.txtid);

                            if(!qty.getText().toString().equals("0") && !qty.getText().toString().equals("")){
                                if (list.length > 0) {
                                    harvestModel.getHarvest().setHarvestID(String.valueOf(list.length+1));
                                    Log.e("else", String.valueOf(list.length+1));
                                }else{
                                    harvestModel.getHarvest().setHarvestID("1");
                                }

                                harvestModel.getHarvest().setHarvestDate(txtharvestdate.getText().toString());
                                harvestModel.getHarvest().setPartName(partname.getText().toString());
                                harvestModel.getHarvest().setQty(qty.getText().toString());
                                harvestModel.getHarvest().setUnit(unit.getText().toString());

                                if (txtnote.getText().toString().equals("")) {
                                    harvestModel.getHarvest().setNote("-");
                                } else {
                                    harvestModel.getHarvest().setNote(txtnote.getText().toString());
                                }

                                harvestModel.getHarvest().setPlanting(txtplantid.getText().toString());

                                manager.insert_Harvest(harvestModel, new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                    }

                                    @Override
                                    public void onError(String err) {
                                        Toast.makeText(AddHarvestActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            if(i+1 == list_partName.length){
                                Toast.makeText(AddHarvestActivity.this, "บันทึกข้อมูลการเก็บเกี่ยวเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddHarvestActivity.this, ListHarvestActivity.class);
                                TextView txtid = findViewById(R.id.txtid);
                                intent.putExtra("plantid", txtid.getText().toString());
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onError(String err) {

                    }
                });
            }
        }
    }
}