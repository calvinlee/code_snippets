
package com.example.android.activity;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RunningTasksPrinter extends Activity {

    private static final String TAG = "RunningTasksPrinter";

    private static final String ROOT_FOLDER = "ETP_EEEDD356_test";

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            printTopActivity();
            sendMessageDelayed(obtainMessage(), 1000);
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        long start = System.currentTimeMillis();
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + ROOT_FOLDER);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        long end = System.currentTimeMillis();
        Log.d(TAG, "finished,elapsed time is " + (end - start));
        mHandler.sendMessage(mHandler.obtainMessage());
        super.onCreate(savedInstanceState);
    }

    private void printTopActivity() {
        // get a list of running processes and iterate through them
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d(TAG, "CURRENT Activity ::" + taskInfo.get(0).topActivity.getPackageName());

        List<ActivityManager.RunningAppProcessInfo> processInfo = am.getRunningAppProcesses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
