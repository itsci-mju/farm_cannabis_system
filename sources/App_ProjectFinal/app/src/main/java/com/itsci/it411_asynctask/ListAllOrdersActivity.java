package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.OrderModel;
import com.itsci.model.OrderModel2;
import com.itsci.model.PaymentModel2;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListAllOrdersActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_orders);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListAllOrdersActivity.this);
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
                Log.e("size", String.valueOf(list.length));
                if(list.length > 0){
                    for(int i = 0; i < list.length; i++) {
                        final int finalI = i;

                        Log.e("order", list[i].getOrderID() + " " + list[i].getOrderDate() + " " +
                                "" + list[i].getReceiveDate() + " " + list[i].getStatus() + " " +
                                "" + list[i].getCustomer().getUser().getUsername());

                        final View v = getLayoutInflater().inflate(R.layout.list_all_order, null);
                        final LinearLayout ll = findViewById(R.id.list_all_order);

                        TextView id = v.findViewById(R.id.orderid);
                        id.setText(list[finalI].getOrderID());

                        TextView orderdate = v.findViewById(R.id.persontype);
                        Date dorderdate = new Date(list[finalI].getOrderDate());
                        String sorderdate = sdf.format(dorderdate);
                        orderdate.setText(sorderdate);

                        TextView receivedate = v.findViewById(R.id.txtfullname);
                        Date dreceivedate = new Date(list[finalI].getReceiveDate());
                        String sreceivedate = sdf.format(dreceivedate);
                        receivedate.setText(sreceivedate);

                        TextView status = v.findViewById(R.id.txtcompany);
                        status.setText(list[finalI].getStatus());

                        Button btnSubmit = v.findViewById(R.id.btnSubmit);
                        if(status.getText().toString().equals("รอการยืนยัน")){
                            btnSubmit.setEnabled(true);
                            status.setTextColor(Color.parseColor("#FF131313"));
                        }else if(status.getText().toString().equals("ยืนยันแล้ว")){
                            btnSubmit.setEnabled(false);
                            btnSubmit.setVisibility(View.GONE);
                            status.setTextColor(Color.parseColor("#FFFF6F00"));
                        }else if(status.getText().toString().equals("ปฏิเสธ")){
                            btnSubmit.setEnabled(false);
                            btnSubmit.setVisibility(View.GONE);
                            status.setTextColor(Color.parseColor("#e6031d"));
                        }else if(status.getText().toString().equals("รอการตรวจสอบชำระเงิน")){
                            btnSubmit.setEnabled(false);
                            btnSubmit.setVisibility(View.GONE);
                            status.setTextColor(Color.parseColor("#FF131313"));
                        }else if(status.getText().toString().equals("ชำระเงินแล้ว")){
                            btnSubmit.setEnabled(false);
                            btnSubmit.setVisibility(View.GONE);
                            status.setTextColor(Color.parseColor("#068C13"));
                        }

                        TextView name = v.findViewById(R.id.customername);
                        name.setText(list[i].getCustomer().getUser().getUsername());

                        OrderDetailModel2 orderDetailModel = new OrderDetailModel2();
                        final double[] totalprice = {0};
                        manager.listOrderDetailByOrderID(list[finalI].getOrderID(), new WSManager.WSManagerListener() {
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

                        manager.getPaymentByOrderID(list[finalI].getOrderID(), new WSManager.WSManagerListener() {
                            @Override
                            public void onComplete(Object response) {
                                if(response != null){
                                    PaymentModel2.Payment payment = (PaymentModel2.Payment) response;
                                    TextView paydate = v.findViewById(R.id.paydate);
                                    Date dpaydate = new Date(payment.getPaydate());
                                    String spaydate = sdf.format(dpaydate);
                                    paydate.setText(spaydate);

                                    ImageView imagePayment = v.findViewById(R.id.ImagePayment);
                                    Bitmap imagePaymentBitmap = ImageUtil.convert(payment.getImgPayment());
                                    imagePayment.setImageBitmap(imagePaymentBitmap);
                                }

                            }

                            @Override
                            public void onError(String err) {
                                loadingDialog.dismiss();
                            }
                        });

                        ll.addView(v);
                    }

                    loadingDialog.dismiss();

                    LinearLayout ll = findViewById(R.id.list_all_order);

                    for(int i = 0; i < ll.getChildCount(); i++) {
                        View v = ll.getChildAt(i);
                        final TextView id = v.findViewById(R.id.orderid);
                        final TextView status = v.findViewById(R.id.txtcompany);
                        Button btnSubmit = v.findViewById(R.id.btnSubmit);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.confirmOrder(id.getText().toString(), new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        Toast.makeText(ListAllOrdersActivity.this, "ยืนยันคำสั่งจอง รหัส " + id.getText().toString() + " " +
                                                "เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                        Intent refresh = new Intent(ListAllOrdersActivity.this, ListAllOrdersActivity.class);
                                        startActivity(refresh);
                                        finish();

                                    }
                                    @Override
                                    public void onError(String err) {
                                        Toast.makeText(ListAllOrdersActivity.this, "ไม่สามารถยืนยันคำสั่งจองนี้ได้", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            }
                        });

                        Button btnChangeStatus = v.findViewById(R.id.btnChangeStatus);
                        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.getOrder().setOrderID(id.getText().toString());
                                orderModel.getOrder().setStatus(status.getText().toString());

                                Intent intent = new Intent(ListAllOrdersActivity.this, EditOrderStatusActivity.class);

                                intent.putExtra("orderid", id.getText().toString());
                                intent.putExtra("status", status.getText().toString());

                                startActivity(intent);
                            }
                        });
                    }
                }else{
                    View v = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                    LinearLayout ll = findViewById(R.id.list_all_order);
                    ll.addView(v);
                    loadingDialog.dismiss();
                }
            }
            @Override
            public void onError(String err) {
                Toast.makeText(ListAllOrdersActivity.this, "ไม่สามารถยืนยันคำสั่งจองนี้ได้", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ListAllOrdersActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ListAllOrdersActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListAllOrdersActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListAllOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListAllOrdersActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListAllOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ListAllOrdersActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}