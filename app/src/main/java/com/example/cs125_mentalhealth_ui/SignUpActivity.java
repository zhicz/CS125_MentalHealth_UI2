package com.example.cs125_mentalhealth_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etMajor;
    private EditText etAge;
    private EditText etGender;
    private EditText etCity;
    private EditText etState;
    private EditText etCountry;
    private Button signInFromSignUp;
    private Button backToSignIn;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etNewUser);
        etPassword = findViewById(R.id.etNewPassword);
        etPhone = findViewById(R.id.etPhone);
        etMajor = findViewById(R.id.etMajor);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etCountry = findViewById(R.id.etCountry);
        signInFromSignUp = findViewById(R.id.btnSignInFromReg);
        backToSignIn = findViewById(R.id.btnBack);
        warning = findViewById(R.id.tvUsernameExists);

        signInFromSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newUserSignIn(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSignInPage();
            }
        });
    }

    private void newUserSignIn(String user, String pw){
        //check if user and password already exists
        send_http_request newSignUp = new send_http_request();
        String phone = etPhone.getText().toString();
        String major = etMajor.getText().toString();
        String age = etAge.getText().toString();
        String gender = etGender.getText().toString();
        int gender_num;
        if(gender.equals("male")){
            gender_num = 1;
        }else if(gender.equals("female")){
            gender_num = 2;
        }else{
            gender_num = 3;
        }
        String city = etCity.getText().toString();
        String state = etState.getText().toString();
        String country = etCountry.getText().toString();
        newSignUp.set_param(phone,major,age,gender_num,city,state,country);

        if(newSignUp.connect_api(user,pw,"/user/create_account","User Created")){
            if(newSignUp.connect_api(user,pw,"/user/login","Auth successful")) {
                warning.setVisibility(View.INVISIBLE);
                String resp = newSignUp.get_resp();
                Bundle extras = new Bundle();
                extras.putString("username", user);
                extras.putString("password", pw);
                extras.putString("APIResponse", resp);
                Intent basicSurvey = new Intent(SignUpActivity.this, BasicSurveyActivity.class);
                basicSurvey.putExtras(extras);
                finish();
                startActivity(basicSurvey);
            }else{
                this.finish();
            }
        } else{
            //prompt user to enter new username and password
            String empty = "";
            warning.setVisibility(View.VISIBLE);
            etUsername.setText(empty);
            etPassword.setText(empty);
        }
    }

    private void backToSignInPage(){
        this.finish();
    }

}
