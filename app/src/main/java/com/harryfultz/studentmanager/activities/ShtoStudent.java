package com.harryfultz.studentmanager.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.appConfigAndDB.DatabaseHelper;
import com.harryfultz.studentmanager.appConfigAndDB.StatusBarColor;

public class ShtoStudent extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    DatabaseHelper dbHelper;
    private EditText username, phone;
    private int selected = -1;
    private RadioGroup genderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shto_student_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarColor.Companion.changeStatusBarColor(this);

        dbHelper = new DatabaseHelper(getApplicationContext());

        genderGroup = (RadioGroup) findViewById(R.id.radioSex);
        if (genderGroup != null)
            genderGroup.setOnCheckedChangeListener(this);

        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
    }

    public void addStudent(View view) {
        hideKeyboard();
        String name = username.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        if (name.length() != 0 || phoneNumber.length() != 0 || selected != -1) {
            if (!dbHelper.checkIfStudentExists(name)) {
                dbHelper.addStudent(name, phoneNumber, selected);
                finish();
                onBackPressed();
            } else {
                MyDynamicToast.errorMessage(this, "Studenti ekziston në listë!");
                username.setText("");
                phone.setText("");
                genderGroup.clearCheck();
            }

        } else {
            MyDynamicToast.warningMessage(getApplicationContext(), "Plotësoni të gjitha fushat!");
        }
    }

    public void addAnotherStudent(View view) {
        String name = username.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();

        if (name.length() != 0 || phoneNumber.length() != 0 || selected != -1) {
            dbHelper.addStudent(name, phoneNumber, selected);
            username.setText("");
            phone.setText("");
            genderGroup.clearCheck();
        } else {
            MyDynamicToast.warningMessage(getApplicationContext(), "Plotësoni të gjitha fushat!");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioMale:
                selected = 1;
                break;
            case R.id.radioFemale:
                selected = 0;
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
