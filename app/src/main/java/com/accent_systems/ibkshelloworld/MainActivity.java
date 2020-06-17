package com.accent_systems.ibkshelloworld;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    TextView prox;
    TextView config;
    TextView route;

    LinearLayout scanBtn;
    LinearLayout configBtn;
    LinearLayout routeBtn;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_COARSE_BL = 2;

    //THIS METHOD RUNS ON APP LAUNCH
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prox = (TextView) findViewById(R.id.proxTV);
        prox.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Khand-Bold.ttf"));
        config = (TextView) findViewById(R.id.configTV);
        config.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Khand-Bold.ttf"));
        route = (TextView) findViewById(R.id.routeTV);
        route.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Khand-Bold.ttf"));

        SpannableString s = new SpannableString("DMO BLE Config");

        s.setSpan(new com.accent_systems.ibkshelloworld.TypefaceSpan(this,"Khand-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#3a3c3e")), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(s);

        assert getSupportActionBar() != null;
        getSupportActionBar().setLogo(R.mipmap.ibks);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scanBtn = (LinearLayout) findViewById(R.id.proxBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startProxDemoActivity = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(startProxDemoActivity);
            }
        });

        configBtn = (LinearLayout) findViewById(R.id.configBtn);
        configBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startConfigActivity = new Intent(MainActivity.this, ConfigurationActivity.class);
                startActivity(startConfigActivity);
            }
        });

        routeBtn = (LinearLayout) findViewById(R.id.routeBtn);
        routeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRouteActivity = new Intent(MainActivity.this, RoutingActivity.class);
                startActivity(startRouteActivity);
            }
        });


        checkLocBT();
        inicializeBluetooth();
    }



    private BluetoothAdapter mBTAdapter;

    private void inicializeBluetooth(){

        //Check if device does support BT by hardware
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLUETOOTH NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Check if device does support BT Low Energy by hardware. Else close the app(finish())!
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLE NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            //If BLE is supported, get the BT adapter. Preparing for use!
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            //If getting the adapter returns error, close the app with error message!
            if (mBTAdapter == null) {
                Toast.makeText(this, "ERROR GETTING BLUETOOTH ADAPTER!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Check if BT is enabled! This method requires BT permissions in the manifest.
                if (!mBTAdapter.isEnabled()) {
                    //If it is not enabled, ask user to enable it with default BT enable dialog! BT enable response will be received in the onActivityResult method.
                    Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTintent, PERMISSION_REQUEST_COARSE_BL);
                }
            }
        }
    }

    @TargetApi(23)
    private void checkLocBT(){
        //If Android version is M (6.0 API 23) or newer, check if it has Location permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //If Location permissions are not granted for the app, ask user for it! Request response will be received in the onRequestPermissionsResult.
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        //Check if permission request response is from Location
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //User granted permissions. Setup the scan settings
                    Log.d("TAG", "coarse location permission granted");
                } else {
                    //User denied Location permissions. Here you could warn the user that without
                    //Location permissions the app is not able to scan for BLE devices and eventually
                    //Close the app
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the response is from BT
        if(requestCode == PERMISSION_REQUEST_COARSE_BL){
            // User chose not to enable Bluetooth.
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
