package com.itsci.it411_asynctask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.FileServiceInfo;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.CustomerModel;
import com.itsci.model.CustomerModel2;
import com.itsci.model.RequestRegisterModel;
import com.itsci.model.UserModel;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.FileStore;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private static final String UPLOAD_URL = "/upload.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WSManager manager = WSManager.getWsManager(this);
        UserModel userModel = new UserModel();
        manager.listUser(userModel, new WSManager.WSManagerListener() {

            @Override
            public void onComplete(Object response) {
                UserModel.User[] list = (UserModel.User[]) response;
                Log.d("data", response.toString());
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void btnUploadIDCard(View view){
        Button btnUpload = findViewById(R.id.btnUploadIDcard);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });
    }

    public void btnUploadCertificate(View view){
        Button btnUpload = findViewById(R.id.btnUploadCertificate);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1002);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imgIDCard = findViewById(R.id.imgIDcard);
        ImageView imgCertificate = findViewById(R.id.imgCertificate);

        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                Log.e("photo", photo.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String base64String = ImageUtil.convert(photo);
            if(requestCode == 1001){
                imgIDCard.setImageBitmap(photo);
                Toast.makeText(RegisterActivity.this, "อัปโหลดรูปถ่ายหน้าบัตรประชาชนเรียบร้อย", Toast.LENGTH_SHORT).show();

                EditText imgIDcardUri = findViewById(R.id.imgIDcardUri);
                imgIDcardUri.setText(base64String);
                imgIDcardUri.setError(null);
            }else{
                imgCertificate.setImageBitmap(photo);
                Toast.makeText(RegisterActivity.this, "อัปโหลดรูปหนังสือรับรองการจดทะเบียนเรียบร้อย", Toast.LENGTH_SHORT).show();

                EditText imgCertificateUri = findViewById(R.id.imgCertificateUri);
                imgCertificateUri.setText(base64String);
                imgCertificateUri.setError(null);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        cursor.close();

        return imagePath;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public void onCheckRadio(View view){
        RadioButton r1 = findViewById(R.id.type_1);
        LinearLayout lc = findViewById(R.id.LayoutCertificate);
        if(r1.isChecked()){
            lc.setVisibility(View.GONE);
        }else{
            lc.setVisibility(View.VISIBLE);
        }
    }

    public void onClickLoginPage(View view){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void doRegister(View view) {
        final EditText fullname = findViewById(R.id.fullname);
        final EditText company = findViewById(R.id.company);
        final EditText address = findViewById(R.id.address);
        final EditText telephone = findViewById(R.id.telephone);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final EditText imgIDcardUri = findViewById(R.id.imgIDcardUri);
        final EditText imgCertificateUri = findViewById(R.id.imgCertificateUri);

        fullname.setError(null);
        company.setError(null);
        address.setError(null);
        telephone.setError(null);
        username.setError(null);
        password.setError(null);
        imgIDcardUri.setError(null);
        imgCertificateUri.setError(null);

        RadioButton r1 = findViewById(R.id.type_1);
        if(r1.isChecked()){
            imgCertificateUri.setError(null);
            imgCertificateUri.setVisibility(View.GONE);
        }else{
            imgCertificateUri.setVisibility(View.VISIBLE);
        }

        String txtfullname = fullname.getText().toString().trim();
        String txtaddress = address.getText().toString().trim();
        String txttelephone = telephone.getText().toString().trim();
        String txtusername = username.getText().toString().trim();
        String txtpassword = password.getText().toString().trim();
        String txtimgIDcardUri = imgIDcardUri.getText().toString().trim();
        String txtimgCertificateUri = imgCertificateUri.getText().toString().trim();

        Matcher matches;
        Pattern pattern;

        if (txtfullname.equals("") || txtaddress.equals("") ||
                txttelephone.equals("") || txtusername.equals("") ||
                txtpassword.equals("") || txtimgIDcardUri.equals("")) {

            String fullnameregex = "^[A-Za-zก-์]{2,100}(\\s){1}[A-Za-zก-์]{1,50}(\\s){0,1}[A-Za-zก-์]{0,50}";

            if (txtfullname.equals("")) {
                fullname.setError("กรุณากรอกชื่อ - นามสกุล");
            } else if (!txtfullname.matches(fullnameregex)) {
                fullname.setError("ชื่อ - นามสกุลต้องประกอบด้วยภาษาไทยหรืออังกฤษ 4 - 200 ตัวอักษร");
            } else {
                fullname.setError(null);
            }

            String addressregex = "[a-zA-Zก-์0-9\\s/.-]{4,255}$";

            if (txtaddress.equals("")) {
                address.setError("กรุณากรอกที่อยู่");
            } else if (!txtaddress.matches(addressregex)) {
                address.setError("รูปแบบที่อยู่ไม่ถูกต้อง");
            } else {
                address.setError(null);
            }

            String telephoneregex = "^(08|06|09)[0-9]{8}$";

            if (txttelephone.equals("")) {
                telephone.setError("กรุณากรอกเบอร์โทรศัพท์");
            } else if (!txttelephone.matches(telephoneregex)) {
                telephone.setError("เบอร์โทรศัพท์ต้องเป็นตัวเลข 10 ตัวเท่านั้น และ ขึ้นต้นด้วย (06,08,09)");
            } else {
                telephone.setError(null);
            }

            String usernameregex = "^[A-Za-z0-9]{4,16}$";

            if (txtusername.equals("")) {
                username.setError("กรุณากรอกชื่อผู้ใช้");
            } else if (!txtusername.matches(usernameregex)) {
                username.setError("ชื่อผู้ใช้ต้องประกอบด้วยภาษาอังกฤษหรือเลข 4 - 16 ตัวอักษร");
            } else {
                username.setError(null);
            }

            String passwordregex = "^[A-Za-z0-9]{4,16}$";

            if (txtpassword.equals("")) {
                password.setError("กรุณากรอกรหัสผ่าน");
            }else if (!txtpassword.matches(passwordregex)){
                password.setError("รหัสผ่านต้องประกอบด้วยภาษาอังกฤษหรือเลข 4 - 16 ตัวอักษร");
            }else{
                password.setError(null);
            }

            if (txtimgIDcardUri.equals("")) {
                imgIDcardUri.setError("กรุณาอัปโหลดรูปถ่ายหน้าบัตรประชาชน");
            }else{
                imgIDcardUri.setError(null);
                imgIDcardUri.setVisibility(View.GONE);
            }

            RadioButton r2 = findViewById(R.id.type_2);
            if (txtimgCertificateUri.equals("") && r2.isChecked()) {
                imgCertificateUri.setError("กรุณาอัปโหลดรูปหนังสือรับรองการจดทะเบียน");
            }else{
                imgCertificateUri.setError(null);
                imgCertificateUri.setVisibility(View.GONE);
            }
        } else {
            fullname.setError(null);
            company.setError(null);
            address.setError(null);
            telephone.setError(null);
            username.setError(null);
            password.setError(null);
            imgIDcardUri.setError(null);
            imgCertificateUri.setError(null);

            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);
            final RequestRegisterModel reqregModel = new RequestRegisterModel();
            final UserModel userModel = new UserModel();
            final CustomerModel2 customerModel2 = new CustomerModel2();

            manager.listRequestRegister(reqregModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    RequestRegisterModel.RequestRegister[] listrequestregister = (RequestRegisterModel.RequestRegister[]) response;

                    reqregModel.getRequestRegister().setReqregid(String.valueOf(listrequestregister.length+1));
                    RadioButton r1 = findViewById(R.id.type_1);
                    RadioButton r2 = findViewById(R.id.type_2);
                    if(r1.isChecked()){
                        reqregModel.getRequestRegister().setPersontype(r1.getText().toString());
                    }else{
                        reqregModel.getRequestRegister().setPersontype(r2.getText().toString());
                    }
                    reqregModel.getRequestRegister().setStatus("รอการยืนยัน");

                    manager.check_duplicate_username(username.getText().toString(), new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            if(response.toString().equals("duplicate")){
                                Toast.makeText(RegisterActivity.this, "ชื่อผู้ใช้ซ้ำ ไม่สามารถใช้ชื่อนี้ได้", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }else{
                                manager.insert_Register(reqregModel, new WSManager.WSManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        userModel.getUser().setFullname(fullname.getText().toString());
                                        userModel.getUser().setUsername(username.getText().toString());
                                        userModel.getUser().setPassword(password.getText().toString());

                                        manager.insert_User(userModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                                manager.listCustomer(customerModel2, new WSManager.WSManagerListener() {
                                                    @Override
                                                    public void onComplete(Object response) {
                                                        CustomerModel2.Customer[] listcustomer = (CustomerModel2.Customer[]) response;

                                                        CustomerModel customerModel = new CustomerModel();
                                                        customerModel.getCustomer().setCustomerID(String.valueOf(listcustomer.length+1));
                                                        customerModel.getCustomer().setCompany(company.getText().toString());
                                                        customerModel.getCustomer().setAddress(address.getText().toString());
                                                        customerModel.getCustomer().setMobileNo(telephone.getText().toString());
                                                        if(imgIDcardUri.getText().toString().trim().equals("")){
                                                            customerModel.getCustomer().setImgIDCard("-");
                                                        }else{
                                                            customerModel.getCustomer().setImgIDCard(imgIDcardUri.getText().toString());
                                                        }
                                                        if(imgCertificateUri.getText().toString().trim().equals("")){
                                                            customerModel.getCustomer().setImgCertificate("-");
                                                        }else{
                                                            customerModel.getCustomer().setImgCertificate(imgCertificateUri.getText().toString());
                                                        }
                                                        customerModel.getCustomer().setReqreg(reqregModel.getRequestRegister().getReqregid());
                                                        customerModel.getCustomer().setUser(userModel.getUser().getUsername());

                                                        manager.insert_Customer(customerModel, new WSManager.WSManagerListener() {
                                                            @Override
                                                            public void onComplete(Object response) {
                                                                Toast.makeText(RegisterActivity.this, "ิยินดีต้อนรับสมาชิกใหม่ ลงทะเบียนสำเร็จเรียบร้อย", Toast.LENGTH_SHORT).show();

                                                                EditText username = findViewById(R.id.username);
                                                                EditText password = findViewById(R.id.password);

                                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                intent.putExtra("username", username.getText().toString());
                                                                intent.putExtra("password", password.getText().toString());
                                                                startActivity(intent);
                                                                loadingDialog.dismiss();
                                                            }

                                                            @Override
                                                            public void onError(String err) {
                                                                Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                                                                loadingDialog.dismiss();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(String err) {
                                                        Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                                                        loadingDialog.dismiss();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String err) {
                                        Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(RegisterActivity.this, "ไม่สามารถทำการสมัครสมาชิกได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });

        }
    }
}
