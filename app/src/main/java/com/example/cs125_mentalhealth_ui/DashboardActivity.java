package com.example.cs125_mentalhealth_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DashboardActivity extends AppCompatActivity {
    private char[] answers = null;
    private char[] basicSurveyResponse;
    private String username;
    private String password;
    private Button signout;
    private Button btnSurvey;
    private TextView tvdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        signout = findViewById(R.id.btnSignOut);
        btnSurvey = findViewById(R.id.btnSurvey);
        tvdate = findViewById(R.id.tvDate);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        tvdate.setText(sdf.format(calendar.getTime()));

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("username") != null) {
            username = bundle.getString("username");
        }
        if(bundle.getString("password") != null) {
            password = bundle.getString("password");
        }
        if(bundle.getString("Activity").equals("BasicSurvey")){
            basicSurveyResponse = bundle.getCharArray("basicSurveyResponse");
        }

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent survey = new Intent(DashboardActivity.this, SurveyActivity.class);
                startActivityForResult(survey, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0 && resultCode == Activity.RESULT_OK)
        {
            answers = data.getCharArrayExtra("answers");
        }
    }

    private void signOut(){
        Intent signIn = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(signIn);
        this.finish();
    }

}
