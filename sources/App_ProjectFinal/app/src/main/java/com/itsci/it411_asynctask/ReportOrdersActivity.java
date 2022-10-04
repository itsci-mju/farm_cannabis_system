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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.itsci.model.HarvestModel2;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.OrderModel2;
import com.itsci.model.ProductModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportOrdersActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_orders);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReportOrdersActivity.this);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReportOrdersActivity.this,
                android.R.layout.select_dialog_item, list_year);

        spinner_year.setAdapter(adapter);
        spinner_year.setSelection(adapter.getPosition(String.valueOf(year)));

        final WSManager manager = WSManager.getWsManager(this);

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manager.listOrderByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        OrderModel2.Order[] list = (OrderModel2.Order[]) response;

                        TextView orderlenght = findViewById(R.id.orderlenght);
                        orderlenght.setText(String.valueOf(list.length));

                        TextView txtwaitconfirm = findViewById(R.id.txtwaitconfirm);
                        TextView txtconfirmorders = findViewById(R.id.txtconfirmorders);
                        TextView txtwaitpay = findViewById(R.id.txtwaitpay);
                        TextView txtconfirmpay = findViewById(R.id.txtconfirmpay);
                        TextView txtdenied = findViewById(R.id.txtdenied);

                        int waitconfirm = 0;
                        int confirmorders = 0;
                        int waitpay = 0;
                        int confirmpay = 0;
                        int denied = 0;

                        for(int i = 0; i < list.length; i++){
                            String status = list[i].getStatus();
                            if(status.equals("รอการยืนยัน")){
                                waitconfirm++;
                            }else if(status.equals("ยืนยันแล้ว")){
                                confirmorders++;
                            }else if(status.equals("รอการตรวจสอบชำระเงิน")){
                                waitpay++;
                            }else if(status.equals("ชำระเงินแล้ว")){
                                confirmpay++;
                            }else if(status.equals("ปฏิเสธ")){
                                denied++;
                            }
                        }

                        txtwaitconfirm.setText(String.valueOf(waitconfirm));

                        txtconfirmorders.setText(String.valueOf(confirmorders));

                        txtwaitpay.setText(String.valueOf(waitpay));

                        txtconfirmpay.setText(String.valueOf(confirmpay));

                        txtdenied.setText(String.valueOf(denied));

                        LinearLayout ll = findViewById(R.id.list_detail_order_in_report);
                        ll.removeAllViews();

                        LinearLayout lp = findViewById(R.id.list_partname_in_report_orders);
                        lp.removeAllViews();

                        String list_partname[] = {"ใบ", "ราก", "ลำต้น", "ยอดอ่อน"};

                        ProductModel productModel = new ProductModel();
                        manager.listProduct(productModel, new WSManager.WSManagerListener() {
                            @Override
                            public void onComplete(Object response) {
                                ProductModel.Product[] p = (ProductModel.Product[]) response;

                                /*for(int i = 0; i < list_partname.length; i++){
                                    int sum = 0;
                                    View v = lp.getChildAt(i);
                                    for(int j = 0; j < p.length; j++) {
                                        if (p[j].getProductname().contains(list_partname[i])) {
                                            sum++;
                                        }

                                        if(sum == 1) {
                                            TextView partname = v.findViewById(R.id.partname);
                                            partname.setText(list_partname[i]);
                                        }
                                    }

                                    ViewGroup.LayoutParams params = v.getLayoutParams();
                                    params.height = 79*sum;
                                    v.setLayoutParams(params);
                                }*/

                                manager.listHarvestByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        HarvestModel2.Harvest[] list = (HarvestModel2.Harvest[]) response;

                                        for (int i = 0; i < p.length; i++) {
                                            View v = getLayoutInflater().inflate(R.layout.list_partname_in_report_orders, null);
                                            TextView partname = v.findViewById(R.id.partname);
                                            partname.setText("");

                                            TextView harvest = v.findViewById(R.id.harvest);
                                            harvest.setText("");

                                            CardView cv = v.findViewById(R.id.CV);
                                            cv.setCardElevation(0);

                                            lp.addView(v);
                                        }

                                        int sum = 0;

                                        for (int i = 0; i < list_partname.length; i++) {
                                            int no = 0;
                                            View v = lp.getChildAt(sum);
                                            for (int j = 0; j < p.length; j++) {
                                                if (p[j].getProductname().contains(list_partname[i])) {
                                                    if (no == 0) {
                                                        TextView partname = v.findViewById(R.id.partname);
                                                        partname.setText(list_partname[i]);

                                                        double sumharvest = 0;

                                                        for(int k = 0; k < list.length; k++){
                                                            if(list[k].getPartName().equals(list_partname[i])){
                                                                sumharvest += Double.parseDouble(list[k].getQty());
                                                            }
                                                        }
                                                        TextView harvest = v.findViewById(R.id.harvest);
                                                        harvest.setText(String.valueOf(sumharvest));
                                                    }
                                                    sum++;
                                                    no++;
                                                }
                                                View v1 = lp.getChildAt(sum-1);
                                                CardView cv = v1.findViewById(R.id.CV);
                                                if(j == no){
                                                    cv.setCardElevation(5);
                                                }else if(j == p.length-1){
                                                    cv.setCardElevation(5);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(String err) {

                                    }
                                });

                                final double[] totalsum = {0};
                                for(int i = 0; i < p.length; i++){
                                    final View v = getLayoutInflater().inflate(R.layout.list_detail_order_in_report, null);

                                    TextView productname = v.findViewById(R.id.productname);
                                    productname.setText(p[i].getProductname());

                                    int finalI = i;
                                    manager.listOrderDetailByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            OrderDetailModel2.OrderDetail[] list = (OrderDetailModel2.OrderDetail[]) response;

                                            int totalqty = 0;

                                            for(int i = 0; i < list.length; i++){
                                                if(list[i].getProduct().getProductname().equals(productname.getText().toString()) && list[i].getOrder().getStatus().equals("ชำระเงินแล้ว")){
                                                    totalqty += Double.parseDouble(list[i].getQty());
                                                }
                                            }

                                            TextView qty = v.findViewById(R.id.qty);
                                            qty.setText(String.valueOf(totalqty));

                                            double sum = 0;

                                            for(int i = 0; i < list.length; i++) {
                                                if(list[i].getProduct().getProductname().equals(productname.getText().toString())  && list[i].getOrder().getStatus().equals("ชำระเงินแล้ว")) {
                                                    sum = totalqty * Double.parseDouble(list[i].getProduct().getPrice());
                                                    totalsum[0] += sum;
                                                    break;
                                                }

                                            }

                                            TextView sumprice = v.findViewById(R.id.sumprice);
                                            if(sum > 0){
                                                sumprice.setText(formatter.format(sum));
                                            }else{
                                                sumprice.setText("0.00");
                                            }

                                            TextView txttotalsum = findViewById(R.id.totalsum);
                                            txttotalsum.setText(formatter.format(totalsum[0]));

                                            ll.addView(v);

                                        }

                                        @Override
                                        public void onError(String err) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(String err) {

                            }
                        });

                        LinearLayout ll_piechart_order = findViewById(R.id.ll_piechart_order);
                        CardView cv_detail_order = findViewById(R.id.cv_detail_order);
                        CardView cv_total_receive = findViewById(R.id.cv_total_receive);

                        if(list.length > 0){
                            ll_piechart_order.setVisibility(View.VISIBLE);
                            cv_detail_order.setVisibility(View.VISIBLE);
                            cv_total_receive.setVisibility(View.VISIBLE);
                        }else{
                            ll_piechart_order.setVisibility(View.GONE);
                            cv_detail_order.setVisibility(View.GONE);
                            cv_total_receive.setVisibility(View.GONE);
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
        ProductModel productModel = new ProductModel();
        manager.listProduct(productModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                final ProductModel.Product[] products = (ProductModel.Product[]) response;

                final Spinner spinner_year = findViewById(R.id.spinner_year);
                manager.listOrderDetailByYear(spinner_year.getSelectedItem().toString(), new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        OrderDetailModel2.OrderDetail[] list = (OrderDetailModel2.OrderDetail[]) response;

                        List<Integer> qty = new ArrayList<>();
                        for(int i = 0; i < products.length; i++){
                            qty.add(0);
                        }

                        for(int i = 0; i < list.length; i++){
                            for(int j = 0; j < products.length; j++){
                                if(list[i].getProduct().getProductname().equals(products[j].getProductname())){
                                    int sum = qty.get(j) + Integer.parseInt(list[i].getQty());
                                    qty.set(j, sum);
                                }
                            }
                        }

                        ArrayList <PieEntry> entries = new ArrayList<>();
                        for(int i = 0; i < products.length; i++){
                            if(qty.get(i) > 0){
                                entries.add(new PieEntry(qty.get(i), products[i].getProductname()));
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

                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(String err) {
                        loadingDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(String err) {
            }
        });


    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ReportOrdersActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ReportOrdersActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ReportOrdersActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReportOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ReportOrdersActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReportOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ReportOrdersActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}