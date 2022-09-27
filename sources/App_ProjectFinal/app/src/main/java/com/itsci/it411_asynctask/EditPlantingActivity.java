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

import java.util.Calendar;

public class EditPlantingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_planting);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditPlantingActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        String id = intent.getStringExtra("plantid");
        String plantingdate = intent.getStringExtra("plantingdate");
        String exp_harvestDate = intent.getStringExtra("exp_harvestDate");
        String pay = intent.getStringExtra("pay");
        String discard = intent.getStringExtra("discard");
        String plant = intent.getStringExtra("plant");
        String sqaure_meters = intent.getStringExtra("sqaure_meters");
        String sqaure_wa = intent.getStringExtra("sqaure_wa");
        String ngar = intent.getStringExtra("ngar");
        String rai = intent.getStringExtra("rai");
        String cropid = intent.getStringExtra("cropid");
        String how_plant = intent.getStringExtra("how_plant");
        String note = intent.getStringExtra("note");
        String status = intent.getStringExtra("status");

        TextView txtid = findViewById(R.id.txtid);
        TextView txtplantingdate = findViewById(R.id.txtplantingdate);
        TextView txtexp_harvestDate = findViewById(R.id.txtexp_harvestDate);
        TextView txtpay = findViewById(R.id.txtpay);
        TextView txtdiscard = findViewById(R.id.txtdiscard);
        TextView txtplant = findViewById(R.id.txtplant);
        TextView txtsqaure_meters = findViewById(R.id.txtsqaure_meters);
        TextView txtsqaure_wa = findViewById(R.id.txtsqaure_wa);
        TextView txtngar = findViewById(R.id.txtngar);
        TextView txtrai = findViewById(R.id.txtrai);
        TextView txtcropid = findViewById(R.id.txtcropid);
        TextView txthow_plant = findViewById(R.id.txthow_plant);
        TextView txtnote = findViewById(R.id.txtnote);
        Spinner spinner_status = findViewById(R.id.spinner_status);

        String[] list_status = {"กำลังเพาะปลูก","กำลังเก็บเกี่ยว","เก็บเกี่ยวหมดแล้ว"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditPlantingActivity.this,
                android.R.layout.select_dialog_item, list_status);

        spinner_status.setAdapter(adapter);

        txtid.setText(id);
        txtplantingdate.setText(plantingdate);
        txtexp_harvestDate.setText(exp_harvestDate);
        txtpay.setText(pay);
        txtdiscard.setText(discard);
        txtplant.setText(plant);
        txtsqaure_meters.setText(sqaure_meters);
        txtsqaure_wa.setText(sqaure_wa);
        txtngar.setText(ngar);
        txtrai.setText(rai);
        txtcropid.setText(cropid);
        txthow_plant.setText(how_plant);
        txtnote.setText(note);
        spinner_status.setSelection(adapter.getPosition(status));

    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(EditPlantingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(EditPlantingActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlantingActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(EditPlantingActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(EditPlantingActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(EditPlantingActivity.this, ListPlantingActivity.class);
        startActivity(intent);
    }

    public void onClickPlantingDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(EditPlantingActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        DatePickerDialog dpd = new DatePickerDialog(EditPlantingActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        dpd.getDatePicker().setMinDate(c.getTimeInMillis());
        dpd.show();
    }

    public void onSubmitEditPlanting(View view){
        EditText txtplantingdate = findViewById(R.id.txtplantingdate);
        EditText txtexp_harvestDate = findViewById(R.id.txtexp_harvestDate);
        EditText txtpay = findViewById(R.id.txtpay);
        EditText txtdiscard = findViewById(R.id.txtdiscard);
        EditText txtplant = findViewById(R.id.txtplant);
        EditText txtarea = findViewById(R.id.txtarea);
        EditText txtsqaure_meters = findViewById(R.id.txtsqaure_meters);
        EditText txtsqaure_wa = findViewById(R.id.txtsqaure_wa);
        EditText txtngar = findViewById(R.id.txtngar);
        EditText txtrai = findViewById(R.id.txtrai);
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
        String sqaure_meters = txtsqaure_meters.getText().toString().trim();
        String sqaure_wa = txtsqaure_wa.getText().toString().trim();
        String ngar = txtngar.getText().toString().trim();
        String rai = txtrai.getText().toString().trim();
        String cropid = txtcropid.getText().toString().trim();
        String how_plant = txthow_plant.getText().toString().trim();
        String note = txtnote.getText().toString().trim();

        if(plantingdate.equals("") || exp_harvestDate.equals("") || pay.equals("") ||
                discard.equals("") || plant.equals("") ||
                cropid.equals("") || how_plant.equals("")){
            if(plantingdate.equals("")){
                txtplantingdate.setError("กรุณากรอกวันที่ปลูก");
            }else{
                txtplantingdate.setError(null);
            }

            if(plantingdate.equals("")){
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

            if(sqaure_meters.equals("") && sqaure_wa.equals("")
                    && ngar.equals("") && rai.equals("")){
                txtarea.setError("กรุณากรอกพื้นที่ที่ปลูก");
            }else if(sqaure_meters.equals("0") && sqaure_wa.equals("0")
                    && ngar.equals("0") && rai.equals("0")){
                txtarea.setError("พื้นที่ที่ปลูกทั้งหมดต้องไม่ใช่เลข 0");
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

                    TextView txtid = findViewById(R.id.txtid);
                    TextView txtplantingdate = findViewById(R.id.txtplantingdate);
                    TextView txtexp_harvestDate = findViewById(R.id.txtexp_harvestDate);
                    TextView txtpay = findViewById(R.id.txtpay);
                    TextView txtdiscard = findViewById(R.id.txtdiscard);
                    TextView txtplant = findViewById(R.id.txtplant);
                    TextView txtsqaure_meters = findViewById(R.id.txtsqaure_meters);
                    TextView txtsqaure_wa = findViewById(R.id.txtsqaure_wa);
                    TextView txtngar = findViewById(R.id.txtngar);
                    TextView txtrai = findViewById(R.id.txtrai);
                    TextView txtcropid = findViewById(R.id.txtcropid);
                    TextView txthow_plant = findViewById(R.id.txthow_plant);
                    TextView txtnote = findViewById(R.id.txtnote);
                    Spinner spinner_status = findViewById(R.id.spinner_status);

                    plantingModel.getPlanting().setPlantID(txtid.getText().toString());
                    plantingModel.getPlanting().setPlantDate(txtplantingdate.getText().toString());
                    plantingModel.getPlanting().setExp_harvestDate(txtexp_harvestDate.getText().toString());
                    plantingModel.getPlanting().setPay(txtpay.getText().toString());
                    plantingModel.getPlanting().setDiscard(txtdiscard.getText().toString());
                    plantingModel.getPlanting().setPlant(txtplant.getText().toString());
                    if(txtsqaure_meters.getText().toString().equals("")){
                        plantingModel.getPlanting().setSqaure_meters("0");
                    }else{
                        plantingModel.getPlanting().setSqaure_meters(txtsqaure_meters.getText().toString());
                    }
                    if(txtsqaure_wa.getText().toString().equals("")){
                        plantingModel.getPlanting().setSqaure_wa("0");
                    }else{
                        plantingModel.getPlanting().setSqaure_wa(txtsqaure_wa.getText().toString());
                    }
                    if(txtngar.getText().toString().equals("")){
                        plantingModel.getPlanting().setNgar("0");
                    }else{
                        plantingModel.getPlanting().setNgar(txtngar.getText().toString());
                    }
                    if(txtrai.getText().toString().equals("")){
                        plantingModel.getPlanting().setRai("0");
                    }else{
                        plantingModel.getPlanting().setRai(txtrai.getText().toString());
                    }
                    plantingModel.getPlanting().setCropid(txtcropid.getText().toString());
                    plantingModel.getPlanting().setHow_plant(txthow_plant.getText().toString());
                    plantingModel.getPlanting().setNote(txtnote.getText().toString());
                    plantingModel.getPlanting().setStatus(spinner_status.getSelectedItem().toString());

                    manager.update_planting(plantingModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Log.e("Respone", response.toString());
                            Toast.makeText(EditPlantingActivity.this, "แก้ไขข้อมูลการเพาะปลูกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditPlantingActivity.this, ListPlantingActivity.class);
                            startActivity(intent);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(EditPlantingActivity.this, "ไม่สามารถแก้ไขข้อมูลการเพาะปลูกได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(EditPlantingActivity.this, "ไม่สามารถแก้ไขข้อมูลการเพาะปลูกได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}