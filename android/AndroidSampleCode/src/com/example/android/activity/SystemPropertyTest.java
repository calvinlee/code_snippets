
package com.example.android.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SystemPropertyTest extends Activity {

    private static final String TAG = SystemPropertyTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
