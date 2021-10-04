package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoices extends AppCompatActivity {

    private int attemptsLeft = 3;
    private int secondsA = 0;
    private int secondsB = 0;
    private boolean runningA = false;
    private boolean runningB = false;
    private int toggleType;
    private int time_limit;
    private boolean isButtonImage;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choices);
        TextView timesText = findViewById(R.id.attempts_left);
        timesText.append(String.valueOf(attemptsLeft));
        if(savedInstanceState!=null) {
            secondsA = savedInstanceState.getInt("secondsA");
            secondsB = savedInstanceState.getInt("secondsB");
        }
        runTimeA();
        runTimeB();
        runningA = true;
        runningB = true;
        Intent Setting = getIntent();
        setUp(Setting);
    }

    private void setUp(Intent setting) {
        toggleType = setting.getIntExtra("toggle_function",1);
        time_limit = setting.getIntExtra("time_limit",10000);
        isButtonImage = setting.getBooleanExtra("image_button",false);
        ImageButton imageButtonView = findViewById(R.id.q2_submit_image);
        Button buttonView = findViewById(R.id.q2_submit);
        if(isButtonImage){
            imageButtonView.setVisibility(View.VISIBLE);
            buttonView.setVisibility(View.GONE);
        }else{
            imageButtonView.setVisibility(View.GONE);
            buttonView.setVisibility(View.VISIBLE);
        }
    }
    private void clear() {
        runningA = false;
        runningB = false;
        secondsA = 0;
        secondsB = 0;
        attemptsLeft = 2;
    }


    public void onClickSubmit(View view) {
        boolean result_1 = true;
        result_1 = !((CheckBox)findViewById(R.id.checkbox_boomslang)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_elephant)).isChecked()
                && !((CheckBox)findViewById(R.id.checkbox_jerboa)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_lion)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_rhino)).isChecked();

        boolean result_2 = true;
        result_1 = !((CheckBox)findViewById(R.id.checkbox_bird)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_dog)).isChecked()
                && !((CheckBox)findViewById(R.id.checkbox_cat)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_pig)).isChecked()
                && ((CheckBox)findViewById(R.id.checkbox_dragon)).isChecked();


        TextView timesText = findViewById(R.id.attempts_left);

        if(result_1&&result_2){
            Intent intent = new Intent(this,ResultActivity.class);
            intent.putExtra("isSuccess",true);
            startActivity(intent);
        }else{
            if(attemptsLeft == 0){
                fail();
            }else{
                attemptsLeft--;
                timesText.setText(String.format("%s%d", getString(R.string.attempt_lefts), attemptsLeft));
                clearCheckBox();
            }
        }
    }

    private void fail() {
        clear();
        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra("isSuccess",false);
        startActivity(intent);
        handler.removeCallbacksAndMessages(null);
    }

    public void clearCheckBox(){
        if(((CheckBox)findViewById(R.id.checkbox_boomslang)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_boomslang)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_jerboa)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_jerboa)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_rhino)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_rhino)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_lion)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_lion)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_elephant)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_elephant)).setChecked(false);
        }

        if(((CheckBox)findViewById(R.id.checkbox_dragon)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_dragon)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_cat)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_cat)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_pig)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_pig)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_bird)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_bird)).setChecked(false);
        }
        if(((CheckBox)findViewById(R.id.checkbox_dog)).isChecked()){
            ((CheckBox)findViewById(R.id.checkbox_dog)).setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        runningB = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        runningB = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        secondsA = 0;
        runningA = false;
        secondsB = 0;
        runningB = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("secondsA",secondsA);
        outState.putInt("secondsB",secondsB);
    }

    private void runTimeA(){
        final TextView timeViewA = findViewById(R.id.timerA);
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hrsA = secondsA/3600;
                int minA = (secondsA%3600)/60;
                int secA = secondsA%60;
                String timeA = String.format("%d:%02d:%02d",hrsA,minA,secA);
                timeViewA.setText(timeA);
                if(runningA) secondsA++;
                handler.postDelayed(this,1000);
            }
        });
    }
    private void runTimeB(){
        final TextView timeViewB = findViewById(R.id.timerB);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(secondsB>time_limit){
                    fail();
                }
                int hrsB = secondsB/3600;
                int minB = (secondsB%3600)/60;
                int secB = secondsB%60;
                String timeB = String.format("%d:%02d:%02d",hrsB,minB,secB);
                timeViewB.setText(timeB);
                if(runningB) secondsB++;
                handler.postDelayed(this,1000);
            }
        });
    }
    public void onToggleButton(View view) {
        boolean isOn = ((ToggleButton)view).isChecked();
        String s = view.getResources().getResourceName(view.getId());
        TextView textview;
        boolean isTimerA = s.endsWith("A");
        if(isTimerA){
            textview = findViewById(R.id.timerA);
        }else{
            textview = findViewById(R.id.timerB);
        }
        if(!isOn){
            switch (toggleType){
                case 1:
                    textview.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    textview.setVisibility(View.INVISIBLE);
                    if(isTimerA)runningA = false;
                    else runningB = false;
                    break;
                case 3:
                    if(isTimerA)runningA = false;
                    else runningB = false;
                    break;
            }
        }else{
            switch (toggleType){
                case 1:
                    textview.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    textview.setVisibility(View.VISIBLE);
                    if(isTimerA)runningA = true;
                    else runningB = true;
                    break;
                case 3:
                    if(isTimerA)runningA = true;
                    else runningB = true;
                    break;
            }
        }

    }
}