package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsci.manager.WSManager;
import com.itsci.model.CustomerModel;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
        String saveusername = preferences.getString("username", "Guest");

        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        String saveuserType = preferences.getString("userType", "1");

        LinearLayout MenuGuest = findViewById(R.id.MenuGuest);
        LinearLayout MenuCustomer = findViewById(R.id.MenuCustomer);
        LinearLayout MenuOfficer1 = findViewById(R.id.MenuOfficer1);
        LinearLayout MenuOfficer2 = findViewById(R.id.MenuOfficer2);
        LinearLayout MenuExecutive1 = findViewById(R.id.MenuExecutive1);
        LinearLayout MenuExecutive2 = findViewById(R.id.MenuExecutive2);
        LinearLayout MenuAdmin = findViewById(R.id.MenuAdmin);

        WSManager manager = WSManager.getWsManager(this);

        if(txtusername.getText().toString().equals("Guest")) {
            MenuGuest.setVisibility(View.VISIBLE);
        }else if(saveuserType.equals("3")) {
            MenuCustomer.setVisibility(View.VISIBLE);
        }else if(saveuserType.equals("2")) {
            MenuOfficer1.setVisibility(View.VISIBLE);
            MenuOfficer2.setVisibility(View.VISIBLE);
        }else if(saveuserType.equals("1")) {
            MenuExecutive1.setVisibility(View.VISIBLE);
            MenuExecutive2.setVisibility(View.VISIBLE);
        }else if(saveuserType.equals("0")) {
            MenuAdmin.setVisibility(View.VISIBLE);
        }
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(MenuActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickMenuLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
        builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //Menu Guest
    public void onClickMenuRegister(View view){
        Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickMenuLogin(View view){
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //Menu Customer
    public void onClickMenuListOrders(View view){
        Intent intent = new Intent(MenuActivity.this, ListOrdersActivity.class);
        startActivity(intent);
    }

    //Menu Officer
    public void onClickMenuAddPlanting(View view){
        Intent intent = new Intent(MenuActivity.this, AddPlantingActivity.class);
        startActivity(intent);
    }

    public void onClickMenuListPlanting(View view){
        Intent intent = new Intent(MenuActivity.this, ListPlantingActivity.class);
        startActivity(intent);
    }

    public void onClickMenuListAllOrders(View view){
        Intent intent = new Intent(MenuActivity.this, ListAllOrdersActivity.class);
        startActivity(intent);
    }

    //Menu Executive
    public void onClickMenuReportPlanting(View view){
        Intent intent = new Intent(MenuActivity.this, ReportPlantingActivity.class);
        startActivity(intent);
    }

    public void onClickMenuReportOrders(View view){
        Intent intent = new Intent(MenuActivity.this, ReportOrdersActivity.class);
        startActivity(intent);
    }

    //Menu Administrator
    public void onClickMenuListRegister(View view){
        Intent intent = new Intent(MenuActivity.this, ListRegisterActivity.class);
        startActivity(intent);
    }
}