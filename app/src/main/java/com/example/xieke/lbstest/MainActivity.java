package com.example.xieke.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLoactionClient;

    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoactionClient = new LocationClient(getApplicationContext());
        mLoactionClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"拒绝授权",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("维度：").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("定位方式:");
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("Network");
            }
            positionText.setText(currentPosition);
        }
    }

    private void requestLocation(){
        mLoactionClient.start();
    }
}
