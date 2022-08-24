package com.itsci.it411_asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itsci.manager.WSManager;
import com.itsci.model.CustomerModel2;
import com.itsci.model.UserModel;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        Intent intent = getIntent();
        String username =  intent.getStringExtra("username");
        String password =  intent.getStringExtra("password");

        EditText txtusername = findViewById(R.id.username);
        txtusername.setText(username);

        EditText txtpassword = findViewById(R.id.password);
        txtpassword.setText(password);

        editor.clear();
        editor.apply();
    }

    public void onClickRegisterPage(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickGuestVisit(View view){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void do_login (View view){
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        username.setError(null);
        password.setError(null);

        if(username.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")){
            if(username.getText().toString().trim().equals("")){
                username.setError("กรุณากรอกชื่อผู้ใช้");
            }else{
                username.setError(null);
            }
            if(password.getText().toString().trim().equals("")){
                password.setError("กรุณากรอกรหัสผ่าน");
            }else{
                password.setError(null);
            }
        } else {
            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);
            final UserModel userModel = new UserModel();
            userModel.getUser().setUsername(username.getText().toString());
            userModel.getUser().setPassword(password.getText().toString());

            manager.doLogin(userModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    if(response.toString().equals("1")){
                        manager.getUser(userModel, new WSManager.WSManagerListener() {
                            @Override
                            public void onComplete(Object response) {
                                final UserModel.User user = (UserModel.User) response;
                                Log.e("user", user.getUsername());
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                final SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("username", user.getUsername());
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                editor.putString("login", json);

                                manager.getUserType(user.getUsername(), new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        String type = response.toString();
                                        Log.e("type", type);
                                        editor.putString("userType", type);
                                        editor.apply();

                                        if(type.equals("3")){
                                            manager.getCustomer(user.getUsername(), new WSManager.WSManagerListener() {
                                                @Override
                                                public void onComplete(Object response) {
                                                    CustomerModel2.Customer customer = (CustomerModel2.Customer) response;
                                                    if(customer.getReqreg().getStatus().equals("ยืนยันแล้ว")){
                                                        Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        loadingDialog.dismiss();
                                                    }else{
                                                        Toast.makeText(LoginActivity.this, "บัญชีของคุณยังไม่ได้รับการยืนยัน", Toast.LENGTH_SHORT).show();
                                                        loadingDialog.dismiss();
                                                    }
                                                }

                                                @Override
                                                public void onError(String err) {
                                                    Toast.makeText(LoginActivity.this, "บัญชีของคุณยังไม่ได้รับการยืนยัน", Toast.LENGTH_SHORT).show();
                                                    loadingDialog.dismiss();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            loadingDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(String err) {
                                        Toast.makeText(LoginActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onError(String err) {
                                Toast.makeText(LoginActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        });

                    }else{
                        Toast.makeText(LoginActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(LoginActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }

    }
}