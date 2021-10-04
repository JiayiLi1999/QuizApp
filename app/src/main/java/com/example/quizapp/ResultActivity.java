package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private int failTimes = 0;
    private int successTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        boolean isSuccess = intent.getBooleanExtra("isSuccess",false);
        TextView resultView = findViewById(R.id.isSuccess);
        if(isSuccess){
            resultView.setText("You Nailed It!");
            successTimes++;
        }else{
            resultView.setText("You Failed...");
            failTimes++;
        }
    }

    public void onClickShare(View view) {
        String text = String.format("You succeed %d times, failed %d times",successTimes,failTimes);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooserIntent = Intent.createChooser(intent,"Send Message Via...");
        startActivity(chooserIntent);
    }
}