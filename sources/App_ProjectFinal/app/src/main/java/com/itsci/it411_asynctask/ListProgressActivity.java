package com.itsci.it411_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itsci.ImageUtil;
import com.itsci.manager.WSManager;
import com.itsci.model.PlantingProgressModel;
import com.itsci.model.PlantingProgressModel2;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ListProgressActivity extends AppCompatActivity {
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_progress);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListProgressActivity.this);
        String saveusername = preferences.getString("username", "Guest");
        TextView txtusername = findViewById(R.id.txtusername);
        txtusername.setText(saveusername);

        Intent intent = getIntent();
        final String plantid = intent.getStringExtra("plantid");

        TextView txtplantid = findViewById(R.id.txtid);
        txtplantid.setText(plantid);

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);

        final WSManager manager = WSManager.getWsManager(this);
        manager.listPlantingProgress(plantid, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                PlantingProgressModel2.PlantingProgress[] list = (PlantingProgressModel2.PlantingProgress[]) response;
                if(list.length > 0){
                    for(int i = 0; i < list.length; i++){
                        View v = getLayoutInflater().inflate(R.layout.list_progress, null);
                        LinearLayout ll = findViewById(R.id.list_progress);

                        TextView ppid = v.findViewById(R.id.ppid);
                        ppid.setText(list[i].getProgressID());

                        TextView tppdate = v.findViewById(R.id.ppdate);
                        Date ppdate = new Date(list[i].getProgressDate());
                        String sppdate = sdf.format(ppdate);
                        tppdate.setText(sppdate);

                        ImageView imgProgress = v.findViewById(R.id.imgProgress);
                        EditText imgProgressUri = v.findViewById(R.id.imgProgressUri);

                        Bitmap imgProgressBitmap = ImageUtil.convert(list[i].getImgProgress());
                        imgProgress.setImageBitmap(imgProgressBitmap);
                        imgProgressUri.setText(list[i].getImgProgress());



                        TextView ppnote = v.findViewById(R.id.persontype);
                        ppnote.setText(list[i].getNote());

                        ll.addView(v);
                    }

                    loadingDialog.dismiss();

                    LinearLayout ll = findViewById(R.id.list_progress);

                    for(int i = 0; i < ll.getChildCount(); i++) {
                        View v = ll.getChildAt(i);
                        CardView btnEditProgress = v.findViewById(R.id.btnEditProgress);
                        CardView btnDeleteProgress = v.findViewById(R.id.btnDeleteProgress);

                        final TextView ppid = v.findViewById(R.id.ppid);
                        final TextView tppdate = v.findViewById(R.id.ppdate);
                        final EditText imgProgressUri = v.findViewById(R.id.imgProgressUri);
                        final TextView ppnote = v.findViewById(R.id.persontype);
                        final TextView plantid = findViewById(R.id.txtid);

                        btnEditProgress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListProgressActivity.this, EditProgressActivity.class);

                                intent.putExtra("ppid", ppid.getText().toString());
                                intent.putExtra("ppdate", tppdate.getText().toString());
                                intent.putExtra("imgProgressUri", imgProgressUri.getText().toString());
                                intent.putExtra("ppnote", ppnote.getText().toString());
                                intent.putExtra("plantid", plantid.getText().toString());

                                startActivity(intent);
                            }
                        });

                        btnDeleteProgress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ListProgressActivity.this);
                                builder.setMessage("คุณต้องการลบข้อมูลความคืบหน้า รหัส " + ppid.getText() + " ใช่หรือไม่ ?");
                                builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PlantingProgressModel progressModel = new PlantingProgressModel();

                                        progressModel.getPlantingProgress().setProgressID(ppid.getText().toString());
                                        progressModel.getPlantingProgress().setProgressDate(tppdate.getText().toString());
                                        progressModel.getPlantingProgress().setImgProgress(imgProgressUri.getText().toString());
                                        progressModel.getPlantingProgress().setNote(ppnote.getText().toString());
                                        progressModel.getPlantingProgress().setPlanting(plantid.getText().toString());

                                        manager.delete_PlantingProgress(progressModel, new WSManager.WSManagerListener() {
                                            @Override
                                            public void onComplete(Object response) {
                                                Toast.makeText(ListProgressActivity.this, "ลบข้อมูลข้อมูลความคืบหน้าเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ListProgressActivity.this, ListProgressActivity.class);
                                                intent.putExtra("plantid", plantid.getText().toString());
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onError(String err) {
                                                Toast.makeText(ListProgressActivity.this, "ไม่สามารถทำการลบข้อมูลข้อมูลความคืบหน้าได้", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();
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
                    LinearLayout ll = findViewById(R.id.list_progress);
                    ll.addView(v);
                    loadingDialog.dismiss();

                    ImageView imgAddData = v.findViewById(R.id.imageAddData);

                    imgAddData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListProgressActivity.this, AddProgressActivity.class);
                            intent.putExtra("plantid", plantid);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(String err) {
                loadingDialog.dismiss();
            }
        });
    }

    public void onClickHomePage(View view){
        Intent intent = new Intent(ListProgressActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickProductPage(View view){
        Intent intent = new Intent(ListProgressActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view){
        TextView txtusername = findViewById(R.id.txtusername);
        if(txtusername.getText().toString().equals("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListProgressActivity.this);
            builder.setMessage("ต้องการเข้าสู่ระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListProgressActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListProgressActivity.this);
            builder.setMessage("ต้องการออกจากระบบใช่หรือไม่ ?");
            builder.setNeutralButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListProgressActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onClickMenuPage(View view){
        Intent intent = new Intent(ListProgressActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickBackPage(View view){
        Intent intent = new Intent(ListProgressActivity.this, ListPlantingActivity.class);
        startActivity(intent);
    }
}