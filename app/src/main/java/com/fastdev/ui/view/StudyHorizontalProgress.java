package com.fastdev.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fastdev.ui.R;


/**
 * @author: ton
 * @date: 2022/4/19
 */
public class StudyHorizontalProgress extends FrameLayout {
    private Space spaceStrat, spaceEnd;
    private ProgressBar progressBar;
    private LinearLayout viewStandard;

    public StudyHorizontalProgress(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public StudyHorizontalProgress(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
//        setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.view_main_study_horizontal_progress, this, true);

        spaceStrat = findViewById(R.id.space_start);
        spaceEnd = findViewById(R.id.space_end);
        progressBar = findViewById(R.id.progress);

//        viewStandard = findViewById(R.id.view_standard);

        if(isInEditMode()){
            setStandarRate(0.8f);
            setProgress(75);
        }
    }

    public void setStandarRate(float standardRate){
        if(standardRate < 0) standardRate = 0;
        if(standardRate > 1) standardRate = 1;

        if(standardRate > 1) standardRate = 1;
        ((LinearLayout.LayoutParams)spaceStrat.getLayoutParams()).weight = standardRate;
        spaceStrat.invalidate();
        ((LinearLayout.LayoutParams)spaceEnd.getLayoutParams()).weight = 1- standardRate;
        spaceEnd.invalidate();
    }

    public void setProgress(float progress){
        if(progress < 0) progress = 0;
        if(progress > 100) progress = 100;

        progressBar.setMax(100);
        progressBar.setProgress((int) (progress * 100));
    }
}
