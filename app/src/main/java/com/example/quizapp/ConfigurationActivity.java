package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }


    public void onClickSubmit(View view) {
        RadioGroup radioGroup = findViewById(R.id.toggle_function);
        TextView timeView = findViewById(R.id.time_limit);
        Switch imageButton = findViewById(R.id.image_button);
        String timeLimitString = timeView.getText().toString();
        int index = 1;
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.toggle_function_1:
                index = 1;
                break;
            case R.id.toggle_function_2:
                index = 2;
                break;
            case R.id.toggle_function_3:
                index = 3;
                break;
        }
        int timeLimit = 10000;
        if(!timeLimitString.equals("")){
            timeLimit = Integer.parseInt(timeLimitString);
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("toggle_function",index);
        intent.putExtra("time_limit",timeLimit);
        intent.putExtra("image_button",imageButton.isChecked());
        startActivity(intent);
    }
}