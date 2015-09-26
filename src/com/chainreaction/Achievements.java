package com.chainreaction;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Achievements extends Activity
{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         TextView textView = new TextView(this);
         textView.setText("Achievements");
         setContentView(textView);
    }
}
