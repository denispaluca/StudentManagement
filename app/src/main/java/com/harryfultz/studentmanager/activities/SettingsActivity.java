package com.harryfultz.studentmanager.activities;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.appConfigAndDB.MessageStorage;


public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceChangeListener {

    private Preference parentsPref, absencePref;
    private MessageStorage mS;


    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        addPreferencesFromResource(R.xml.pref_general);
        mS = new MessageStorage(getActivity());
        initializePreferences();
    }

    private void initializePreferences() {
        parentsPref = findPreference("parentsKey");
        absencePref = findPreference("mungesaKey");

        parentsPref.setOnPreferenceChangeListener(this);
        absencePref.setOnPreferenceChangeListener(this);

        setMessages();
    }

    private void setMessages() {
        mS.setAbsenceMessage(PreferenceManager
                .getDefaultSharedPreferences(absencePref.getContext())
                .getString(absencePref.getKey(), ""));

        mS.setParentsMessage(PreferenceManager
                .getDefaultSharedPreferences(parentsPref.getContext())
                .getString(parentsPref.getKey(), ""));
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(preference.getKey(), newValue.toString());

        setMessages();
        return true;
    }

    private Activity getActivity() {
        return this;
    }

}
