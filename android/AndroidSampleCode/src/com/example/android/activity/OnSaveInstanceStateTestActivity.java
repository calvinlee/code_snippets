
package com.example.android.activity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.R;

public class OnSaveInstanceStateTestActivity extends Activity implements OnClickListener {
    private static final String TAG = OnSaveInstanceStateTestActivity.class.getSimpleName();

    private static final String FILE_SAVE = "doc.txt";

    private static final String FILE_SAVE_TMP = "doc.txt.tmp";

    /** Called when the activity is first created. */
    private EditText mText;

    private Button mSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad);
        mText = (EditText) findViewById(R.id.text);
        mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(this);

        if (restoreState()) {
            Toast.makeText(getApplicationContext(), getString(R.string.restore_ok),
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Successfully restored state!");
        } else if (loadFile()) {
            Toast.makeText(getApplicationContext(), getString(R.string.load_ok), Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "Successfully loaded file!");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Saving activity state...");
        writeFile(FILE_SAVE_TMP, mText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        writeFile(FILE_SAVE, mText.getText().toString());
        Toast.makeText(getApplicationContext(), getString(R.string.save_ok), Toast.LENGTH_SHORT)
                .show();
    }

    private boolean loadFile() {
        File file = new File(getFilesDir(), FILE_SAVE);
        if (!file.exists()) {
            return false;
        }

        try {
            String text = readFile(FILE_SAVE);
            mText.setText(text);
        } catch (Exception e) {
            Log.e(TAG, "Load file failed!");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState,savedInstanceState=" + savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private boolean restoreState() {
        File tmpFile = new File(getFilesDir(), FILE_SAVE_TMP);
        if (!tmpFile.exists()) {
            return false;
        }

        try {
            String text = readFile(FILE_SAVE_TMP);
            mText.setText(text);
            tmpFile.delete();
        } catch (Exception e) {
            Log.e(TAG, "Restored state failed!");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void writeFile(String fileName, String content) {
        Log.d(TAG, "writting file,file name is " + fileName + ",content=" + content);
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(content)) {
            return;
        }
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(openFileOutput(fileName, MODE_PRIVATE));
            out.write(content.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readFile(String fileName) throws Exception {
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        try {
            in = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String s = null;
            while ((s = in.readLine()) != null) {
                buffer.append(s);
                buffer.append("\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }

        return buffer.toString();
    }
}
