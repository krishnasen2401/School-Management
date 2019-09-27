package com.lingamworks.school;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lingamworks.school.Utils.FileUtils;
import com.lingamworks.school.apicalls.RetrofitClient;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText name1,dob1,pname11,pname21,phone11,phone21,email1,fees1;
    RadioButton playgroup,mont1,mont2,mont3;
    RadioButton rbGmale,rbGFemale;
    String classes1,gender1;
    ImageView imageView;
    Uri fileuri;
    Bitmap bitmap;
    File file;
    private static final int gallerycode=100;
    private static final int permissioncode=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1=findViewById(R.id.etName);
        dob1=findViewById(R.id.etDOB);
        pname11=findViewById(R.id.etPname1);
        pname21=findViewById(R.id.etPname2);
        phone11=findViewById(R.id.etPhone1);
        phone21=findViewById(R.id.etPhone2);
        email1=findViewById(R.id.etEmail);
        fees1=findViewById(R.id.etFees);
        playgroup=findViewById(R.id.rbPlaygroup);
        mont1=findViewById(R.id.rbMont1);
        mont2=findViewById(R.id.rbMont2);
        mont3=findViewById(R.id.rbMont3);
        rbGFemale=findViewById(R.id.rbFemale);
        rbGmale=findViewById(R.id.rbMale);
        classes1=null;
        fileuri=Uri.EMPTY;
        file=null;
        imageView=findViewById(R.id.imageView2);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            //permission is not granted ,request it
                String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,permissioncode);
            }else {
            //permission already granted

            }}else{
            //system is less than marshmello
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==gallerycode){
            fileuri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void PickDate(View view){
        dob1=findViewById(R.id.etDOB);
        final Calendar c = Calendar.getInstance();int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob1.setText(String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
public void imagePickers(View view){
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,"Select A image"),gallerycode );
    }

    public void sbSubmit(View v){
        if(playgroup.isChecked()){
            classes1="Playgroup";
        }

        if(mont1.isChecked()){
            classes1="Mont 1";
        }

        if(mont2.isChecked()){
            classes1="Mont 2";
        }

        if(mont3.isChecked()){
            classes1="Mont 3";
        }
        if(rbGmale.isChecked()){
            gender1="M";
        }
        if(rbGFemale.isChecked()){
            gender1="F";
        }
        RequestBody name = RequestBody.create(MediaType.parse("multipar/form-data"), String.valueOf(name1.getText()));
        RequestBody dob=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(dob1.getText()));
        RequestBody gender = RequestBody.create(MediaType.parse("multipar/form-data"),gender1 );
        RequestBody classes=RequestBody.create(MediaType.parse("multipar/form-data"),classes1);
        RequestBody parent1=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(pname11.getText()));
        RequestBody phone1=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(phone11.getText()));
        RequestBody parent2=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(pname21.getText()));
        RequestBody phone2=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(phone21.getText()));
        RequestBody email=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(email1.getText()));
        RequestBody fees=RequestBody.create(MediaType.parse("multipar/form-data"),String.valueOf(phone21.getText()));
        if (fileuri!=Uri.EMPTY){
            file = new File(FileUtils.getPath(fileuri, this));
            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] imageInByte = bytes.toByteArray();
            long lengthbmp = imageInByte.length;
            Log.d("size", String.valueOf(lengthbmp));//<<500000
        }else{
                bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //insert bitmap into temp imagepath
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
            Uri uri= Uri.parse(path);
            file = new File(FileUtils.getPath(uri, this));
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-dataimage/*"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", file.getName(), requestFile);
        Call<ResponseBody> call= RetrofitClient.getInstance().getApi().addstudent(name,gender,dob,classes,parent1,phone1,parent2,phone2,email,fees,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                    Log.d("success",s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
