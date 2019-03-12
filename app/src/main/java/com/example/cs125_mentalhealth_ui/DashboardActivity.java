package com.example.cs125_mentalhealth_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DashboardActivity extends AppCompatActivity {
    private int surveyScore = 0;
    private int basicSurveyScore;
    private String username;
    private String password;
    private JSONObject resp;
    private Button signout;
    private Button btnSurvey;
    private TextView tvdate;
    private TextView tvHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        signout = findViewById(R.id.btnSignOut);
        btnSurvey = findViewById(R.id.btnSurvey);
        tvdate = findViewById(R.id.tvDate);
        tvHealth = findViewById(R.id.tvHPIndex);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
        if(bundle.getString("APIResponse") != null)
        {
            try{
                resp = new JSONObject(bundle.getString("APIResponse"));
            }catch(Exception e){
                resp = null;
                e.printStackTrace();
            }
        }
        if(bundle.getString("Activity").equals("BasicSurvey")){
            basicSurveyScore = bundle.getInt("basicSurveyScore");
            String user_prefer = bundle.getString("prefer");
            if(user_prefer.equals("") == false){
                update_user_preference(user_prefer);
            }
            handle_basic_survey();
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
            surveyScore = data.getIntExtra("answers",0);
            send_http_request insert_survey = new send_http_request();
            if(surveyScore <= 3){
                tvHealth.setText("Happy");
            }else if(surveyScore <= 8){
                tvHealth.setText("Content");
            }else if(surveyScore <= 11){
                tvHealth.setText("Stressed");
            }else if(surveyScore > 11){
                tvHealth.setText("Danger");
            }

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                insert_survey.insert_survey_data(resp.getJSONObject("data"), surveyScore, sdf.format(calendar.getTime()));

                System.out.println("Inserting Survey data..." + surveyScore + " : " + sdf.format(calendar.getTime()));

                //get recommendation
                JSONObject recom = insert_survey.get_recom(resp.getJSONObject("data").getInt("id"),resp.getJSONObject("data").getString("session"));
                System.out.println("Recommendation data: " + recom.toString());
                //build recommendation page
                Intent test_recom = new Intent(DashboardActivity.this, recomActivity.class);
                Bundle extras = new Bundle();
                extras.putString("recomendation",recom.getString("link"));
                extras.putInt("recom_id",recom.getInt("id"));
                extras.putString("recom_type",recom.getString("type"));
                extras.putInt("user_id",resp.getJSONObject("data").getInt("id"));
                extras.putString("session",resp.getJSONObject("data").getString("session"));
                test_recom.putExtras(extras);
                startActivity(test_recom);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void signOut(){
        Intent signIn = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(signIn);
        this.finish();
    }

    private void handle_basic_survey(){
        if(basicSurveyScore<= 5){
            insert_basic_survey(2);
        }else if(basicSurveyScore <= 25){
            insert_basic_survey(6);
        }else if(basicSurveyScore <= 45){
            insert_basic_survey(10);
        }else if(basicSurveyScore > 45){
            insert_basic_survey(13);
        }
    }

    private void insert_basic_survey(int score){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            send_http_request insert_survey = new send_http_request();
            JSONObject user_data = resp.getJSONObject("data");
            for(int i = 1; i < 6; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                insert_survey.insert_survey_data(user_data, score, sdf.format(calendar.getTime()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void update_user_preference(String preference){
        try{
            send_http_request update_prefer = new send_http_request();
            JSONObject user_data = resp.getJSONObject("data");
            update_prefer.update_user_preference(user_data,preference);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
