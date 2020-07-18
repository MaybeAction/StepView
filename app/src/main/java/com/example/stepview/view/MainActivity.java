package com.example.stepview.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.stepview.R;
import com.example.stepview.customview.StepView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStepView = findViewById(R.id.step_view);
        ArrayList<String> contents = new ArrayList<>();
        contents.add("one");
        contents.add("two");
        contents.add("three");
        contents.add("four");
        mStepView.setData(contents);
        mStepView.setIndex(2);
    }
}