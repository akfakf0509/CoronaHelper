package com.example.a2020_10_29d;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Diagnosis_record extends LinearLayout {

    public Diagnosis_record(Context context, String email, String result){
        super(context);

        init(context);

        ((TextView)findViewById(R.id.email)).setText(email);
        ((TextView)findViewById(R.id.result)).setText(result);
    }

    public Diagnosis_record(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.diagnosis_record, this, true);
    }
}
