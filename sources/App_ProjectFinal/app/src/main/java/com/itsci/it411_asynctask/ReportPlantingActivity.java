package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itsci.manager.WSManager;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;
import com.itsci.model.PlantingModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportPlantingActivity extends AppCompatActivity {
    String pattern = "MMMM";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("th", "TH"));
    String pattern2 = "MM";
    SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2, new Locale("th", "TH"));

    private PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_planting);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReportPlantingActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        String[] list_year = new String[5];

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        for(int i = 0; i < 5; i++){
            list_year[i] = String.valueOf(year-i);
        }

        final Spinner spinner_year = findViewById(R.id.spinner_year);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReportPlantingActivity.this,
                android.R.layout.select_dialog_item, list_year);

        spinner_year.setAdapter(adapter);
        spinner_year.setSelection(adapter.getPosition(String.valueOf(year)));


        final WSManager manager = WSManager.getWsManager(this);

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manager.listPlantingByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        PlantingModel.Planting[] list = (PlantingModel.Planting[]) response;

                        TextView plantlenght = findViewById(R.id.plantlenght);
                        plantlenght.setText(String.valueOf(list.length));

                        TextView txtqtypay = findViewById(R.id.txtqtypay);
                        TextView txtqtydiscard = findViewById(R.id.txtqtydiscard);
                        TextView txtqtyplant = findViewById(R.id.txtqtyplant);

                        int qtypay = 0;
                        int qtydiscard = 0;
                        int qtyplant = 0;

                        for(int i = 0; i < list.length; i++){
                            int pay = Integer.parseInt(list[i].getPay());
                            int discard = Integer.parseInt(list[i].getDiscard());
                            int plant = Integer.parseInt(list[i].getPlant());
                            qtypay += pay;
                            qtydiscard += discard;
                            qtyplant += plant;
                        }

                        txtqtypay.setText(String.valueOf(qtypay));

                        txtqtydiscard.setText(String.valueOf(qtydiscard));

                        txtqtyplant.setText(String.valueOf(qtyplant));

                        LinearLayout ll = findViewById(R.id.list_detail_planting_in_report);
                        ll.removeAllViews();
                        for(int i = 0; i < list.length; i++){
                            View v = getLayoutInflater().inflate(R.layout.list_detail_planting_in_report, null);

                            TextView pid = v.findViewById(R.id.pid);
                            pid.setText(String.valueOf(i+1));

                            TextView month = v.findViewById(R.id.month);
                            Date pdate = new Date(list[i].getPlantDate());
                            String sdate = sdf.format(pdate);
                            month.setText(sdate);

                            TextView pay = v.findViewById(R.id.pay);
                            pay.setText(list[i].getPay());

                            TextView discard = v.findViewById(R.id.discard);
                            discard.setText(list[i].getDiscard());

                            TextView plant = v.findViewById(R.id.plant);
                            plant.setText(list[i].getPlant());

                            ll.addView(v);
                        }
                    }

                    @Override
                    public void onError(String err) {

                    }
                });

                pieChart = findViewById(R.id.pie_chart);
                setupPieChart();
                loadPieChartData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        //pieChart.setEntryLabelTextSize(12);
        //pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.WHITE);
        l.setTextSize(12);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        Spinner spinner_year = findViewById(R.id.spinner_year);
        manager.listHarvestByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;

                List<String> partname = new ArrayList<>();

                for(int i = 0; i < list.length; i++){
                    Log.e("partname", list[i].getPartName());
                    if(i == 0){
                        partname.add(list[i].getPartName());
                    }else{
                        boolean flag = false;
                        for(int j = 0; j < partname.size(); j++){
                            if(list[i].getPartName().equals(list[j].getPartName())){
                                flag = true;
                            }
                        }
                        if(flag == false){
                            partname.add(list[i].getPartName());
                        }
                    }
                }

                List<Float> qty = new ArrayList<>();
                for(int i = 0; i < partname.size(); i++){
                    qty.add((float) 0);
                }
                for(int i = 0; i < list.length; i++){
                    for(int j = 0; j < partname.size(); j++){
                        if(list[i].getPartName().equals(partname.get(j))){
                            float sum = (float) (qty.get(j) + Double.parseDouble(list[i].getQty()));
                            qty.set(j, (float) sum);
                        }
                    }
                }


                ArrayList<PieEntry> entries = new ArrayList<>();
                for(int i = 0; i < qty.size(); i++){
                    if(qty.get(i) > 0){
                        entries.add(new PieEntry(qty.get(i), partname.get(i)));
                    }
                }

                ArrayList<Integer> colors = new ArrayList<>();
                for (int color: ColorTemplate.MATERIAL_COLORS) {
                    colors.add(color);
                }

                for (int color: ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(pieChart));
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);

                pieChart.setData(data);
                pieChart.invalidate();

                pieChart.animateY(1400, Easing.EaseInOutQuad);

                LinearLayout ll = findViewById(R.id.list_detail_harvest_in_report);
                ll.removeAllViews();

                List<String> months = new ArrayList<>();
                List<String> months2 = new ArrayList<>();

                for(int i = 0; i < list.length; i++){
                    Date pmonth = new Date(list[i].getHarvestDate());
                    String smonth = sdf.format(pmonth);
                    String nmonth = sdf2.format(pmonth);
                    if(i == 0){
                        months.add(nmonth);
                        months2.add(smonth);
                    }else{
                        boolean flag = false;
                        for(int j = 0; j < months.size(); j++){
                            if(nmonth.equals(months.get(j))){
                                flag = true;
                            }
                        }
                        if(flag == false){
                            months.add(nmonth);
                            months2.add(smonth);
                        }
                    }
                }
                Log.e("months size : ", String.valueOf(months.size()));
                Log.e("months info : ", months.toString());

                int id = 1;
                for(int m = 0; m < months.size(); m++){
                    View v = getLayoutInflater().inflate(R.layout.list_detail_harvest_in_report, null);
                    TextView hid = v.findViewById(R.id.hid);
                    hid.setText(String.valueOf(id));

                    TextView month = v.findViewById(R.id.month);
                    month.setText(months2.get(m));

                    HarvestModel harvestModel = new HarvestModel();
                    harvestModel.getHarvest().setHarvestID(list[m].getHarvestID());
                    harvestModel.getHarvest().setPlanting(list[m].getPlanting().getPlantID());

                    if(m == 0) {
                        id += 1;
                        ll.addView(v);

                        manager.listHarvestByMonth(months.get(m), new WSManager.WSManagerListener() {
                            @Override
                            public void onComplete(Object response) {
                                HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;

                                List<String> partname2 = new ArrayList<>();
                                for(int i = 0; i < list.length; i++){
                                    if(i == 0){
                                        partname2.add(list[i].getPartName());
                                    }else{
                                        boolean flag = false;
                                        for(int j = 0; j < partname2.size(); j++){
                                            if(list[i].getPartName().equals(partname2.get(j))){
                                                flag = true;
                                            }
                                        }
                                        if(flag == false){
                                            partname2.add(list[i].getPartName());
                                        }
                                    }
                                    Log.e("partname2 size : ", String.valueOf(partname2.size()));
                                    Log.e("partname2 info : ", partname2.toString());
                                }

                                List<Float> qty2 = new ArrayList<>();
                                for(int i = 0; i < partname2.size(); i++){
                                    qty2.add((float) 0);
                                }
                                for(int i = 0; i < list.length; i++){
                                    for(int j = 0; j < partname2.size(); j++){
                                        if(list[i].getPartName().equals(partname2.get(j))){
                                            float sum = (float) (qty2.get(j) + Double.parseDouble(list[i].getQty()));
                                            qty2.set(j, (float) sum);
                                        }
                                    }
                                }
                                Log.e("qty2 size : ", String.valueOf(qty2.size()));
                                Log.e("qty2 info : ", qty2.toString());

                                for (int i = 0; i < partname2.size(); i++) {
                                    final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_harvest_in_report, null);
                                    LinearLayout ll2 = v.findViewById(R.id.list_in_list_harvest);

                                    TextView partname = v2.findViewById(R.id.partname);
                                    partname.setText(partname2.get(i));

                                    TextView qty = v2.findViewById(R.id.qty);
                                    qty.setText(String.format("%.1f", qty2.get(i)));

                                    TextView unit = v2.findViewById(R.id.sumprice);
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
                        if(flag == false){
                            id += 1;
                            ll.addView(v);

                            manager.listHarvestByMonth(months.get(m), new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;

                                    List<String> partname2 = new ArrayList<>();
                                    for(int i = 0; i < list.length; i++){
                                        if(i == 0){
                                            partname2.add(list[i].getPartName());
                                        }else{
                                            boolean flag = false;
                                            for(int j = 0; j < partname2.size(); j++){
                                                if(list[i].getPartName().equals(partname2.get(j))){
                                                    flag = true;
                                                }
                                            }
                                            if(flag == false){
                                                partname2.add(list[i].getPartName());
                                            }
                                        }
                                        Log.e("partname2 size : ", String.valueOf(partname2.size()));
                                        Log.e("partname2 info : ", partname2.toString());
                                    }

                                    List<Float> qty2 = new ArrayList<>();
                                    for(int i = 0; i < partname2.size(); i++){
                                        qty2.add((float) 0);
                                    }
                                    for(int i = 0; i < list.length; i++){
                                        for(int j = 0; j < partname2.size(); j++){
                                            if(list[i].getPartName().equals(partname2.get(j))){
                                                float sum = (float) (qty2.get(j) + Double.parseDouble(list[i].getQty()));
                                                qty2.set(j, (float) sum);
                                            }
                                        }
                                    }
                                    Log.e("qty2 size : ", String.valueOf(qty2.size()));
                                    Log.e("qty2 info : ", qty2.toString());

                                    for (int i = 0; i < partname2.size(); i++) {
                                        final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_harvest_in_report, null);
                                        LinearLayout ll2 = v.findViewById(R.id.list_in_list_harvest);

                                        TextView partname = v2.findViewById(R.id.partname);
                                        partname.setText(partname2.get(i));

                                        TextView qty = v2.findViewById(R.id.qty);
                                        qty.setText(String.format("%.1f", qty2.get(i)));

                                        TextView unit = v2.findViewById(R.id.sumprice);
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
                }



                CardView cv_detail_planting = findViewById(R.id.cv_detail_planting);
                LinearLayout ll_piechart_planting = findViewById(R.id.ll_piechart_planting);
                CardView cv_detail_harvest = findViewById(R.id.cv_detail_harvest);

                if(list.length > 0){
                    cv_detail_planting.setVisibility(View.VISIBLE);
                    ll_piechart_planting.setVisibility(View.VISIBLE);
                    cv_detail_harvest.setVisibility(View.VISIBLE);
                }else{
                    cv_detail_planting.setVisibility(View.GONE);
                    ll_piechart_planting.setVisibility(View.GONE);
                    cv_detail_harvest.setVisibility(View.GONE);
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onError(String err) {
                loadingDialog.dismiss();
            }
        });

    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ReportPlantingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ReportPlantingActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ReportPlantingActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReportPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ReportPlantingActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReportPlantingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ReportPlantingActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}