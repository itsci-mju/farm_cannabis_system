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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.PlantingModel;
import com.itsci.model.ProductModel;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPlantingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_planting);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddPlantingActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        EditText edt = findViewById(R.id.txtplantingdate);
        if((month+1) < 10 && dayOfMonth < 10) {
            edt.setText("0" + dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) < 10 && dayOfMonth >= 10){
            edt.setText(dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) >= 10 && dayOfMonth < 10){
            edt.setText("0" + dayOfMonth + "-" + (month+1) + "-" + year);
        }else {
            edt.setText(dayOfMonth + "-" + (month+1) + "-" + year);
        }

        int month2 = c.get(Calendar.MONTH)+3;

        EditText edt2 = findViewById(R.id.txtexp_harvestDate);
        if((month2+1) < 10 && dayOfMonth < 10) {
            edt2.setText("0" + dayOfMonth + "-0" + (month2+1) + "-" + year);
        }else if((month2+1) < 10 && dayOfMonth >= 10){
            edt2.setText(dayOfMonth + "-0" + (month2+1) + "-" + year);
        }else if((month2+1) >= 10 && dayOfMonth < 10){
            edt2.setText("0" + dayOfMonth + "-" + (month2+1) + "-" + year);
        }else {
            edt2.setText(dayOfMonth + "-" + (month2+1) + "-" + year);
        }

        Spinner unit = findViewById(R.id.spinner_unit);

        String[] list_unit = {"ตารางเมตร","ตารางวา","ไร่"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPlantingActivity.this,
                android.R.layout.select_dialog_item, list_unit);

        unit.setAdapter(adapter);
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(AddPlantingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(AddPlantingActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddPlantingActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AddPlantingActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(AddPlantingActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickPlantingDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddPlantingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtplantingdate);
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

    public void onClickExpHarvestDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+3;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddPlantingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtexp_harvestDate);
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

    public void onSubmitAddPlanting(View view){
        EditText txtplantingdate = findViewById(R.id.txtplantingdate);
        EditText txtexp_harvestDate = findViewById(R.id.txtexp_harvestDate);
        EditText txtpay = findViewById(R.id.txtpay);
        EditText txtdiscard = findViewById(R.id.txtdiscard);
        EditText txtplant = findViewById(R.id.txtplant);
        EditText txtarea = findViewById(R.id.txtarea);
        EditText txtcropid = findViewById(R.id.txtcropid);
        EditText txthow_plant = findViewById(R.id.txthow_plant);
        EditText txtnote = findViewById(R.id.txtnote);

        txtplantingdate.setError(null);
        txtexp_harvestDate.setError(null);
        txtpay.setError(null);
        txtdiscard.setError(null);
        txtplant.setError(null);
        txtarea.setError(null);
        txtcropid.setError(null);
        txthow_plant.setError(null);
        txtnote.setError(null);

        String plantingdate = txtplantingdate.getText().toString().trim();
        String exp_harvestDate = txtexp_harvestDate.getText().toString().trim();
        String pay = txtpay.getText().toString().trim();
        String discard = txtdiscard.getText().toString().trim();
        String plant = txtplant.getText().toString().trim();
        String area = txtarea.getText().toString().trim();
        String cropid = txtcropid.getText().toString().trim();
        String how_plant = txthow_plant.getText().toString().trim();
        String note = txtnote.getText().toString().trim();

        if(plantingdate.equals("") || exp_harvestDate.equals("") || pay.equals("") ||
        discard.equals("") || plant.equals("") || area.equals("") ||
        cropid.equals("") || how_plant.equals("")){
            if(plantingdate.equals("")){
                txtplantingdate.setError("กรุณากรอกวันที่ปลูก");
            }else{
                txtplantingdate.setError(null);
            }

            if(exp_harvestDate.equals("")){
                txtplantingdate.setError("กรุณากรอกวันที่คาดว่าจะเก็บเกี่ยว");
            }else{
                txtplantingdate.setError(null);
            }

            if(pay.equals("")) {
                txtpay.setError("กรุณากรอกปริมาณปลูกที่รับจ่ายสิ่งที่ปลูก");
            }else if(pay.equals("0")){
                txtpay.setError("กรุณากรอกปริมาณปลูกที่รับจ่ายสิ่งที่ปลูกมากกว่า 0");
            }else{
                txtpay.setError(null);
            }

            if(discard.equals("")){
                txtdiscard.setError("กรุณากรอกจำนวนคัดทิ้ง");
            }else{
                txtdiscard.setError(null);
            }

            if(plant.equals("")) {
                txtplant.setError("กรุณากรอกจำนวนต้นกล้าที่ปลูก");
            }else if(plant.equals("0")){
                txtplant.setError("กรุณากรอกจำนวนต้นกล้าที่ปลูกมากกว่า 0");
            }else{
                txtplant.setError(null);
            }

            if(area.equals("")) {
                txtarea.setError("กรุณากรอกพื้นที่ที่ปลูก");
            }else if(area.equals("0")){
                txtarea.setError("พื้นที่ที่ปลูกต้องไม่ใช่เลข 0");
            }else{
                txtarea.setError(null);
            }

            if(cropid.equals("")){
                txtcropid.setError("กรุณากรอกหมายเลข Crop ID");
            }else{
                txtcropid.setError(null);
            }

            if(how_plant.equals("")){
                txthow_plant.setError("กรุณากรอกวิธีการปลูก");
            }else{
                txthow_plant.setError(null);
            }

            if(note.length() > 255){
                txtnote.setError("หมายเหตุไม่สามารถมากกว่า 255 ตัวอักษรได้");
            }else{
                txtnote.setError(null);
            }

        }else {
            txtplantingdate.setError(null);
            txtexp_harvestDate.setError(null);
            txtpay.setError(null);
            txtdiscard.setError(null);
            txtplant.setError(null);
            txtarea.setError(null);
            txtcropid.setError(null);
            txthow_plant.setError(null);
            txtnote.setError(null);

            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);
            final PlantingModel plantingModel = new PlantingModel();
            manager.listPlanting(plantingModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    PlantingModel.Planting[] list = (PlantingModel.Planting[]) response;

                    TextView txtplantingdate = findViewById(R.id.txtplantingdate);
                    TextView txtexp_harvestDate = findViewById(R.id.txtexp_harvestDate);
                    TextView txtpay = findViewById(R.id.txtpay);
                    TextView txtdiscard = findViewById(R.id.txtdiscard);
                    TextView txtplant = findViewById(R.id.txtplant);
                    TextView txtarea = findViewById(R.id.txtarea);
                    Spinner spinner_unit = findViewById(R.id.spinner_unit);
                    TextView txtcropid = findViewById(R.id.txtcropid);
                    TextView txthow_plant = findViewById(R.id.txthow_plant);
                    TextView txtnote = findViewById(R.id.txtnote);

                    if (list.length > 0) {
                        int plantid = 1;
                        for (int i = 0; i < list.length; i++) {
                            Log.e("position", i + " " + list[i].getPlantID());

                            if (!String.valueOf(plantid).equals(String.valueOf(list[i].getPlantID()))) {
                                plantingModel.getPlanting().setPlantID(String.valueOf(plantid));
                            } else {
                                plantingModel.getPlanting().setPlantID(String.valueOf(list.length + 1));
                            }
                            plantid++;
                        }
                    } else {
                        plantingModel.getPlanting().setPlantID("1");
                    }


                    plantingModel.getPlanting().setPlantDate(txtplantingdate.getText().toString());
                    plantingModel.getPlanting().setExp_harvestDate(txtexp_harvestDate.getText().toString());
                    plantingModel.getPlanting().setPay(txtpay.getText().toString());
                    plantingModel.getPlanting().setDiscard(txtdiscard.getText().toString());
                    plantingModel.getPlanting().setPlant(txtplant.getText().toString());
                    plantingModel.getPlanting().setArea(txtarea.getText().toString());
                    plantingModel.getPlanting().setUnit(spinner_unit.getSelectedItem().toString());
                    plantingModel.getPlanting().setCropid(txtcropid.getText().toString());
                    plantingModel.getPlanting().setHow_plant(txthow_plant.getText().toString());
                    if (txtnote.getText().toString().equals("")) {
                        plantingModel.getPlanting().setNote("-");
                    } else {
                        plantingModel.getPlanting().setNote(txtnote.getText().toString());
                    }

                    manager.insert_planting(plantingModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Toast.makeText(AddPlantingActivity.this, "บันทึกข้อมูลการเพาะปลูกเรียบร้อยแล้ว รหัส " + plantingModel.getPlanting().getPlantID(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPlantingActivity.this, ListPlantingActivity.class);
                            startActivity(intent);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(AddPlantingActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเพาะปลูกได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(AddPlantingActivity.this, "ไม่สามารถทำการบันทึกข้อมูลการเพาะปลูกได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}