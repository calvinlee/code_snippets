
package com.example.android.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class PermissionGidsTest extends Activity {

    private static final String TAG = PermissionGidsTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PackageManager pm = this.getPackageManager();
        try {
            // If you uses a specific permission defined in
            // /system/etc/permission/platform.xml,the corresponding gid of this
            // permission will be add to the gids
            // These gid is hard-coded in
            // system/core/include/private/android_filesystem_config.h
            int[] gids = pm.getPackageInfo(getPackageName(), PackageManager.GET_GIDS).gids;
            Log.d(TAG, "gids=" + Arrays.toString(gids));

            PermissionInfo[] pers = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).permissions;
            Log.d(TAG, "perms=" + pers);

            getServicesInfo(getPackageName());

            List<String> supportedTypes = new ArrayList<String>();
            final String COLUMN_NAME = "supported";
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(
                        Uri.parse("content://com.example.provider/mime/"), new String[] {
                            COLUMN_NAME
                        }, null, null, null);
                cursor.move(-1);
                while (cursor.moveToNext()) {
                    String mimeType = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    supportedTypes.add(mimeType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        String encodeHtml = TextUtils.htmlEncode("Hello world");
        Log.d(TAG, encodeHtml);
        // String packageName =
        // getPackageNameWithPid(android.os.Process.myPid());
        // Log.d(TAG, "my package name is " + packageName);
        super.onCreate(savedInstanceState);
    }

    private void getServicesInfo(String packageName) {
        // IBinder b = ServiceManager.getService("package");
        // IPackageManager pm = IPackageManager.Stub.asInterface(b);
        PackageManager pm = this.getPackageManager();
        ServiceInfo[] services;
        try {
            services = pm.getPackageInfo(packageName, PackageManager.GET_SERVICES).services;
            if (services == null) {
                return;
            }
            for (ServiceInfo service : services) {
                ComponentName cm = new ComponentName(service.packageName, service.name);
                Log.d(TAG, "serviceinfo component name:" + cm);
            }
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getPackageNameWithPid(int pid) {
        Log.d(TAG, "getting package name of pid: " + pid);
        StringBuffer pkgName = new StringBuffer();
        java.lang.Process process = null;
        try {
            String[] args = {
                    "/system/bin/cat", "/proc/" + pid + "/cmdline"
            };
            ProcessBuilder builder = new ProcessBuilder(args);
            builder.directory(new File("/system/bin"));
            builder.redirectErrorStream(true);
            process = builder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "appending line: " + line);
                pkgName.append(line);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        return pkgName.toString();
    }

    private int executeCommand(String cmd) {
        int returnValue = -1;
        if (TextUtils.isEmpty(cmd)) {
            return returnValue;
        }

        StringBuffer cmdOutput = new StringBuffer();
        Log.d(TAG, "executing command: " + cmd);
        try {
            String value = "";

            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            while ((value = input.readLine()) != null) {
                cmdOutput.append(value);
                cmdOutput.append("\n");
            }

            returnValue = process.waitFor();
            Log.d(TAG, "command process exit value: " + returnValue);
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        if (!TextUtils.isEmpty(cmdOutput)) {
            Log.d(TAG, "Command output: " + cmdOutput);
        }

        return returnValue;
    }

}
