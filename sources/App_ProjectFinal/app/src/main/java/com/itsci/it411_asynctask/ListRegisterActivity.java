package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.CustomerModel;
import com.itsci.model.CustomerModel2;

import java.io.FileNotFoundException;

public class ListRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_register);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListRegisterActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        final Spinner spinner_Status = findViewById(R.id.spinner_Status);

        String[] list_status = {"รอการยืนยัน","ยืนยันแล้ว","ถูกปฏิเสธ"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListRegisterActivity.this,
                android.R.layout.select_dialog_item, list_status);

        spinner_Status.setAdapter(adapter);

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);

        spinner_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel2 customerModel = new CustomerModel2();
                manager.listCustomer(customerModel, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        CustomerModel2.Customer[] list = (CustomerModel2.Customer[]) response;
                        if(list.length > 0){
                            LinearLayout ll = findViewById(R.id.list_register);
                            ll.removeAllViews();
                            for(int i = 0; i < list.length; i++) {
                                if (list[i].getReqreg().getStatus().equals(spinner_Status.getSelectedItem().toString())) {
                                    View v = getLayoutInflater().inflate(R.layout.list_register, null);

                                    TextView registerid = v.findViewById(R.id.registerid);
                                    registerid.setText(list[i].getReqreg().getReqregid());

                                    TextView persontype = v.findViewById(R.id.persontype);
                                    persontype.setText(list[i].getReqreg().getPersontype());

                                    TextView txtfullname = v.findViewById(R.id.txtfullname);
                                    txtfullname.setText(list[i].getUser().getFullname());

                                    TextView txtcompany = v.findViewById(R.id.txtcompany);
                                    txtcompany.setText(list[i].getCompany());

                                    TextView txtmobileno = v.findViewById(R.id.txtmobileno);
                                    txtmobileno.setText(list[i].getMobileNo());

                                    ImageView imgIDcard = v.findViewById(R.id.imgIDcard);
                                    ImageView imgCertificate = v.findViewById(R.id.imgCertificate);

                                    Bitmap imgIDcardUri = ImageUtil.convert(list[i].getImgIDCard());
                                    Bitmap imgCertificateUri = ImageUtil.convert(list[i].getImgCertificate());

                                    imgIDcard.setImageBitmap(imgIDcardUri);
                                    imgCertificate.setImageBitmap(imgCertificateUri);

                                    TextView TitleimgCertificate = v.findViewById(R.id.TitleimgCertificate);

                                    if(persontype.getText().toString().equals("นิติบุคคล")){
                                        TitleimgCertificate.setVisibility(View.VISIBLE);
                                        imgCertificate.setVisibility(View.VISIBLE);
                                    }

                                    TextView txtstatus = v.findViewById(R.id.txtstatus);
                                    txtstatus.setText(list[i].getReqreg().getStatus());

                                    Button submit = v.findViewById(R.id.btnSubmit);
                                    Button deny = v.findViewById(R.id.btnDeny);
                                    if(list[i].getReqreg().getStatus().equals("ยืนยันแล้ว")){
                                        submit.setVisibility(View.GONE);
                                        deny.setVisibility(View.GONE);
                                        txtstatus.setTextColor(Color.parseColor("#068C13"));
                                    }else if(list[i].getReqreg().getStatus().equals("ถูกปฏิเสธ")){
                                        submit.setVisibility(View.GONE);
                                        deny.setVisibility(View.GONE);
                                        txtstatus.setTextColor(Color.parseColor("#e6031d"));
                                    }else{
                                        submit.setVisibility(View.VISIBLE);
                                        deny.setVisibility(View.VISIBLE);
                                    }

                                    ll.addView(v);
                                }
                            }
                            loadingDialog.dismiss();

                            for(int i = 0; i < ll.getChildCount(); i++) {
                                View v = ll.getChildAt(i);
                                final TextView registerid = v.findViewById(R.id.registerid);
                                Button submit = v.findViewById(R.id.btnSubmit);
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ListRegisterActivity.this);
                                        builder.setMessage("ต้องการยืนยันคำขอสมัครสมาชิกของ รหัส " + registerid.getText().toString() + " ใช่หรือไม่ ?");
                                        builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                manager.Confirm_Registration(registerid.getText().toString(), new WSManager.WSManagerListener() {
                                                    @Override
                                                    public void onComplete(Object response) {
                                                        Intent intent = new Intent(ListRegisterActivity.this, ListRegisterActivity.class);
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onError(String err) {

                                                    }
                                                });
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });

                                Button deny = v.findViewById(R.id.btnDeny);
                                deny.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ListRegisterActivity.this);
                                        builder.setMessage("ต้องการปฏิเสธคำขอสมัครสมาชิกของ รหัส " + registerid.getText().toString() + " ใช่หรือไม่ ?");
                                        builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                manager.Deny_Registration(registerid.getText().toString(), new WSManager.WSManagerListener() {
                                                    @Override
                                                    public void onComplete(Object response) {
                                                        Intent intent = new Intent(ListRegisterActivity.this, ListRegisterActivity.class);
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onError(String err) {

                                                    }
                                                });
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }
                        }else{
                            View v = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                            LinearLayout ll = findViewById(R.id.list_register);
                            ll.addView(v);
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(String err) {
                        loadingDialog.dismiss();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ListRegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ListRegisterActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListRegisterActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListRegisterActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ListRegisterActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}