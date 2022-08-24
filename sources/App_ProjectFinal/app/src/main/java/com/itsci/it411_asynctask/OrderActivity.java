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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.OrderDetailModel;
import com.itsci.model.OrderModel;
import com.itsci.model.OrderModel2;
import com.itsci.model.PlantingModel;
import com.itsci.model.ProductModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

    String pattern = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    Calendar exp = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        WSManager manager = WSManager.getWsManager(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OrderActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        final List<String> order =  intent.getStringArrayListExtra("order");

        final double[] totalprice = {0};

        final LinearLayout d = findViewById(R.id.detail);
        for(int i = 0; i < order.size(); i++) {
            View v = getLayoutInflater().inflate(R.layout.order_layout, null);
            String[] arr = order.get(i).split("/");
            double price = 0;

            TextView txtname = v.findViewById(R.id.txt);
            txtname.setText(arr[0]);

            TextView txtqty = v.findViewById(R.id.txtqty);
            txtqty.setText(arr[2]);

            TextView txtprice = v.findViewById(R.id.txtprice);
            price = Double.parseDouble(arr[1]) * Integer.parseInt(arr[2]);
            txtprice.setText(formatter.format(price));

            totalprice[0] += price;

            d.addView(v);
        }

        TextView txttotalprice = findViewById(R.id.txttotalprice);
        txttotalprice.setText("฿" + formatter.format(totalprice[0]));

        ProductModel productModel = new ProductModel();
        manager.listProduct(productModel, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {

                        LinearLayout ll = findViewById(R.id.detail);

                        for(int i = 0; i < ll.getChildCount(); i++){
                            View v = ll.getChildAt(i);
                            final String[] arr = order.get(i).split("/");
                            final TextView txtprice = v.findViewById(R.id.txtprice);
                            final EditText txtqty = v.findViewById(R.id.txtqty);
                            final int finalI = i;
                            Button btnplus = v.findViewById(R.id.btnPlus);
                            btnplus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int qty = Integer.parseInt(txtqty.getText().toString());
                                    qty++;

                                    String[] arr = order.get(finalI).split("/");
                                    order.set(finalI, arr[0] + "/" + arr[1] + "/" + qty + "/" + arr[3]);

                                    String stringqty = String.valueOf(qty);
                                    txtqty.setText(stringqty);

                                    double price = 0;

                                    price = Double.parseDouble(arr[1]) * qty;
                                    txtprice.setText(formatter.format(price));

                                    sumtotal();
                                }
                            });
                            Button btnminus = v.findViewById(R.id.btnMinus);
                            btnminus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int qty = Integer.parseInt(txtqty.getText().toString());

                                    if(qty == 1){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                                        builder.setTitle("แจ้งเตือน");
                                        builder.setMessage("คุณต้องการลบสินค้านี้ใช่หรือไม่ ?");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int item) {
                                                Log.i("Remove", String.valueOf(finalI));
                                                order.remove(finalI);

                                                if(order.size() > 0){

                                                    List<String> neworder = new ArrayList<>();

                                                    for (int i = 0; i < order.size(); i++) {
                                                        String[] arr = order.get(i).split("/");
                                                        neworder.add(i, arr[0] + "/" + arr[1] + "/" + arr[2] + "/" + arr[3]);
                                                    }
                                                    Intent intent = new Intent(OrderActivity.this, OrderActivity.class);
                                                    intent.putStringArrayListExtra("order", (ArrayList<String>) neworder);
                                                    startActivity(intent);
                                                }else{
                                                    Intent intent = new Intent(OrderActivity.this, ProductActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }else{
                                        qty--;

                                        String[] arr = order.get(finalI).split("/");
                                        order.set(finalI, arr[0] + "/" + arr[1] + "/" + qty + "/" + arr[3]);
                                    }
                                    String stringqty = String.valueOf(qty);
                                    txtqty.setText(stringqty);

                                    double price = 0;

                                    price = Double.parseDouble(arr[1]) * qty;
                                    txtprice.setText(formatter.format(price));

                                    sumtotal();
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(String err) {

                    }
                });

        PlantingModel plantingModel = new PlantingModel();
        manager.listPlanting(plantingModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                PlantingModel.Planting[] list = (PlantingModel.Planting[]) response;
                final Calendar now = Calendar.getInstance();
                int year;
                int month;
                int dayOfMonth;
                for(int i = 0; i < list.length; i++){
                    Date dexp = new Date(list[i].getExp_harvestDate());
                    String[] sexp = sdf.format(dexp).split("-");
                    year = Integer.parseInt(sexp[0]);
                    month = Integer.parseInt(sexp[1]);
                    dayOfMonth = Integer.parseInt(sexp[2]);
                    exp.set(year,month,dayOfMonth);
                    if(exp.after(now)){
                        EditText edt = findViewById(R.id.txtreceivedate);
                        if((month) < 10 && dayOfMonth < 10) {
                            edt.setText("0" + dayOfMonth + "-0" + (month) + "-" + year);
                        }else if((month) < 10 && dayOfMonth >= 10){
                            edt.setText(dayOfMonth + "-0" + (month) + "-" + year);
                        }else if((month) >= 10 && dayOfMonth < 10){
                            edt.setText("0" + dayOfMonth + "-" + (month) + "-" + year);
                        }else {
                            edt.setText(dayOfMonth + "-" + (month) + "-" + year);
                        }
                    }
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(OrderActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(OrderActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void sumtotal(){
        LinearLayout ll = findViewById(R.id.detail);
        double totalprice = 0;

        for(int i = 0; i < ll.getChildCount(); i++) {
            View v = ll.getChildAt(i);

            TextView txtprice = v.findViewById(R.id.txtprice);

            double dprice = 0;

            Number number = null;
            try {
                number = format.parse(txtprice.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dprice = number.doubleValue();

            totalprice += dprice;
        }
        TextView txttotalprice = findViewById(R.id.txttotalprice);
        txttotalprice.setText("฿"+formatter.format(totalprice));
    }

    public void onClickReceiveDate(View view){
        int year = exp.get(Calendar.YEAR);
        int month = exp.get(Calendar.MONTH)+1;
        int dayOfMonth = exp.get(Calendar.DAY_OF_MONTH);

        Calendar maxexp = Calendar.getInstance();
        maxexp.set(year,month,dayOfMonth);

        DatePickerDialog dpd = new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtreceivedate);
                if((month) < 10 && dayOfMonth < 10) {
                    edt.setText("0" + dayOfMonth + "-0" + (month) + "-" + year);
                }else if((month) < 10 && dayOfMonth >= 10){
                    edt.setText(dayOfMonth + "-0" + (month) + "-" + year);
                }else if((month) >= 10 && dayOfMonth < 10){
                    edt.setText("0" + dayOfMonth + "-" + (month) + "-" + year);
                }else {
                    edt.setText(dayOfMonth + "-" + (month) + "-" + year);
                }
            }
        }, year, month-1, dayOfMonth);
        dpd.getDatePicker().setMinDate(exp.getTimeInMillis());
        dpd.getDatePicker().setMaxDate(maxexp.getTimeInMillis());
        dpd.show();
    }

    public void onSubmitAddOrder(View view) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        String orderdate = "";

        if((month) < 10 && dayOfMonth < 10) {
            orderdate = "0" + dayOfMonth + "-0" + (month) + "-" + year;
        }else if((month+1) < 10 && dayOfMonth >= 10){
            orderdate = dayOfMonth + "-0" + (month) + "-" + year;
        }else if((month+1) >= 10 && dayOfMonth < 10){
            orderdate = "0" + dayOfMonth + "-" + (month) + "-" + year;
        }else {
            orderdate = dayOfMonth + "-" + (month) + "-" + year;
        }

        final EditText receivedate = findViewById(R.id.txtreceivedate);

        Date odate = new Date();
        Date rdate = new Date();

        try {
            odate = new SimpleDateFormat("dd-MM-yyyy").parse(orderdate);
            rdate = new SimpleDateFormat("dd-MM-yyyy").parse(receivedate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        receivedate.setError(null);

        if(rdate.before(odate) == true){
            receivedate.setError("กรุณาเลือกวันที่รับสินค้าให้ถูกต้อง");
        }else {
            receivedate.setError(null);
            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);

            final OrderModel orderModel = new OrderModel();

            final String finalOrderdate = orderdate;

            manager.listAllOrder(orderModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    final OrderModel2.Order[] listorder = (OrderModel2.Order[]) response;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OrderActivity.this);
                    String saveusername = preferences.getString("username", "");

                    Log.e("Length", String.valueOf(listorder.length));

                    final OrderModel orderModel = new OrderModel();
                    if (listorder.length < 1) {
                        orderModel.getOrder().setOrderID(String.valueOf(1));
                    } else {
                        orderModel.getOrder().setOrderID(String.valueOf(listorder.length + 1));
                    }
                    orderModel.getOrder().setOrderDate(finalOrderdate);
                    orderModel.getOrder().setReceiveDate(receivedate.getText().toString());
                    orderModel.getOrder().setStatus("รอการยืนยัน");
                    orderModel.getOrder().setCustomer(saveusername);

                    manager.insert_Order(orderModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Log.e("response order", response.toString());
                            Toast.makeText(OrderActivity.this, "สั่งจองสินค้าเรียบร้อย โปรดรอเจ้าหน้าที่ทำการยืนยันคำสั่งจอง", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrderActivity.this, ListOrdersActivity.class);
                            startActivity(intent);
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(OrderActivity.this, "ไม่สามารถทำการสั่งจองสินค้าได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();

                        }
                    });

                    ProductModel productModel = new ProductModel();
                    manager.listProduct(productModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            ProductModel.Product[] list = (ProductModel.Product[]) response;
                            LinearLayout ll = findViewById(R.id.detail);

                            for (int i = 0; i < ll.getChildCount(); i++) {
                                View v = ll.getChildAt(i);
                                TextView txtname = v.findViewById(R.id.txt);

                                TextView txtprice = v.findViewById(R.id.txtprice);
                                Number price = null;
                                try {
                                    price = format.parse(txtprice.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                EditText txtqty = v.findViewById(R.id.txtqty);

                                OrderDetailModel orderdetailModel = new OrderDetailModel();
                                orderdetailModel.getOrderDetail().setOrderDetailID(String.valueOf(i + 1));
                                orderdetailModel.getOrderDetail().setQty(txtqty.getText().toString());
                                orderdetailModel.getOrderDetail().setTotalprice(String.valueOf(price.doubleValue()));
                                for (int j = 0; j < list.length; j++) {
                                    if (txtname.getText().toString().equals(list[j].getProductname())) {
                                        orderdetailModel.getOrderDetail().setProduct(list[j].getProductid());
                                    }
                                }
                                orderdetailModel.getOrderDetail().setOrder(orderModel.getOrder().getOrderID());

                                Log.e("orderdetail", orderdetailModel.getOrderDetail().getOrderDetailID() + " " +
                                        "" + orderdetailModel.getOrderDetail().getQty() + " " +
                                        "" + orderdetailModel.getOrderDetail().getTotalprice() + " " +
                                        "" + orderdetailModel.getOrderDetail().getProduct() + " " +
                                        "" + orderdetailModel.getOrderDetail().getOrder());

                                manager.insert_OrderDetail(orderdetailModel, new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        loadingDialog.dismiss();

                                    }

                                    @Override
                                    public void onError(String err) {
                                        Toast.makeText(OrderActivity.this, "ไม่สามารถทำการสั่งจองสินค้าได้", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();

                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(OrderActivity.this, "ไม่สามารถทำการสั่งจองสินค้าได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();

                        }
                    });

                }

                @Override
                public void onError(String err) {
                    Toast.makeText(OrderActivity.this, "ไม่สามารถทำการสั่งจองสินค้าได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}