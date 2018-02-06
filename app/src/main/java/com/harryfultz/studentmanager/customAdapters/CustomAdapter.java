package com.harryfultz.studentmanager.customAdapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.harryfultz.studentmanager.models.Model;
import com.harryfultz.studentmanager.R;

public class CustomAdapter extends ArrayAdapter<Model> {
    public CheckedTextView cb;
    private Model[] modelItems = null;
    private Context context;


    public CustomAdapter(Context c, Model[] resource) {
        super(c, R.layout.list_item, resource);

        this.context = c;
        this.modelItems = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_item, parent, false);

        cb = (CheckedTextView) convertView.findViewById(R.id.checkBox1);
        TextView pN = (TextView) convertView.findViewById(R.id.numberId);
        ImageView prfImage = (ImageView) convertView.findViewById(R.id.defaultPic);

        if(modelItems[position] != null){
            if (pN != null)
                pN.setText(modelItems[position].getPhoneNumber());

            cb.setText(modelItems[position].getName());

            if (modelItems[position].getGender() == 1) {
                prfImage.setImageResource(R.drawable.maledefaultpic);
            } else {
                prfImage.setImageResource(R.drawable.femaledefaultpic);
            }

            if (modelItems[position].getValue() == 1) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
        }



        return convertView;
    }


}
