package com.westone.mylivepush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.TextureView;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,//定位权限
            Manifest.permission.ACCESS_FINE_LOCATION,//定位权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
            Manifest.permission.READ_PHONE_STATE//读取手机状态权限
    };
    private static final int PERMISSON_REQUESTCODE = 0;

    private TextureView textureView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions(needPermissions);
        textureView = findViewById(R.id. local_textureView);
        ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @SuppressLint("UnsafeExperimentalUsageError")
            @Override
            public void run() {
                try {
                    ProcessCameraProvider processCameraProvider = cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();
                    Camera camera = processCameraProvider.bindToLifecycle((LifecycleOwner) MainActivity.this, cameraSelector, preview);
                    preview.setTargetRotation(textureView.getDisplay().getRotation());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void checkPermissions(String... permissions){
        if (checkPermissionsGranted(permissions)){ //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }

    private boolean checkPermissionsGranted(String... permissions){
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return  false;
            }
        }
        return true;
    }
}