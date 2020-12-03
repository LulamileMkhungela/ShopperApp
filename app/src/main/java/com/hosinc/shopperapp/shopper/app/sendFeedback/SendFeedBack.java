package com.hosinc.shopperapp.shopper.app.sendFeedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hosinc.shopperapp.shopper.app.R;

public class SendFeedBack extends AppCompatActivity {

    private EditText subject;
    private EditText description;
    private String email, type, doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_feedback);
        setTitle("Send Feedback");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        final LinearLayout linearLayout = findViewById(R.id.relative_send_feedback);
        Button send = findViewById(R.id.btn_submit_feedBack);
        Button cancel = findViewById(R.id.btn_backTo_profile);

        cancel.setOnClickListener(view -> finish());

        subject = findViewById(R.id.etSendFeedbackSubject);
        description = findViewById(R.id.etProvideDetails);

        Intent intent = getIntent();
        if (intent.hasExtra("type") && intent.hasExtra("postRef")) {
            type = intent.getStringExtra("type");
            doc = intent.getStringExtra("postRef");
            if (type != null && type.equals("Report")) {
                subject.setHint("What Would you like to report.(Optional)");
                TextView heading = findViewById(R.id.compose_feedback);
                heading.setText(R.string.feedback_heading);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("FeedBack");
                }
            } else {
                subject.setHint("Name(Optional).");
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Feedback");
                }
            }
        }

        send.setOnClickListener(view -> {
            String sub = subject.getText().toString().trim();
            String des = description.getText().toString().trim();
            if (email != null && !des.isEmpty()) {
                String body;
                if (type != null && type.equals("FeedBack")) {
                    body = "Email: " + email + "\n\n" + "FeedBack Query: " + doc + "\n\n" + des;
                } else {
                    body = "Email: " + email + "\n\n" + des;
                }

                com.hosinc.shopperapp.shopper.app.sendFeedback.SendMail sm = new com.hosinc.shopperapp.shopper.app.sendFeedback.SendMail(com.hosinc.shopperapp.shopper.app.sendFeedback.SendFeedBack.this, "lulamile@opensesyme.com", sub, body);
                sm.execute();
            } else {
                if (des.isEmpty()) {
                    description.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
                Snackbar snackbar = Snackbar.make(linearLayout, "Oops,Please provide more details", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
