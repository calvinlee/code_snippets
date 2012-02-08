
package com.example.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Demonstrated how to use uses-library tag to load libraries dynamically.
 * <p>
 * 1. use dx tool to optimize the original jar file, this is a must otherwise
 * package manager can not load the jar file at boot time<br>
 * dx --dex --output=ical4j-1.0-rc3.jar ical4j-1.0-rc3_orig.jar<br>
 * then push the ical4j-1.0-rc3.jar to /system/framework/
 * </p>
 * <p>
 * 2. add a line in framework/data/platform.xml, like:
 * 
 * <pre>
 *     library name="com.ical4j"
 *           file="/system/framework/ical4j-1.0-rc3.jar"
 * </pre>
 * push it to /system/etc/permissions/platform.xml
 * </p>
 * <p>
 * 3. Use it in your AndroidManifest.xml:
 * 
 * <pre>
 * uses-library android:name="com.ical4j"
 * </pre>
 * 
 * </p>
 * 
 * @author calvin
 */
public class UseLibraryTest extends Activity {

    private static final String TAG = UseLibraryTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Class.forName("net.fortuna.ical4j.model.Calendar");
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "Library not installed!");
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

}
