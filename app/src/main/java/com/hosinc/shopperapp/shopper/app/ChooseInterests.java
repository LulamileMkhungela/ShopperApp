package com.hosinc.shopperapp.shopper.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseInterests extends AppCompatActivity {
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interests);
        btnNext = findViewById(R.id.btn_next);

        Intent interestActivity = new Intent(getApplicationContext(), HomeNavigation.class );
        startActivity(interestActivity);
        finish();

    }
}