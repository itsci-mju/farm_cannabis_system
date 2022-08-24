package com.itsci.it411_asynctask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.OrderModel;
import com.itsci.model.PaymentModel;
import com.itsci.model.PaymentModel2;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
        final String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        EditText edt = findViewById(R.id.txtpaydate);
        if((month+1) < 10 && day < 10) {
            edt.setText("0" + day + "-0" + (month+1) + "-" + year);
        }else if((month+1) < 10 && day >= 10){
            edt.setText(day + "-0" + (month+1) + "-" + year);
        }else if((month+1) >= 10 && day < 10){
            edt.setText("0" + day + "-" + (month+1) + "-" + year);
        }else {
            edt.setText(day + "-" + (month+1) + "-" + year);
        }

        Intent intent = getIntent();
        String orderid =  intent.getStringExtra("orderid");
        String orderdate =  intent.getStringExtra("orderdate");
        String receivedate =  intent.getStringExtra("receivedate");
        String status =  intent.getStringExtra("status");

        LinearLayout ll = findViewById(R.id.list_order);
        final View v = getLayoutInflater().inflate(R.layout.list_order, null);

        TextView id = v.findViewById(R.id.showorderid);
        id.setText(orderid);

        TextView txtorderdate = v.findViewById(R.id.orderdate);
        txtorderdate.setText(orderdate);

        TextView txtreceivedate = v.findViewById(R.id.receivedate);
        txtreceivedate.setText(receivedate);

        TextView txtstatus = v.findViewById(R.id.status);
        txtstatus.setText(status);

        Button btnPayment = v.findViewById(R.id.btnPayment);
        btnPayment.setVisibility(View.GONE);

        if(txtstatus.getText().toString().equals("ยืนยันแล้ว")){
            txtstatus.setTextColor(Color.parseColor("#ff7b00"));
        }

        ll.addView(v);

        WSManager manager = WSManager.getWsManager(this);
        manager.listOrderDetailByOrderID(orderid, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                OrderDetailModel2.OrderDetail[] list = (OrderDetailModel2.OrderDetail[]) response;
                double totalprice = 0;
                for(int i = 0; i < list.length; i++) {
                    final View v2 = getLayoutInflater().inflate(R.layout.list_in_list_order, null);
                    LinearLayout ll2 = v.findViewById(R.id.list_in_list_order);

                    TextView txt = v2.findViewById(R.id.txt);
                    txt.setText(list[i].getProduct().getProductname() + " x " + list[i].getQty() + " กิโลกรัม" +
                            " ฿" + formatter.format(Double.parseDouble(list[i].getTotalprice()))  );

                    totalprice += Double.parseDouble(list[i].getTotalprice());

                    TextView txttotalprice = findViewById(R.id.totalprice);
                    txttotalprice.setText("฿" + formatter.format(totalprice));

                    ll2.addView(v2);
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void btnUploadPayment(View view){
        Button btnUpload = findViewById(R.id.btnUploadPayment);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imgPayment = findViewById(R.id.imgPayment);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String base64String = ImageUtil.convert(photo);

            if(requestCode == 1001){
                imgPayment.setImageBitmap(photo);

                EditText imgPaymentUri = findViewById(R.id.imgPaymentUri);
                imgPaymentUri.setText(base64String);
                imgPaymentUri.setError(null);
                Toast.makeText(PaymentActivity.this, "อัปโหลดรูปหลักฐานการชำระเงินเรียบร้อย", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void onClickHomePage(View view){
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(PaymentActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(PaymentActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(PaymentActivity.this, ListOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickPayDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtpaydate);
                if((month+1) < 10 && dayOfMonth < 10) {
                    edt.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                }else if((month+1) < 10 && dayOfMonth >= 10){
                    edt.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                }else if((month+1) >= 10 && dayOfMonth < 10){
                    edt.setText(year + "-" + (month+1) + "-0" + dayOfMonth);
                }else {
                    edt.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                }
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpd.show();
    }

    public void onSubmitPayment(View view){
        EditText imgPaymentUri = findViewById(R.id.imgPaymentUri);
        EditText txtpaydate = findViewById(R.id.txtpaydate);

        imgPaymentUri.setError(null);
        txtpaydate.setError(null);

        String imgPayment = imgPaymentUri.getText().toString().trim();
        String paydate = txtpaydate.getText().toString().trim();

        if(imgPayment.equals("") || paydate.equals("")){
            if(imgPayment.equals("")){
                imgPaymentUri.setError("กรุณาเลือกรูปภาพหลักฐานการชำระเงิน");
            }else{
                imgPaymentUri.setError(null);
            }

            if(paydate.equals("")){
                txtpaydate.setError("กรุณาเลือกวันที่ชำระเงิน");
            }else{
                txtpaydate.setError(null);
            }
        }else{
            imgPaymentUri.setError(null);
            txtpaydate.setError(null);

            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);
            PaymentModel2 paymentModel = new PaymentModel2();
            manager.listPayment(paymentModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    PaymentModel2.Payment[] list = (PaymentModel2.Payment[]) response;

                    TextView txttotalprice = findViewById(R.id.totalprice);
                    String[] totalprice = txttotalprice.getText().toString().split("฿");
                    Number price = null;
                    try {
                        price = format.parse(totalprice[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    EditText txtpaydate = findViewById(R.id.txtpaydate);
                    final EditText imgPaymentUri = findViewById(R.id.imgPaymentUri);
                    Intent intent = getIntent();
                    final String orderid =  intent.getStringExtra("orderid");

                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.getPayment().setPaymentID(String.valueOf(list.length+1));
                    paymentModel.getPayment().setAmount(String.valueOf(price.doubleValue()));
                    paymentModel.getPayment().setPaydate(txtpaydate.getText().toString());
                    paymentModel.getPayment().setImgPayment(imgPaymentUri.getText().toString());
                    paymentModel.getPayment().setOrder(orderid);

                    manager.insert_Payment(paymentModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            //Uri ImgPaymentUri = Uri.parse(imgPaymentUri.getText().toString());
                            //File ImgPaymentFile = new File(getRealPathFromURI(ImgPaymentUri));

                            final OrderModel orderModel = new OrderModel();

                            orderModel.getOrder().setOrderID(orderid);
                            orderModel.getOrder().setStatus("รอการตรวจสอบชำระเงิน");

                            manager.update_OrderStatus(orderModel, new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    Toast.makeText(PaymentActivity.this, "ชำระเงินสำเร็จ กรุณารอการตรวจสอบการชำระเงิน", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentActivity.this, ListOrdersActivity.class);
                                    startActivity(intent);
                                    loadingDialog.dismiss();

                                }

                                @Override
                                public void onError(String err) {
                                    Toast.makeText(PaymentActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถทำการชำระเงินได้", Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();

                                }
                            });
                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(PaymentActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถทำการชำระเงินได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();

                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(PaymentActivity.this, "ข้อมูลไม่ถูกต้อง ไม่สามารถทำการชำระเงินได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}