/**
 * 
 */
package com.example.android.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.R;

/**
 * @author calvin
 * @see http://stackoverflow.com/questions/4579885/determine-opencore-or-stagefright-framework-for-mediaplayer
 */
public class DetermineStagefright extends Activity
{
    private static final String TAG = DetermineStagefright.class.getName();
    
    private boolean mStreamSeekable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Toast.makeText(this, "SDK version=" + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
        checkStateFright();
        
        if (Build.VERSION.SDK_INT < 8)
        {
            //2.1 or earlier, opencore only, no stream seeking
            mStreamSeekable = false;
        }
        else if (Build.VERSION.SDK_INT == 8)
        { // 2.2, check to see if stagefright enabled
            mStreamSeekable = false;
            //checkStateFright();
        }
        else
        { //greater than 2.2, assume stagefright from here on out
            mStreamSeekable = true;
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    private void checkStateFright()
    {
        try
        {
            FileInputStream buildIs = new FileInputStream(new File("/system/build.prop"));
            if (convertStreamToString(buildIs).contains("media.stagefright.enable-player=true"))
            {
                mStreamSeekable = true;
                Toast.makeText(this, "stagefright enabled", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e)
        { //problem finding build file
            e.printStackTrace();
        }
    }
    
    private static String convertStreamToString(InputStream is)
        throws UnsupportedEncodingException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "", e);
            // Ignore this exception}
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                Log.e(TAG, "", e);
                // Ignore this exception}
            }
        }
        return sb.toString();
    }
    
}
