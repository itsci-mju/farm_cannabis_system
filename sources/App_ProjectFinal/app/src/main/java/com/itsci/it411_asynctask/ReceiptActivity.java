package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsci.manager.WSManager;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.PaymentModel2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReceiptActivity.this);
        final String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        final String orderid =  intent.getStringExtra("orderid");
        String orderdate =  intent.getStringExtra("orderdate");
        String receivedate =  intent.getStringExtra("receivedate");
        String status =  intent.getStringExtra("status");

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        manager.listOrderDetailByOrderID(orderid, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                OrderDetailModel2.OrderDetail[] list = (OrderDetailModel2.OrderDetail[]) response;

                double totalprice = 0;
                for(int i = 0; i < list.length; i++) {
                    LinearLayout ll = findViewById(R.id.list_in_receipt);
                    final View v = getLayoutInflater().inflate(R.layout.list_in_receipt, null);

                    TextView fullname = findViewById(R.id.fullname);
                    fullname.setText(list[i].getOrder().getCustomer().getUser().getFullname());

                    manager.getPaymentByOrderID(orderid, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            PaymentModel2.Payment payment = (PaymentModel2.Payment) response;

                            TextView no = findViewById(R.id.no);
                            no.setText(payment.getPaymentID());

                            TextView paydate = findViewById(R.id.paydate);
                            Date dpaydate = new Date(payment.getPaydate());
                            String spaydate = sdf.format(dpaydate);
                            paydate.setText(spaydate);
                        }

                        @Override
                        public void onError(String err) {

                        }
                    });

                    TextView address = findViewById(R.id.address);
                    address.setText(list[i].getOrder().getCustomer().getAddress());

                    TextView company = findViewById(R.id.company);
                    company.setText(list[i].getOrder().getCustomer().getCompany());

                    TextView id = v.findViewById(R.id.id);
                    id.setText(String.valueOf(i+1));

                    TextView name = v.findViewById(R.id.name);
                    name.setText(list[i].getProduct().getProductname());

                    TextView price = v.findViewById(R.id.price);
                    double priceone = Double.parseDouble(list[i].getTotalprice()) /  Double.parseDouble(list[i].getQty());
                    price.setText(formatter.format(priceone));

                    TextView qty = v.findViewById(R.id.qty);
                    qty.setText(list[i].getQty());

                    TextView Totalprice = v.findViewById(R.id.totalprice);
                    Totalprice.setText(formatter.format(Double.parseDouble(list[i].getTotalprice())));

                    totalprice += Double.parseDouble(list[i].getTotalprice());
                    TextView txttotalprice = findViewById(R.id.totalAllprice);
                    txttotalprice.setText(formatter.format(totalprice));

                    ll.addView(v);
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
        Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ReceiptActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiptActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReceiptActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiptActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReceiptActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ReceiptActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(ReceiptActivity.this, ListOrdersActivity.class);
        startActivity(intent);
    }
}