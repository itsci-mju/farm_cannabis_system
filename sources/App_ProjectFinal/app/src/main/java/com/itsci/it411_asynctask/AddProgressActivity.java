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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;
import com.itsci.model.PlantingProgressModel;
import com.itsci.model.PlantingProgressModel2;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class AddProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddProgressActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        String plantid = intent.getStringExtra("plantid");

        TextView txtid = findViewById(R.id.txtid);
        txtid.setText(plantid);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        EditText edt = findViewById(R.id.txtprogressdate);
        if((month+1) < 10 && dayOfMonth < 10) {
            edt.setText("0" + dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) < 10 && dayOfMonth >= 10){
            edt.setText(dayOfMonth + "-0" + (month+1) + "-" + year);
        }else if((month+1) >= 10 && dayOfMonth < 10){
            edt.setText("0" + dayOfMonth + "-" + (month+1) + "-" + year);
        }else {
            edt.setText(dayOfMonth + "-" + (month+1) + "-" + year);
        }
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(AddProgressActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(AddProgressActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddProgressActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddProgressActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AddProgressActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddProgressActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(AddProgressActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(AddProgressActivity.this, ListPlantingActivity.class);
        TextView txtid = findViewById(R.id.txtid);
        intent.putExtra("plantid", txtid.getText().toString());
        startActivity(intent);
    }

    public void onClickProgressDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddProgressActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText edt = findViewById(R.id.txtprogressdate);
                if((month+1) < 10 && dayOfMonth < 10) {
                    edt.setText("0" + dayOfMonth + "-0" + (month+1) + "-" + year);
                }else if((month+1) < 10 && dayOfMonth >= 10){
                    edt.setText(dayOfMonth + "-0" + (month+1) + "-" + year);
                }else if((month+1) >= 10 && dayOfMonth < 10){
                    edt.setText("0" + dayOfMonth + "-" + (month+1) + "-" + year);
                }else {
                    edt.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                }
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpd.show();
    }

    public void btnUploadProgress(View view){
        Button btnUpload = findViewById(R.id.btnUploadProgress);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imgProgress = findViewById(R.id.imgProgress);
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
                imgProgress.setImageBitmap(photo);

                EditText imgProgressUri = findViewById(R.id.imgProgressUri);
                imgProgressUri.setText(base64String);

                Toast.makeText(AddProgressActivity.this, "อัปโหลดรูปภาพประกอบเรียบร้อย", Toast.LENGTH_SHORT).show();
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

    public void onSubmitAddProgress(View view) {
        EditText txtprogressdate = findViewById(R.id.txtprogressdate);
        EditText imgProgressUri = findViewById(R.id.imgProgressUri);
        EditText txtnote = findViewById(R.id.txtnote);

        txtprogressdate.setError(null);
        imgProgressUri.setError(null);
        txtnote.setError(null);

        String progressdate = txtprogressdate.getText().toString().trim();
        String imgProgress = imgProgressUri.getText().toString().trim();
        String note = txtnote.getText().toString().trim();

        if(progressdate.equals("") || imgProgress.equals("")){
            if(progressdate.equals("")){
                txtprogressdate.setError("กรุณากรอกวันที่บันทึกความคืบหน้า");
            }else{
                txtprogressdate.setError(null);
            }

            if(imgProgress.equals("")){
                imgProgressUri.setError("กรุณาเลือกรูปภาพประกอบความคืบหน้า");
            }else{
                imgProgressUri.setError(null);
            }

            if(note.length() > 255){
                txtnote.setError("หมายเหตุไม่สามารถมากกว่า 255 ตัวอักษรได้");
            }else{
                txtnote.setError(null);
            }
        }else{
            txtprogressdate.setError(null);
            imgProgressUri.setError(null);
            txtnote.setError(null);

            final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

            final WSManager manager = WSManager.getWsManager(this);

            TextView txtplantid = findViewById(R.id.txtid);

            manager.listPlantingProgress(txtplantid.getText().toString(), new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    PlantingProgressModel plantingProgressModel = new PlantingProgressModel();

                    TextView txtprogressdate = findViewById(R.id.txtprogressdate);
                    EditText imgProgressUri = findViewById(R.id.imgProgressUri);
                    TextView txtnote = findViewById(R.id.txtnote);
                    TextView txtplantid = findViewById(R.id.txtid);

                    PlantingProgressModel2.PlantingProgress[] list = (PlantingProgressModel2.PlantingProgress[]) response;

                    if (list.length > 0) {
                        int ppid = 1;
                        for (int i = 0; i < list.length; i++) {
                            Log.e("position", i + " " + list[i].getProgressID());

                            if (!String.valueOf(ppid).equals(String.valueOf(list[i].getProgressID()))) {
                                plantingProgressModel.getPlantingProgress().setProgressID(String.valueOf(ppid));
                            } else {
                                plantingProgressModel.getPlantingProgress().setProgressID(String.valueOf(list.length + 1));
                            }
                            ppid++;
                        }
                    } else {
                        plantingProgressModel.getPlantingProgress().setProgressID("1");
                    }

                    plantingProgressModel.getPlantingProgress().setProgressDate(txtprogressdate.getText().toString());
                    plantingProgressModel.getPlantingProgress().setImgProgress(imgProgressUri.getText().toString());

                    if (txtnote.getText().toString().equals("")) {
                        plantingProgressModel.getPlantingProgress().setNote("-");
                    } else {
                        plantingProgressModel.getPlantingProgress().setNote(txtnote.getText().toString());
                    }

                    plantingProgressModel.getPlantingProgress().setPlanting(txtplantid.getText().toString());

                    manager.insert_PlantingProgress(plantingProgressModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Toast.makeText(AddProgressActivity.this, "บันทึกข้อมูลความคืบหน้าเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddProgressActivity.this, ListProgressActivity.class);
                            TextView txtid = findViewById(R.id.txtid);
                            intent.putExtra("plantid", txtid.getText().toString());
                            startActivity(intent);
                            loadingDialog.dismiss();

                        }

                        @Override
                        public void onError(String err) {
                            Toast.makeText(AddProgressActivity.this, "ไม่สามารถทำการบันทึกข้อมูลความคืบหน้าได้", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(AddProgressActivity.this, "ไม่สามารถทำการบันทึกข้อมูลความคืบหน้าได้", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}