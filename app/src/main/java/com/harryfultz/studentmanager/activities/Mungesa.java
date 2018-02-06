package com.harryfultz.studentmanager.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.appConfigAndDB.DatabaseHelper;
import com.harryfultz.studentmanager.appConfigAndDB.Initializer;
import com.harryfultz.studentmanager.appConfigAndDB.StatusBarColor;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;


public class Mungesa extends AppCompatActivity {


    Initializer init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.absence_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarColor.Companion.changeStatusBarColor(this);
        init = new Initializer(this, getApplicationContext());
        init.initializeAllViews();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        init.initializeAllViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        init.initializeAllViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.folderview_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.shto:
                startActivity(new Intent(this, ShtoStudent.class));
                break;
            case R.id.deleteAll:
                MaterialStyledDialog dialog = new MaterialStyledDialog(this);
                dialog.setTitle("Kujdes!");
                dialog.setDescription("Jeni i sigurt që doni të fshini të gjithë nxënësit? ");
                dialog.setIcon(R.drawable.warning);
                dialog.withDialogAnimation(true, Duration.FAST);
                dialog.setHeaderColor(R.color.warning);
                dialog.setCancelable(false);
                dialog.setPositive("Po", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(Mungesa.this);
                        // init.deselectAllStudents();
                        dbHelper.deleteAllStudents();
                        finish();
                        startActivity(new Intent(Mungesa.this, Mungesa.class));
                    }
                });
                dialog.setNegative("Jo", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // check if the dialogbox is showing or not
        init.exitAppCondition();
    }

}
