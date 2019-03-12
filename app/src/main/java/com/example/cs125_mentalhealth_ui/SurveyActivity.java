package com.example.cs125_mentalhealth_ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {
    private Map<Integer,String[]> QA = new HashMap<>();
    private Button selectA;
    private Button selectB;
    private Button selectC;
    private Button selectD;
    private TextView question;
    private int counter = 1;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        fillQA();
        question = findViewById(R.id.tvQuestion);
        selectA = findViewById(R.id.btnBSA);
        selectB = findViewById(R.id.btnBSB);
        selectC = findViewById(R.id.btnBSC);
        selectD = findViewById(R.id.btnSelectD);

        question.setText(QA.get(counter)[0]);
        selectA.setText(QA.get(counter)[1]);
        selectB.setText(QA.get(counter)[2]);
        selectC.setText(QA.get(counter)[3]);
        selectD.setText(QA.get(counter)[4]);

        selectA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record('A');
            }
        });
        selectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record('B');
            }
        });
        selectC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record('C');
            }
        });
        selectD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record('D');
            }
        });
    }

    private void record(char newAnswer){
        if(newAnswer == 'B'){
            score += 1;
        }else if(newAnswer == 'C'){
            score += 2;
        } else if(newAnswer == 'D'){
            score += 3;
        }
        counter++;
        if (counter > 6)
        {
            setResult(Activity.RESULT_OK, new Intent().putExtra("answers",score));
            this.finish();
        }else{
            question.setText(QA.get(counter)[0]);
            for(int i = 1; i < QA.get(counter).length; i++){
                if(i == 1){
                    selectA.setText(QA.get(counter)[i]);
                } else if(i == 2){
                    selectB.setText(QA.get(counter)[i]);
                }else if(i == 3){
                    selectC.setText(QA.get(counter)[i]);
                }else if(i == 4){
                    selectD.setText(QA.get(counter)[i]);
                }
            }
            if(QA.get(counter).length == 5)
            {
                selectD.setVisibility(View.VISIBLE);
            }
            else
            {
                selectD.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void fillQA(){
        QA.put(1,new String[]{"Did you have a sound sleep last night?","A. Yes, of course!","B. Not bad like normal.","C. Very bad sleep.","D. Extremely terrible sleep I have ever have."});
        QA.put(2,new String[]{"How do you feel like when waking up this morning?","A. Wonderful! I can’t wait to have a nice day.", "B. Not special feeling like a normal day.", "C. I felt so bad.", "D. I hate everything would happen today and I just want to disappear."});
        QA.put(3,new String[]{"Did you have food frequently today?","A. Of course! I have good meals today.","B. I don’t have enough food today.","C. I don’t take anything today and I don’t want to eat anything."});
        QA.put(4,new String[]{"Did you enjoy your academic studying today?","A. Yes, I have the classes I take and enjoy the feeling to learn something new.","B. Just have a normal day in college.","C. I don’t enjoy what I learn today."});
        QA.put(5,new String[]{"Did you have a good communication today?","A. Yes, I talked with some people and enjoyed it.","B. Just have a normal conversation with others.","C. I barely talked with anyone and I don’t talk with others."});
        QA.put(6,new String[]{"Finally, how do you feel today?","A. I feel so good and I like the day I have.","B. I don’t have a special feeling.","C. I hate today and I feel so bad."});
    }
}
