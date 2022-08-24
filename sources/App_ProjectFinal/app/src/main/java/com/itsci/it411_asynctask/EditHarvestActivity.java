package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;

import java.util.Calendar;

public class EditHarvestActivity extends AppCompatActivity {
    String[] list_partName = {"ใบ","ราก","ลำต้น","ยอดอ่อน"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_harvest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditHarvestActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();

        String hid = intent.getStringExtra("hid");
        String hdate = intent.getStringExtra("hdate");
        String hnote = intent.getStringExtra("hnote");
        String plantid = intent.getStringExtra("plantid");

        TextView txthid = findViewById(R.id.hid);
        txthid.setText(hid);

        TextView txthdate = findViewById(R.id.txtharvestdate);
        txthdate.setText(hdate);

        TextView txthnote = findViewById(R.id.txtnote);
        txthnote.setText(hnote);

        TextView txtid = findViewById(R.id.txtid);
        txtid.setText(plantid);


        final WSManager manager = WSManager.getWsManager(this);

        HarvestModel harvestModel = new HarvestModel();
        harvestModel.getHarvest().setHarvestID(hid);
        harvestModel.getHarvest().setPlanting(plantid);

        manager.listDetailHarvest(harvestModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                for(int i = 0; i < list_partName.length; i++){
                    final View v = getLayoutInflater().inflate(R.layout.list_in_add_and_edit_harvest, null);
                    LinearLayout ll = findViewById(R.id.list_in_add_and_edit_harvest);

                    HarvestModel2.Harvest[] detail = (HarvestModel2.Harvest[]) response;

                    TextView partname = v.findViewById(R.id.partname);
                    partname.setText(list_partName[i]);


                    for(int j = 0; j < detail.length; j++){
                        if(detail[j].getPartName().equals(list_partName[i])){
                            EditText qty = v.findViewById(R.id.qty);
                            qty.setText(detail[j].getQty());
                        }
                    }

                    TextView txtunit = v.findViewById(R.id.unit);

                    txtunit.setText("กิโลกรัม");

                    ll.addView(v);
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(EditHarvestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(EditHarvestActivity.this, ProductActivity.class);
        TextView txtid = findViewById(R.id.txtid);
        intent.putExtra("plantid", txtid.getText().toString());
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditHarvestActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(EditHarvestActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditHarvestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(EditHarvestActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(EditHarvestActivity.this, ListHarvestActivity.class);
        TextView txtid = findViewById(R.id.txtid);
        intent.putExtra("plantid", txtid.getText().toString());
        startActivity(intent);
    }

    public void onClickHarvestDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(EditHarvestActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        dpd.show();
    }

    public void onSubmitEditHarvest(View view){
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
            AlertDialog.Builder builder = new AlertDialog.Builder(EditHarvestActivity.this);
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
        }else {
            if (harvestdate.equals("")) {
                if (harvestdate.equals("")) {
                    txtharvestdate.setError("กรุณากรอกวันที่เก็บเกี่ยว");
                } else {
                    txtharvestdate.setError(null);
                }

                if (note.length() > 255) {
                    txtnote.setError("หมายเหตุไม่สามารถมากกว่า 255 ตัวอักษรได้");
                } else {
                    txtnote.setError(null);
                }
            }else {
                txtharvestdate.setError(null);
                txtnote.setError(null);

                //final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

                final WSManager manager = WSManager.getWsManager(this);

                final HarvestModel harvestModel = new HarvestModel();

                for (int i = 0; i < list_partName.length; i++) {
                    final View v = ll.getChildAt(i);

                    final TextView hid = findViewById(R.id.hid);
                    txtharvestdate = findViewById(R.id.txtharvestdate);
                    final TextView partname = v.findViewById(R.id.partname);
                    final EditText qty = v.findViewById(R.id.qty);
                    final TextView unit = v.findViewById(R.id.unit);
                    txtnote = findViewById(R.id.txtnote);
                    final TextView txtplantid = findViewById(R.id.txtid);

                    final int finalI = i;
                    final EditText finalTxtharvestdate = txtharvestdate;
                    final EditText finalTxtnote = txtnote;

                    harvestModel.getHarvest().setHarvestID(hid.getText().toString());
                    harvestModel.getHarvest().setPlanting(txtplantid.getText().toString());

                    manager.listDetailHarvest(harvestModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            HarvestModel2.Harvest[] detail = (HarvestModel2.Harvest[]) response;

                            harvestModel.getHarvest().setHarvestID(hid.getText().toString());
                            harvestModel.getHarvest().setHarvestDate(finalTxtharvestdate.getText().toString());
                            harvestModel.getHarvest().setPartName(partname.getText().toString());
                            harvestModel.getHarvest().setQty(qty.getText().toString());
                            harvestModel.getHarvest().setUnit(unit.getText().toString());

                            if (finalTxtnote.getText().toString().equals("")) {
                                harvestModel.getHarvest().setNote("-");
                            } else {
                                harvestModel.getHarvest().setNote(finalTxtnote.getText().toString());
                            }

                            harvestModel.getHarvest().setPlanting(txtplantid.getText().toString());

                            boolean ckname = false;
                            boolean ckqty = false;
                            for(int j = 0; j < detail.length; j++){
                                if(detail[j].getPartName().equals(list_partName[finalI])) {
                                    ckname = true;
                                    if(!qty.getText().toString().equals(detail[j].getQty())) {
                                        ckqty = true;
                                    }
                                }
                            }

                            if(ckname == true) {
                                if (!qty.getText().toString().equals("0") && !qty.getText().toString().equals("")) {
                                    if (ckqty == true) {
                                        manager.update_harvest(harvestModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(EditHarvestActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                } else if (qty.getText().toString().equals("0") || qty.getText().toString().equals("")) {
                                    manager.delete_Detailharvest(harvestModel, new WSManager.WSManagerListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                        }

                                        @Override
                                        public void onError(String err) {
                                            Toast.makeText(EditHarvestActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }else {
                                    if (!qty.getText().toString().equals("0") && !qty.getText().toString().equals("")) {
                                        manager.insert_Harvest(harvestModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(EditHarvestActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }
                            }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(EditHarvestActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเก็บเกี่ยวได้", Toast.LENGTH_SHORT).show();

                        }
                    });
                    if(i+1 == list_partName.length){
                        Toast.makeText(EditHarvestActivity.this, "แก้ไชข้อมูลการเก็บเกี่ยวเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditHarvestActivity.this, ListHarvestActivity.class);
                        TextView txtid = findViewById(R.id.txtid);
                        intent.putExtra("plantid", txtid.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        }
    }
}