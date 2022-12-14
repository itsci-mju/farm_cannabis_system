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
            builder.setMessage("???????????????????????????????????????????????????????????????????????????????????? ?");
            builder.setNeutralButton("?????????", new DialogInterface.OnClickListener() {
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
            builder.setMessage("????????????????????????????????????????????????????????????????????????????????? ?");
            builder.setNeutralButton("?????????", new DialogInterface.OnClickListener() {
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
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
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
        dpd.getDatePicker().setMinDate(c.getTimeInMillis());
        dpd.show();
    }

    public void onSubmitAddPlanting(View view){
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
                txtplantingdate.setError("?????????????????????????????????????????????????????????");
            }else{
                txtplantingdate.setError(null);
            }

            if(exp_harvestDate.equals("")){
                txtplantingdate.setError("???????????????????????????????????????????????????????????????????????????????????????????????????");
            }else{
                txtplantingdate.setError(null);
            }

            if(pay.equals("")) {
                txtpay.setError("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            }else if(pay.equals("0")){
                txtpay.setError("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? 0");
            }else{
                txtpay.setError(null);
            }

            if(discard.equals("")){
                txtdiscard.setError("???????????????????????????????????????????????????????????????");
            }else{
                txtdiscard.setError(null);
            }

            if(plant.equals("")) {
                txtplant.setError("????????????????????????????????????????????????????????????????????????????????????");
            }else if(plant.equals("0")){
                txtplant.setError("????????????????????????????????????????????????????????????????????????????????????????????????????????? 0");
            }else{
                txtplant.setError(null);
            }

            if(sqaure_meters.equals("") && sqaure_wa.equals("")
                && ngar.equals("") && rai.equals("")){
                txtarea.setError("?????????????????????????????????????????????????????????????????????");
            }else if(sqaure_meters.equals("0") && sqaure_wa.equals("0")
                    && ngar.equals("0") && rai.equals("0")){
                txtarea.setError("?????????????????????????????????????????????????????????????????????????????????????????????????????? 0");
            }else{
                txtarea.setError(null);
            }

            if(cropid.equals("")){
                txtcropid.setError("???????????????????????????????????????????????? Crop ID");
            }else{
                txtcropid.setError(null);
            }

            if(how_plant.equals("")){
                txthow_plant.setError("????????????????????????????????????????????????????????????");
            }else{
                txthow_plant.setError(null);
            }

            if(note.length() > 255){
                txtnote.setError("???????????????????????????????????????????????????????????????????????? 255 ?????????????????????????????????");
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
                    TextView txtsqaure_meters = findViewById(R.id.txtsqaure_meters);
                    TextView txtsqaure_wa = findViewById(R.id.txtsqaure_wa);
                    TextView txtngar = findViewById(R.id.txtngar);
                    TextView txtrai = findViewById(R.id.txtrai);
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
                    if (txtnote.getText().toString().equals("")) {
                        plantingModel.getPlanting().setNote("-");
                    } else {
                        plantingModel.getPlanting().setNote(txtnote.getText().toString());
                    }
                    plantingModel.getPlanting().setStatus("???????????????????????????????????????");

                    manager.insert_planting(plantingModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Toast.makeText(AddPlantingActivity.this, "???????????????????????????????????????????????????????????????????????????????????????????????????????????? ???????????? " + plantingModel.getPlanting().getPlantID(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPlantingActivity.this, ListPlantingActivity.class);
                            startActivity(intent);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(AddPlantingActivity.this, "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(AddPlantingActivity.this, "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}