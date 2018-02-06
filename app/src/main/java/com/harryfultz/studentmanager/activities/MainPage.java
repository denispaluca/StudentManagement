package com.harryfultz.studentmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.appConfigAndDB.AirplaneModeKt;
import com.harryfultz.studentmanager.appConfigAndDB.DatabaseHelper;
import com.harryfultz.studentmanager.appConfigAndDB.MessageStorage;
import com.harryfultz.studentmanager.appConfigAndDB.StatusBarColor;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    private MessageStorage mS;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                if (isFirstStart) {
                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                }

                SharedPreferences.Editor editor = getPrefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();

            }
        }).start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StatusBarColor.Companion.changeStatusBarColor(this);

        LinearLayout mungesa = (LinearLayout) findViewById(R.id.mungesatButton);
        LinearLayout mbledhje = (LinearLayout) findViewById(R.id.mbledhjeButton);
        LinearLayout specialComplain = (LinearLayout) findViewById(R.id.specialComplainButton);

        mS = new MessageStorage(this);
        dbHelper = new DatabaseHelper(this);

        mungesa.setOnClickListener(this);
        mbledhje.setOnClickListener(this);
        specialComplain.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.mainpage_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsId:
                startActivity(new Intent(MainPage.this, SettingsActivity.class));
                break;

            case R.id.about:
                startActivity(new Intent(MainPage.this, About.class));
                break;

            case R.id.introId:
                finish();
                startActivity(new Intent(this, IntroActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mungesatButton:
                Log.d("Mungesat", "Execyted");
                startActivity(new Intent(MainPage.this, Mungesa.class));
                break;
            case R.id.mbledhjeButton:

                MaterialStyledDialog dialog = new MaterialStyledDialog(this);
                dialog.setTitle("Kujdes!");
                dialog.setDescription("Jeni i sigurt që doni t'i dërgoni prindërve mesazhin për mbledhje ?");
                dialog.setIcon(R.drawable.warning);
                dialog.withDialogAnimation(true, Duration.FAST);
                dialog.setHeaderColor(R.color.warning);
                dialog.setCancelable(false);
                dialog.setPositive("Po", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendParentsGatheringMessage();

                    }
                });
                dialog.setNegative("Jo", null);
                dialog.show();

                break;
            case R.id.specialComplainButton:
                startActivity(new Intent(MainPage.this, SpecialComplain.class));
                break;
        }
    }

    private void sendParentsGatheringMessage() {
        if(!AirplaneModeKt.AirplaneMode(this)){
            String message = mS.getParentsMessage();
            ArrayList<String> parts = SmsManager.getDefault().divideMessage(message);

            if (dbHelper.getNumberOfRows() == 0) {
                MyDynamicToast.informationMessage(this, "Nuk keni regjistruar asnjë nxënës.");
            } else {
                for (int i = 0; i < dbHelper.getNumberOfRows(); i++) {
                    try {
                        SmsManager.getDefault().sendMultipartTextMessage(dbHelper.getAllNumbers()[i], null, parts, null, null);
                        MyDynamicToast.successMessage(this, "Mesazhet u dërguan.");

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        MyDynamicToast.warningMessage(this, "Mesazhi i mbledhjeve me prindër është bosh");
                    }

                }
            }
        }else {
            MyDynamicToast.errorMessage(this, "Çaktivizoni \"Airplane Mode\" në celularë");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
