package com.accent_systems.ibkshelloworld;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.WindowManager;

public class RoutingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_routing);
        setContentView(R.layout.activity_configuration);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableString s = new SpannableString("Routing Table");

        s.setSpan(new com.accent_systems.ibkshelloworld.TypefaceSpan(this, "Khand-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#3a3c3e")), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(s);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void getRoutingTable() {
        // Regel hier dat je de waarde van de BLE service kunt opvvragen.
    }
}
