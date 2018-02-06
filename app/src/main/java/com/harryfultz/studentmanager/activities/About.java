package com.harryfultz.studentmanager.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;

import com.harryfultz.studentmanager.R;

public class About extends AppCompatActivity {

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle e) {
        super.onCreate(e);
        setContentView(R.layout.about_layout);
        appBarAnimation();

    }

    private void appBarAnimation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);


        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.wallpaper);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getVibrantSwatch().getRgb();
                collapsingToolbarLayout.setContentScrimColor(mutedColor);
                collapsingToolbarLayout.setStatusBarScrimColor(mutedColor);

            }
        });

    }

}