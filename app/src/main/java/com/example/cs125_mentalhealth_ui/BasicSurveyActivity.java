package com.example.cs125_mentalhealth_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BasicSurveyActivity extends AppCompatActivity {
    private String username;
    private  String password;
    private String prefer;
    private int[] scoreRubric1 = {2, 3, 4, 7, 10, 11, 12, 13, 14, 16, 17, 18, 19};

    private int score = 0;
    private Map<Integer,String[]> BSQA = new HashMap<>();
    private Button BSA;
    private Button BSB;
    private Button BSC;
    private Button BSD;
    private Button BSE;
    private TextView BSQ;
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_survey);

        fillBSQA();
        BSQ = findViewById(R.id.tvBSQ);
        BSA = findViewById(R.id.btnBSA);
        BSB = findViewById(R.id.btnBSB);
        BSC = findViewById(R.id.btnBSC);
        BSD = findViewById(R.id.btnBSD);
        BSE = findViewById(R.id.btnBSE);

        BSQ.setText(BSQA.get(counter)[0]);
        BSA.setText(BSQA.get(counter)[1]);
        BSB.setText(BSQA.get(counter)[2]);
        BSC.setText(BSQA.get(counter)[3]);

        BSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBSQA('A');
            }
        });
        BSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBSQA('B');
            }
        });
        BSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBSQA('C');
            }
        });
        BSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBSQA('D');
            }
        });
        BSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBSQA('E');
            }
        });
    }

    private void recordBSQA(char selection){
        update_score(selection);
        counter++;
        if(counter > 21){
            Bundle bundle = getIntent().getExtras();
            if(bundle.getString("username") != null) {
                username = bundle.getString("username");
            }
            if(bundle.getString("password") != null) {
                password = bundle.getString("password");
            }
            String resp = null;
            if(bundle.getString("APIResponse") != null){
                resp = bundle.getString("APIResponse");
            }

            Bundle extras = new Bundle();
            extras.putString("username",username);
            extras.putString("password",password);
            extras.putString("Activity","BasicSurvey");
            extras.putString("APIResponse", resp);
            extras.putString("prefer",prefer);
            extras.putInt("basicSurveyScore",score);
            Intent dashboard = new Intent(BasicSurveyActivity.this, DashboardActivity.class);
            dashboard.putExtras(extras);
            this.finish();
            startActivity(dashboard);
        }else{
            BSQ.setText(BSQA.get(counter)[0]);
            for(int i = 1; i < BSQA.get(counter).length; i++){
                if(i == 1){
                    BSA.setText(BSQA.get(counter)[i]);
                } else if(i == 2){
                    BSB.setText(BSQA.get(counter)[i]);
                }else if(i == 3){
                    BSC.setText(BSQA.get(counter)[i]);
                    BSD.setVisibility(View.INVISIBLE);
                    BSE.setVisibility(View.INVISIBLE);
                }else if(i == 4){
                    BSD.setText(BSQA.get(counter)[i]);
                    BSD.setVisibility(View.VISIBLE);
                }else if(i == 5){
                    BSE.setText(BSQA.get(counter)[i]);
                    BSE.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void fillBSQA(){
        BSQA.put(1, new String[]{"How is your adaptation to university life?", "A. Poor", "B. Still Alright", "C. Well"});
        BSQA.put(2, new String[]{"If you have a low learning efficiency during the hard study, will you feel upset?","A. Often","B. Sometimes","C. Rarely","D. No"});
        BSQA.put(3, new String[]{"Are you always nervous before and after the exam?"," A. Often","B. sometimes","C. Rarely","D. Will not"});
        BSQA.put(4, new String[]{"Do you think that tension will malign influence on your test scores?","A. Often","B. sometimes","C. Rarely","D.  will not"});
        BSQA.put(5, new String[]{"What role do you think pressure plays in your learning life?","A. More negative effects will affect emotions","B. no feelings","C. Positive influence will bring motivation to learning"});
        BSQA.put(6, new String[]{"Do you think that the dormitory, classroom, and surrounding environment are conducive to college students?","A. Very beneficial"," B. More favorable","C. general","D. More disadvantageous","E. Very disadvantageous"});
        BSQA.put(7, new String[]{"Are you dissatisfied with the quality of the school, class or teaching?","A. Often have"," B. Occasionally","C. rare","D. No"});
        BSQA.put(8, new String[]{"Are you satisfied with your body shape and appearance?","A. Very satisfied","B. satisfaction","C. Does not matter","D. Not satisfied","E. Very dissatisfied"});
        BSQA.put(9, new String[]{"How do you get along with class and dormitory students?","A. well"," B. still alright","C. not too good","D. very bad"});
        BSQA.put(10, new String[]{"Do you think that there are obstacles in communication between others and you?","A. There are big obstacles","B. Sometimes have obstacles","C. A bit less","D. No"});
        BSQA.put(11, new String[]{"Does your situation with your classmates make you feel stressed?","A. Often","B. Sometimes","C. Few times","D. No"});
        BSQA.put(12, new String[]{"Do you feel pressure when you are rejected by the crushes or pursued by the others?","A. A lot of pressure"," B. Depending on the situation","C. Less stress","D. Did not give a damn about"});
        BSQA.put(13, new String[]{"Have you ever felt unhappy about how to fall in love with other relationships?","A. Often","B. Less","C. Sometimes"," D. No"});
        BSQA.put(14, new String[]{"Do you have pressure for financial reasons?","A. Often have"," B. Sometimes","C. Few","D. No"});
        BSQA.put(15, new String[]{"Are you satisfied with the major you have studied?","A. Very satisfied","B. General","C. Dissatisfied","D. Very dissatisfied"});
        BSQA.put(16, new String[]{"Do you have pressure from your academic performance?","A. Often have"," B. Sometimes","C. Less","D. No"});
        BSQA.put(17, new String[]{"How do you evaluate your parents' expectations for yourself?","A. Their expectations are very high","B. High","C. General","D. Low"});
        BSQA.put(18, new String[]{"Do you think that parents' expectations will bring you pressure?","A. Very large pressure","B. General","C. Rarely","D. No"});
        BSQA.put(19, new String[]{"Are you confused and worried about your future?","A. Often","B. Occasionally","C. Rarely","D. No"});
        BSQA.put(20, new String[]{"Who will you help first when you are under pressure?","A. Family","B. Friends","C. Other","D. No one"});
        BSQA.put(21, new String[]{"How do you deal with stress?","A. Listening to music","B. Watch videos","C. Talking with friend","D. other"});
    }

    private void update_score(char selection){
        if(Arrays.asList(scoreRubric1).contains(counter)) {
            if(selection == 'A'){
                score += 3;
            } else if(selection == 'B') {
                score += 2;
            } else if(selection == 'C'){
                score += 1;
            }
        }else if(counter == 1 || counter == 5) {
            if(selection == 'A'){
                score += 2;
            } else if(selection == 'B') {
                score += 1;
            }
        }else if(counter == 6 || counter == 8 || counter == 9) {
            if(selection == 'A'){
                score -= 1;
            } else if(selection == 'D') {
                score += 2;
            } else if(selection == 'C'){
                score += 1;
            } else if(selection == 'E'){
                score += 3;
            }
        } else if(counter == 20 && selection == 'D')
        {
            score += 2;
        } else if(counter == 21)
        {
            if(selection == 'A'){
                prefer = "music";
            }else if(selection == 'B'){
                prefer = "video";
            }else{
                prefer = "";
            }
        }
    }
}
