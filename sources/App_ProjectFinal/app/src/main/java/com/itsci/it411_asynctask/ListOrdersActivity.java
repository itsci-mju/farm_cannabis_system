package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsci.manager.WSManager;
import com.itsci.model.CustomerModel2;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.OrderModel;
import com.itsci.model.OrderModel2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListOrdersActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListOrdersActivity.this);
        final String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        OrderModel orderModel = new OrderModel();
        manager.listAllOrder(orderModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                OrderModel2.Order[] list = (OrderModel2.Order[]) response;
                if(list.length > 0){
                    int orderid = 1;

                    for(int i = 0; i < list.length; i++) {
                        int q = 0;
                        for(int j = 0; j < i; j++) {
                            if (list[j].getStatus().equals("รอการยืนยัน")) {
                                q++;
                            }
                        }
                        if (list[i].getCustomer().getUser().getUsername().equals(saveusername)) {
                            LinearLayout ll = findViewById(R.id.list_order);
                            final View v = getLayoutInflater().inflate(R.layout.list_order, null);

                            TextView id = v.findViewById(R.id.orderid);
                            id.setText(String.valueOf(i+1));

                            TextView showorderid = v.findViewById(R.id.showorderid);
                            showorderid.setText(String.valueOf(orderid));

                            TextView orderdate = v.findViewById(R.id.orderdate);
                            Date dorderdate = new Date(list[i].getOrderDate());
                            String sorderdate = sdf.format(dorderdate);
                            orderdate.setText(sorderdate);

                            TextView receivedate = v.findViewById(R.id.receivedate);
                            Date dreceivedate = new Date(list[i].getReceiveDate());
                            String sreceivedate = sdf.format(dreceivedate);
                            receivedate.setText(sreceivedate);

                            TextView status = v.findViewById(R.id.status);
                            status.setText(list[i].getStatus());

                            TextView queue = v.findViewById(R.id.queue);
                            queue.setText(String.valueOf(q));

                            Button btnReceipt = v.findViewById(R.id.btnReceipt);
                            Button btnPayment = v.findViewById(R.id.btnPayment);
                            LinearLayout queue_layout = v.findViewById(R.id.queue_layout);

                            if (status.getText().toString().equals("รอการยืนยัน")) {
                                queue_layout.setVisibility(View.VISIBLE);
                                btnReceipt.setVisibility(View.GONE);
                                btnPayment.setEnabled(false);
                                btnPayment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0ECDE")));
                                btnPayment.setTextColor(Color.parseColor("#68B2A0"));
                                status.setTextColor(Color.parseColor("#FF131313"));
                            } else if (status.getText().toString().equals("ยืนยันแล้ว")) {
                                btnReceipt.setVisibility(View.GONE);
                                btnPayment.setEnabled(true);
                                btnPayment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#629F41")));
                                status.setTextColor(Color.parseColor("#FFFF6F00"));
                            } else if (status.getText().toString().equals("ปฏิเสธ")) {
                                btnReceipt.setVisibility(View.GONE);
                                btnPayment.setEnabled(false);
                                btnPayment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0ECDE")));
                                btnPayment.setTextColor(Color.parseColor("#68B2A0"));
                                status.setTextColor(Color.parseColor("#e6031d"));
                            } else if (status.getText().toString().equals("รอการตรวจสอบชำระเงิน")) {
                                btnReceipt.setVisibility(View.GONE);
                                btnPayment.setEnabled(false);
                                btnPayment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0ECDE")));
                                btnPayment.setTextColor(Color.parseColor("#68B2A0"));
                                status.setTextColor(Color.parseColor("#FF131313"));
                            } else if (status.getText().toString().equals("ชำระเงินแล้ว")) {
                                btnReceipt.setVisibility(View.VISIBLE);
                                btnPayment.setEnabled(false);
                                btnPayment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0ECDE")));
                                btnPayment.setTextColor(Color.parseColor("#68B2A0"));
                                status.setTextColor(Color.parseColor("#068C13"));
                            }
                            orderid++;

                            final double[] totalprice = {0};
                            manager.listOrderDetailByOrderID(String.valueOf(i+1), new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    OrderDetailModel2.OrderDetail[] list = (OrderDetailModel2.OrderDetail[]) response;
                                    for(int i = 0; i < list.length; i++) {
                                        Log.e("list", list[i].getProduct().getProductname() + " " + String.valueOf(list[i].getQty()) );
                                        final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_order, null);
                                        LinearLayout ll2 = v.findViewById(R.id.list_in_list_order);

                                        TextView txt = v2.findViewById(R.id.txt);
                                        txt.setText(list[i].getProduct().getProductname() + " x " + list[i].getQty() + " กิโลกรัม" +
                                                " ฿" + formatter.format(Double.parseDouble(list[i].getTotalprice()))  );

                                        totalprice[0] += Double.parseDouble(list[i].getTotalprice());
                                        TextView txttotalprice = v.findViewById(R.id.totalprice);
                                        txttotalprice.setText("฿" + formatter.format(totalprice[0]));

                                        ll2.addView(v2);
                                    }
                                }
                                @Override
                                public void onError(String err) {
                                    loadingDialog.dismiss();
                                }
                            });

                            ll.addView(v);
                        }
                    }

                    LinearLayout ll = findViewById(R.id.list_order);

                    for (int i = 0; i < ll.getChildCount(); i++) {
                        View v = ll.getChildAt(i);

                        final TextView id = v.findViewById(R.id.orderid);
                        final TextView orderdate = v.findViewById(R.id.orderdate);
                        final TextView receivedate = v.findViewById(R.id.receivedate);
                        final TextView status = v.findViewById(R.id.status);

                        Button btnReceipt = v.findViewById(R.id.btnReceipt);
                        btnReceipt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListOrdersActivity.this, ReceiptActivity.class);

                                intent.putExtra("orderid", id.getText().toString());
                                intent.putExtra("orderdate", orderdate.getText().toString());
                                intent.putExtra("receivedate", receivedate.getText().toString());
                                intent.putExtra("status", status.getText().toString());

                                startActivity(intent);
                            }
                        });

                        Button btnPayment = v.findViewById(R.id.btnPayment);
                        btnPayment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListOrdersActivity.this, PaymentActivity.class);

                                intent.putExtra("orderid", id.getText().toString());
                                intent.putExtra("orderdate", orderdate.getText().toString());
                                intent.putExtra("receivedate", receivedate.getText().toString());
                                intent.putExtra("status", status.getText().toString());

                                startActivity(intent);
                            }
                        });
                    }

                }else{
                    View v = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                    LinearLayout ll = findViewById(R.id.list_order);
                    ll.addView(v);
                    loadingDialog.dismiss();
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
        Intent intent = new Intent(ListOrdersActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ListOrdersActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListOrdersActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListOrdersActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ListOrdersActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}