package project.se.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import info.hoang8f.android.segmented.SegmentedGroup;
import project.se.talktodeaf.R;

public class Setting extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SegmentedGroup segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented2.setTintColor(Color.DKGRAY);
    }

}
