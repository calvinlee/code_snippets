
package com.example.android.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class SystemPropertyTest extends Activity {

    private static final String TAG = SystemPropertyTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, Uri.parse(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "dddd.img"))
                .toString()).toString());
        getUsingReflection("sys.current.mode");
        getUsingCmd("sys.current.mode");

        Log.d(TAG, "setUsingReflection...");
        setUsingReflection("sys.current.mode", "false");

        Log.d(TAG, "read it again...");
        getUsingCmd("sys.current.mode");

        Log.d(TAG, "setUsingCmd...");
        setUsingCmd("sys.current.mode", "false");

        Log.d(TAG, "read it again...");
        getUsingReflection("sys.current.mode");

        Log.d(TAG,
                "Environment.getExternalStorageState() = " + Environment.getExternalStorageState());
        Log.d(TAG, "hashcode = " + Environment.getExternalStorageState().hashCode());
        Log.d(TAG, "Environment.MEDIA_MOUNTED hashcode = " + Environment.MEDIA_MOUNTED.hashCode());
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Log.d(TAG, "mounted");
        } else {
            Log.d(TAG, "umounted");
        }

        boolean value2 = Environment.MEDIA_MOUNTED == Environment.MEDIA_MOUNTED;
        Log.d(TAG, "value2 = " + value2);
    }

    private String getUsingReflection(String key) {
        String value = "";
        @SuppressWarnings("rawtypes")
        Class clazz;
        try {
            clazz = Class.forName("android.os.SystemProperties");
            // Parameters Types
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[] {
                    String.class
            };

            Method get = clazz.getMethod("get", paramTypes);

            // Parameters
            Object[] params = new Object[] {
                    key
            };

            value = (String) get.invoke(clazz, params);
            Log.d(TAG, "getting system property using reflection,ret=" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private String setUsingReflection(String key, String value) {
        @SuppressWarnings("rawtypes")
        Class clazz;
        try {
            clazz = Class.forName("android.os.SystemProperties");
            // Parameters Types
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[] {
                    String.class, String.class
            };

            Method set = clazz.getMethod("set", paramTypes);

            // Parameters
            Object[] params = new Object[] {
                    key, value
            };

            set.invoke(clazz, params);
            Log.d(TAG, "setting system property using reflection");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private String getUsingCmd(String key) {
        String value = "";
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + key);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((value = input.readLine()) != null) {
                Log.d(TAG, "getting system property using Cmd,ret=" + value);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return value;
    }

    private String setUsingCmd(String key, String value) {
        try {
            Runtime.getRuntime().exec("setprop " + key + " " + value);
        } catch (Exception err) {
            err.printStackTrace();
        }

        return value;
    }
}
