package com.mbds.material.PatrouilleNFC.Help;

/**
 * Created by Safidimahefa on 07/04/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mbds.material.PatrouilleNFC.MainActivity;
import com.naokistudio.material.PatrouilleNFC.R;


public class DefaultHelp extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.help1));
        addSlide(SampleSlide.newInstance(R.layout.help2));
        addSlide(SampleSlide.newInstance(R.layout.help3));
        addSlide(SampleSlide.newInstance(R.layout.help4));
        addSlide(SampleSlide.newInstance(R.layout.help5));
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        //Toast.makeText(getApplicationContext(),
              //  getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}

