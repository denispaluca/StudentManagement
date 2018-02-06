package com.harryfultz.studentmanager.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.appConfigAndDB.AirplaneModeKt;
import com.harryfultz.studentmanager.appConfigAndDB.DatabaseHelper;
import com.harryfultz.studentmanager.appConfigAndDB.StatusBarColor;

import java.util.ArrayList;

public class SpecialComplain extends AppCompatActivity implements View.OnClickListener {

    private EditText complain;
    private AutoCompleteTextView studentName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_complain_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);
        StatusBarColor.Companion.changeStatusBarColor(this);
        initializeViews();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void initializeViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.complainFab);
        if (fab != null) {
            fab.setOnClickListener(this);
        }

        studentName = (AutoCompleteTextView) findViewById(R.id.studentNameEditTextId);
        complain = (EditText) findViewById(R.id.specialComplainEditTextId);

        String names[] = dbHelper.getAllStudents();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        studentName.setAdapter(adapter);
        studentName.setThreshold(2);

    }

    @Override
    public void onClick(View v) {
        // Dergo ankesen
        sendMessge();

    }

    private void sendMessge() {

        if (!AirplaneModeKt.AirplaneMode(this)) {

            String studenti = studentName.getText().toString().trim(); // <- Studenti i zgjedhur
            String ankesa = complain.getText().toString();             // <- ankesa e mësueses

            if (dbHelper.checkIfStudentExists(studenti) && !ankesa.isEmpty()) {               // <- Kontrollon nëse studenti ekziston dhe ankesa nuk është bosh
                String numri = dbHelper.getSpecificNumberFromStudent(studenti);  // <- numri i studenti të zgjedhur


                ArrayList<String> parts = SmsManager.getDefault().divideMessage(ankesa);
                try {
                    SmsManager.getDefault().sendMultipartTextMessage(numri, null, parts, null, null);  // <- dërgo mesazhin
                    MyDynamicToast.successMessage(this, "Mesazhi u dërgua.");

                    SmsManager.getDefault().sendMultipartTextMessage(numri, null, parts, null, null);  // <- dërgo mesazhin
                    MyDynamicToast.successMessage(this, "Mesazhi u dërgua.");
                } catch (Exception e) {
                    e.printStackTrace();
                    MyDynamicToast.errorMessage(this, "Mesazhi nuk u dërgua.");
                    Log.d("Error ne dergim: ", "" + e.getMessage());
                }

            } else {
                MyDynamicToast.informationMessage(this, "Nxënësi që zgjodhët nuk ekziston ose ankesa është bosh!");
            }
        } else {
            MyDynamicToast.errorMessage(this, "Çaktivizoni \"Airplane Mode\" në celularë");
        }

    }

}
