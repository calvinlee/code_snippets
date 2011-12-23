
package com.example.android.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
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
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        String packageName = getPackageNameWithPid(android.os.Process.myPid());
        Log.d(TAG, "my package name is " + packageName);
        super.onCreate(savedInstanceState);
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

}
