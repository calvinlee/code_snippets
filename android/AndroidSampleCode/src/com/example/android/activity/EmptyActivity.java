
package com.example.android.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * This activity does nothing.It used as a stub when user touch one of the
 * notifications as we don't want to anything else but to disappear it.
 */
public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);
    }

}
