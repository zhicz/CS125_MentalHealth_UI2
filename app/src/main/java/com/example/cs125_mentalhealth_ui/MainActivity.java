package com.example.cs125_mentalhealth_ui;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button signIn;
    private Button signUp;
    private TextView invalidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        signIn = findViewById(R.id.btnSignIn);
        signUp = findViewById(R.id.btnSignUp);
        invalidText = findViewById(R.id.tvInvalid);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });

    }

    private void reg(){
        Intent reg = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(reg);
        etUsername.setText("");
        etPassword.setText("");
    }

    private void validate(String name, String userPassword){
        //get name and password checked in db
        send_http_request checkLogin = new send_http_request();
        if(checkLogin.connect_api(name,userPassword, "/user/login","Auth successful")){
            Intent dashboard = new Intent(MainActivity.this, DashboardActivity.class);
            invalidText.setVisibility(View.INVISIBLE);

            String resp = checkLogin.get_resp();

            Bundle extras = new Bundle();
            extras.putString("username",name);
            extras.putString("password",userPassword);
            extras.putString("Activity","SignIn");
            extras.putString("APIResponse", resp);
            dashboard.putExtras(extras);
            startActivity(dashboard);
        }
        else
        {
            invalidText.setVisibility(View.VISIBLE);
        }
    }

}
