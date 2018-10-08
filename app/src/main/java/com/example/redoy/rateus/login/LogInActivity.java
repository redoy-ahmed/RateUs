package com.example.redoy.rateus.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.redoy.rateus.R;
import com.example.redoy.rateus.rating.RatingActivity;

public class LogInActivity extends AppCompatActivity {

    private Button mLogInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mLogInButton = findViewById(R.id.btn_server_login);

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, RatingActivity.class));
            }
        });
    }
}
