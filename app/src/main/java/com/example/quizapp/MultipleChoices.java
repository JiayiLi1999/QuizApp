package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
    private boolean mIsBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choices);
        TextView timesText = findViewById(R.id.attempts_left);
        timesText.append(String.valueOf(attemptsLeft));

        Intent intent = new Intent(this,TimerService.class);
        mIsBound = bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if(savedInstanceState!=null) {
            if (bound && timer != null) {
                timer.setTimeA(savedInstanceState.getInt("secondsA"));
                timer.setTimeB(savedInstanceState.getInt("secondsB"));
            }
        }
        runTimeA();
        runTimeB();
        runningA = true;
        runningB = true;
        Intent Setting = getIntent();
        setUp(Setting);
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
        clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("secondsA",secondsA);
        outState.putInt("secondsB",secondsB);
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
        attemptsLeft = 2;
        runningA = false;
        runningB = false;
        if (bound && timer != null) {
            timer.setTimeA(0);
            timer.setTimeB(0);
        }
        if(mIsBound) {
            unbindService(connection);
            mIsBound = false;
        }
        bound = false;
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

    private TimerService timer;
    private boolean bound = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.TimerBinder timerBinder = (TimerService.TimerBinder) service;
            timer = timerBinder.getTimer();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };
    private void runTimeA(){
        final TextView timeViewA = findViewById(R.id.timerA);
        handler.post(new Runnable() {
            @Override
            public void run() {
                int time = 0;
                if (runningA && bound && timer != null) {
                    time = timer.getTimeA();
                    if(time>time_limit) {
                        fail();
                        timer.passTime();
                    }
                }
                int hrsA = time/3600;
                int minA = (time%3600)/60;
                int secA = time%60;
                String timeA = String.format("%d:%02d:%02d",hrsA,minA,secA);
                if(runningA) timeViewA.setText(timeA);
                handler.postDelayed(this,1000);
            }
        });
    }
    private void runTimeB(){
        final TextView timeViewB = findViewById(R.id.timerB);
        handler.post(new Runnable() {
            @Override
            public void run() {
                int time = 0;
                if (runningB && bound && timer != null) {
                    time = timer.getTimeB();
                    if(time>time_limit) {
                        fail();
                        timer.passTime();
                    }
                }
                int hrsB = time/3600;
                int minB = (time%3600)/60;
                int secB = time%60;
                String timeB = String.format("%d:%02d:%02d",hrsB,minB,secB);
                if(runningB) timeViewB.setText(timeB);
                handler.postDelayed(this,1000);
            }
        });
    }
}