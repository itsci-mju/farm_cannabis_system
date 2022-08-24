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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.manager.WSManager;
import com.itsci.model.OrderModel;
import com.itsci.model.PlantingModel;

public class EditOrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_status);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditOrderStatusActivity.this);
        final String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        String orderid =  intent.getStringExtra("orderid");
        String status =  intent.getStringExtra("status");

        TextView id = findViewById(R.id.id);
        id.setText(orderid);

        Spinner spinner_status = findViewById(R.id.spinner_status);
        String[] list_status = {"รอการยืนยัน","ยืนยันแล้ว","ปฏิเสธ","รอการตรวจสอบชำระเงิน","ชำระเงินแล้ว"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditOrderStatusActivity.this,
                android.R.layout.select_dialog_item, list_status);

        spinner_status.setAdapter(adapter);

        id.setText(orderid);
        spinner_status.setSelection(adapter.getPosition(status));

    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(EditOrderStatusActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(EditOrderStatusActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditOrderStatusActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditOrderStatusActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(EditOrderStatusActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(EditOrderStatusActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(EditOrderStatusActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(EditOrderStatusActivity.this, ListAllOrdersActivity.class);
        startActivity(intent);
    }

    public void onSubmitEditOrderStatus(View view){
        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        TextView id = findViewById(R.id.id);
        Spinner spinner_status = findViewById(R.id.spinner_status);

        final WSManager manager = WSManager.getWsManager(this);
        final OrderModel orderModel = new OrderModel();

        orderModel.getOrder().setOrderID(id.getText().toString());
        orderModel.getOrder().setStatus(spinner_status.getSelectedItem().toString());

        manager.update_OrderStatus(orderModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                Toast.makeText(EditOrderStatusActivity.this, "แก้ไขสถานะคำสั่งจอง รหัส " + orderModel.getOrder().getOrderID() + " เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrderStatusActivity.this, ListAllOrdersActivity.class);
                startActivity(intent);
                loadingDialog.dismiss();
            }

            @Override
            public void onError(String err) {
                Toast.makeText(EditOrderStatusActivity.this, "เกิดข้อผิดพลาด ไม่สามารถแก้ไขสถานะคำสั่งจองได้", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

            }
        });
    }
}