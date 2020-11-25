package com.hosinc.shopperapp.shopper.app.registrationProcess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hosinc.shopperapp.shopper.app.HomeNavigation;
import com.hosinc.shopperapp.shopper.app.R;

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