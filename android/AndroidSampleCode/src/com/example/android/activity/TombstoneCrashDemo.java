package com.example.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.R;

public class TombstoneCrashDemo extends Activity
{
    private static final String TAG = "TombstoneCrashDemo";
    static
    {
        System.loadLibrary("tombstonec");
    }
    
    private native int genTombstone(int a, int b);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d(TAG, "Activity call JNI: ");
        genTombstone(1, 2);
    }
}
