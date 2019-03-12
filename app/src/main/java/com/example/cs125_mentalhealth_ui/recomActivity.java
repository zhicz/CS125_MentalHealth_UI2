package com.example.cs125_mentalhealth_ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class recomActivity extends AppCompatActivity {
    private Button btnrate;
    private RatingBar rbRate;
    private TextView tvRecom;
    private int recom_id;
    private String recom_type;
    private String session;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom);

        btnrate = findViewById(R.id.btnRate);
        rbRate = findViewById(R.id.rbRating);
        tvRecom = findViewById(R.id.tvRecom);

        Bundle b = getIntent().getExtras();
        if(b.getString("recomendation") != null){
            tvRecom.setText(b.getString("recomendation"));
        }
        if(b.getInt("recom_id") >= 0){
            recom_id = b.getInt("recom_id");
        }
        if(b.getString("recom_type") != null){
            recom_type = b.getString("recom_type");
        }
        if(b.getString("session") != null){
            session = b.getString("session");
        }
        if(b.getInt("user_id") >= 0){
            user_id = b.getInt("user_id");
        }

        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbRate.getNumStars() != 0) {
                    rate();
                }
            }
        });

    }

    private void rate(){
        int rating = rbRate.getNumStars();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONObject data = new JSONObject().put("score",rating).put("date",sdf.format(calendar.getTime())).put("user_id",user_id);
            send_http_request insert_rating = new send_http_request();
            if (recom_type.equals("music")) {
                data = data.put("music_id", recom_id);
                insert_rating.insert_recom_rating(user_id, data, session, "music");
            } else {
                data = data.put("video_id", recom_id);
                insert_rating.insert_recom_rating(user_id, data, session, "video");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.finish();

    }
}
