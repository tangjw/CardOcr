package com.zsm.card;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.intsig.openapilib.OpenApi;
import com.intsig.openapilib.OpenApiParams;

public class MainActivity extends Activity {
    
    //构造一个OpenApi对象
    OpenApi openApi = OpenApi.instance("yM1ya7NRb01AA3StC2SA69UR");
    
    //构造一个OpenApiParams对象
    OpenApiParams params = new OpenApiParams() {
        {
            this.setRecognizeLanguage("");
            this.setReturnCropImage(true);
            this.setSaveCard(true);
        }
    };
    private static final int REQUEST_CODE_RECOGNIZE = 0x1001;
    private Button start;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        
        start = (Button) findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRecognizeCapture();
            }
        });
    }
    
    
    public void testRecognizeCapture() {
        
        //判断当前手机上是否装有CamCard
        if (openApi.isCamCardInstalled(this)) {
            //判断当前手机上的CamCard是否支持OpenApi
            if (openApi.isExistAppSupportOpenApi(this)) {
                openApi.recognizeCardByCapture(this, REQUEST_CODE_RECOGNIZE, params);
            } else {
                Toast.makeText(this, "No app support openapi", Toast.LENGTH_LONG).show();
                System.out.println("camcard download link:" + openApi.getDownloadLink());
            }
        } else {
            Toast.makeText(this, "No CamCard", Toast.LENGTH_LONG).show();
            System.out.println("camcard download link:" + openApi.getDownloadLink());
        }
    }
    
    public void testRecognizeImage(String path) {
        if (openApi.isExistAppSupportOpenApi(this)) {
            openApi.recognizeCardByImage(this, path, REQUEST_CODE_RECOGNIZE, params);
        } else {
            Toast.makeText(this, "No app support openapi", Toast.LENGTH_LONG).show();
            System.out.println("camcard download link:" + openApi.getDownloadLink());
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_RECOGNIZE) {
                showResult(data.getStringExtra(OpenApi.EXTRA_KEY_VCF),
                        data.getStringExtra(OpenApi.EXTRA_KEY_IMAGE));
            }
        } else {
            int errorCode = data.getIntExtra(openApi.ERROR_CODE, 200);
            String errorMessage = data.getStringExtra(openApi.ERROR_MESSAGE);
            System.out.println("ddebug error " + errorCode + "," + errorMessage);
            Toast.makeText(this, "Recognize canceled/failed. + ErrorCode " + errorCode + " ErrorMsg " + errorMessage,
                    Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void showResult(String vcf, String path) {
        Intent intent = new Intent(this, ShowResultActivity.class);
        intent.putExtra("result_vcf", vcf);
        intent.putExtra("result_trimed_image", path);
        startActivity(intent);
    }
    
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }
    
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                
            } else {
                // Permission Denied
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "请先允许权限", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    
}
