package com.unibague.pazyregion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.unibague.pazyregion.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView_splashB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView_splashB=findViewById(R.id.image_view_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        Animation animate_down = AnimationUtils.loadAnimation(this,R.anim.displacement_down);

        imageView_splashB.setAnimation(fade_in);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}