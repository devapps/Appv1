package com.infra.qrys_wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.infra.qrys_wallet.Registration.RegisterMobileNo;

/**
 * Created by sandeep.devhare on 14-10-2015.
 */
public class ApplicationIntro extends AppIntro {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(com.infra.qrys_wallet.Utils.Slider_AppIntro.newInstance(R.layout.introscreen_one));
        addSlide(com.infra.qrys_wallet.Utils.Slider_AppIntro.newInstance(R.layout.intro_screen_two));
        addSlide(com.infra.qrys_wallet.Utils.Slider_AppIntro.newInstance(R.layout.intro_screen_three));
        addSlide(com.infra.qrys_wallet.Utils.Slider_AppIntro.newInstance(R.layout.intro_screen_four));

        setFadeAnimation();
    }

    private void loadMainActivity(){

        /*Testing */


       Intent intent = new Intent(this, RegisterMobileNo.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}


