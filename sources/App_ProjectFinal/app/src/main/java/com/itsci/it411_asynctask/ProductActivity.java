package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.PlantingModel;
import com.itsci.model.ProductModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    Calendar open = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProductActivity.this);
        final String saveusername = preferences.getString("username", "Guest");
        final TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        final int[] listimgProducts = {R.drawable.product1, R.drawable.product2, R.drawable.product3, R.drawable.product4,
                R.drawable.product5, R.drawable.product6, R.drawable.product7, R.drawable.product8, R.drawable.product9};

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        WSManager manager = WSManager.getWsManager(this);
        ProductModel productModel = new ProductModel();
        manager.listProduct(productModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                ProductModel.Product[] list = (ProductModel.Product[]) response;
                Log.d("products", response.toString());

                for(int i = 0; i < list.length; i++){
                    View v = getLayoutInflater().inflate(R.layout.list_product, null);
                    LinearLayout ll = findViewById(R.id.list_product);

                    ImageView imgProduct = v.findViewById(R.id.imgProduct);
                    imgProduct.setImageResource(listimgProducts[i]);

                    TextView name = v.findViewById(R.id.txtproductname);
                    name.setText(list[i].getProductname());

                    TextView price = v.findViewById(R.id.txtprice);
                    price.setText(formatter.format(Double.parseDouble(list[i].getPrice())));

                    if(saveusername.equals("Guest")){
                        Button btnplus = v.findViewById(R.id.btnPlus);
                        Button btnminus = v.findViewById(R.id.btnMinus);
                        EditText txtqty = v.findViewById(R.id.txtqty);

                        btnplus.setVisibility(View.INVISIBLE);
                        btnminus.setVisibility(View.INVISIBLE);
                        txtqty.setVisibility(View.INVISIBLE);
                    }

                    ll.addView(v);
                }

                loadingDialog.dismiss();

                LinearLayout ll = findViewById(R.id.list_product);

                for(int i = 0; i < ll.getChildCount(); i++){
                    View v = ll.getChildAt(i);
                    final EditText txtqty = v.findViewById(R.id.txtqty);
                    Button btnplus = v.findViewById(R.id.btnPlus);
                    btnplus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int qty = Integer.parseInt(txtqty.getText().toString());
                            qty++;
                            String stringqty = String.valueOf(qty);
                            txtqty.setText(stringqty);
                        }
                    });
                }

                for(int i = 0; i < ll.getChildCount(); i++){
                    View v = ll.getChildAt(i);
                    final EditText txtqty = v.findViewById(R.id.txtqty);
                    Button btnminus = v.findViewById(R.id.btnMinus);
                    btnminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int qty = Integer.parseInt(txtqty.getText().toString());
                            if(qty == 0){
                                qty = 0;
                            }else{
                                qty--;
                            }
                            String stringqty = String.valueOf(qty);
                            txtqty.setText(stringqty);
                        }
                    });
                }
            }

            @Override
            public void onError(String err) {
                loadingDialog.dismiss();
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
                    Date dopen = new Date(list[i].getPlantDate());
                    String[] sopen = sdf.format(dopen).split("-");
                    year = Integer.parseInt(sopen[0]);
                    month = Integer.parseInt(sopen[1]);
                    dayOfMonth = Integer.parseInt(sopen[2]);
                    open.set(year,month,dayOfMonth);

                    Button btnOrder = findViewById(R.id.btnOrder);
                    Log.e("month + nowmonth", month + " " + (now.getTime().getMonth()+1));
                    //default must +1
                    if(month != now.getTime().getMonth()+1){
                        btnOrder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#454545")));
                        btnOrder.setText("ระบบสั่งจองยังไม่เปิดให้ใช้งาน");
                        btnOrder.setEnabled(false);
                    }else{
                        btnOrder.setVisibility(View.VISIBLE);

                        btnOrder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#68B2A0")));

                        //btnOrder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3ACA25")));
                        btnOrder.setText("สั่งจองสินค้า");
                        btnOrder.setEnabled(true);
                    }
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ProductActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void btnOrderClick(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProductActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        String saveuserType = preferences.getString("userType", "1");

        if(saveusername.equals("Guest")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("ไม่สามารถสั่งจองสินค้าได้ กรุณาเข้าสู่ระบบก่อน");
            builder.setCancelable(false);
            builder.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else if(!saveuserType.equals("3")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("สามารถสั่งจองได้เฉพาะลูกค้าเท่านั้น");
            builder.setCancelable(false);
            builder.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            final Intent intent = new Intent(ProductActivity.this, OrderActivity.class);
            final List<String> order = new ArrayList<>();
            final double[] totalprice = {0};

            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            WSManager manager = WSManager.getWsManager(this);
            ProductModel productModel = new ProductModel();
            manager.listProduct(productModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    ProductModel.Product[] list = (ProductModel.Product[]) response;
                    LinearLayout ll = findViewById(R.id.list_product);

                    for (int i = 0; i < list.length; i++) {
                        View v = ll.getChildAt(i);

                        EditText txtqty = v.findViewById(R.id.txtqty);
                        int qty = Integer.parseInt(txtqty.getText().toString());

                        TextView txtname = v.findViewById(R.id.txtproductname);
                        String name = txtname.getText().toString();
                        TextView txtprice = v.findViewById(R.id.txtprice);
                        double dprice = 0;

                        for(int j = 0; j < list.length; j++){
                            if(list[j].getProductname().equals(txtname.getText().toString())){
                                dprice = Double.parseDouble(list[j].getPrice());
                            }
                        }

                        double price = 0;

                        if (qty > 0) {
                            price = dprice * qty;

                            order.add(order.size(), name + "/" + dprice + "/" + qty + "/" + price);
                        }

                    }
                    intent.putStringArrayListExtra("order", (ArrayList<String>) order);
                    Log.e("Order", order.toString());

                    if(order.size() > 0) {
                        startActivity(intent);
                        loadingDialog.dismiss();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                        builder.setTitle("แจ้งเตือน");
                        builder.setMessage("กรุณาเลือกสั่งซื้ออย่างน้อย 1 รายการ");
                        builder.setCancelable(false);
                        builder.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(ProductActivity.this, "ไม่สามารถทำการสั่งจองสินค้าได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}