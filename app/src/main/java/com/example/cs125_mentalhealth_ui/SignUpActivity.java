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
    private Button signInFromSignUp;
    private Button backToSignIn;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etNewUser);
        etPassword = findViewById(R.id.etNewPassword);
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

        if(newSignUp.connect_api(user,pw,"/user/create_account","User Created")){
            warning.setVisibility(View.INVISIBLE);

            Bundle extras = new Bundle();
            extras.putString("username",user);
            extras.putString("password",pw);

            Intent basicSurvey = new Intent(SignUpActivity.this, BasicSurveyActivity.class);
            basicSurvey.putExtras(extras);
            finish();
            startActivity(basicSurvey);
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
