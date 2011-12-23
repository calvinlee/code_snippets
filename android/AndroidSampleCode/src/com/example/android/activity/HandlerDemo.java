package com.example.android.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HandlerDemo extends Activity
{
    private static final String TAG = "HandlerDemo";
    
    private static final int INTERVAL = 5000;
    
    private static final int MSG_WHACH_TASK = 3;
    
    private final Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    try
                    {
                        Log.d(TAG, "Processing blocking message,Thread "
                                + Thread.currentThread().getId()
                                + " will sleep for 5 secs...");
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Log.d(TAG, "Delayed messaged has been processed!");
                    break;
                case MSG_WHACH_TASK:
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    
                    List<ActivityManager.RunningTaskInfo> rs = activityManager.getRunningTasks(50);
                    
                    for (int i = 0; i < rs.size(); i++)
                    {
                        Log.w(TAG,
                                "*************************************************");
                        ActivityManager.RunningTaskInfo runningTaskInfo = rs.get(i);
                        Log.i(TAG, "Id=" + runningTaskInfo.id
                                + "\nnumActivities "
                                + runningTaskInfo.numActivities
                                + "\nnumRunning " + runningTaskInfo.numRunning
                                + "\nLaunched com:"
                                + runningTaskInfo.baseActivity
                                + "\ntopActivity" + runningTaskInfo.topActivity
                                + "\ndescribeContents:"
                                + runningTaskInfo.describeContents());
                    }
                    Log.e(TAG, "################DONE######################");
                    sendMessageDelayed(obtainMessage(MSG_WHACH_TASK), INTERVAL);
                    break;
                default:
                    break;
                
            }
            super.handleMessage(msg);
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mHandler.sendEmptyMessage(MSG_WHACH_TASK);
        //        mHandler.sendEmptyMessage(1);
        //        Log.d(TAG,
        //                "Blocked message has been sent,it will block the current thread.");
        //        
        //        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 2000);
        //        Log.d(TAG,
        //                "Delayed message has been sent,it should be processed after 2 secs.");
        super.onCreate(savedInstanceState);
    }
    
}
