package com.harryfultz.studentmanager.customAdapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.harryfultz.studentmanager.models.CheckBoxModel;
import com.harryfultz.studentmanager.R;
import com.hanks.library.AnimateCheckBox;


public class CheckBoxCustomAdapter extends ArrayAdapter<CheckBoxModel> {

    public AnimateCheckBox checkBox;
    private Context c;
    private CheckBoxModel[] cbM;
    private ViewGroup parent;

    public CheckBoxCustomAdapter(Context c, CheckBoxModel[] resource) {
        super(c, R.layout.checkboxitemlayout, resource);
        this.c = c;
        this.cbM = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.parent = parent;
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        convertView = inflater.inflate(R.layout.checkboxitemlayout, parent, false);
        checkBox = (AnimateCheckBox) convertView.findViewById(R.id.checkBoxButtonId);
        TextView textView = (TextView) convertView.findViewById(R.id.checBoxTextId);
        textView.setText(cbM[position].getItemName());

        if (cbM[position].getValue() == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        return convertView;
    }

}
