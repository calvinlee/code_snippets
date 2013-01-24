
package com.example.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.activity.ConnectionTestDemo;
import com.example.android.activity.DetermineStagefright;
import com.example.android.activity.HandlerDemo;
import com.example.android.activity.LifeCyclePrinter;
import com.example.android.activity.MediaOnlineStreamDemo;
import com.example.android.activity.OnSaveInstanceStateTestActivity;
import com.example.android.activity.PermissionGidsTest;
import com.example.android.activity.ProgressNotificationDemo;
import com.example.android.activity.RunningTasksPrinter;
import com.example.android.activity.SystemPropertyTest;
import com.example.android.activity.TombstoneCrashDemo;
import com.example.android.activity.UseLibraryTest;
import com.example.android.activity.WebviewTestActivity;

public class AndroidMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(this, LifeCyclePrinter.class));
//        startActivity(new Intent(this, DetermineStagefright.class));
//        startActivity(new Intent(this, HandlerDemo.class));
//
//        startActivity(new Intent(this, MediaOnlineStreamDemo.class));
//        startActivity(new Intent(this, OnSaveInstanceStateTestActivity.class));
//        startActivity(new Intent(this, PermissionGidsTest.class));
//        startActivity(new Intent(this, RunningTasksPrinter.class));
//        startActivity(new Intent(this, SystemPropertyTest.class));
//        startActivity(new Intent(this, TombstoneCrashDemo.class));
//        startActivity(new Intent(this, WebviewTestActivity.class));
//        startActivity(new Intent(this, ConnectionTestDemo.class));

//        startActivity(new Intent(this, ProgressNotificationDemo.class));
        
        startActivity(new Intent(this, RunningTasksPrinter.class));
        finish();
    }
}
