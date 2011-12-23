
package com.example.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.R;

public class WebviewTestActivity extends Activity {
    private static final String TAG = "WebviewTestActivity";

    private long mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        // Log.d(TAG, "onCreate");
        final WebView wb = (WebView) findViewById(R.id.webview);
        wb.setWebViewClient(new WebViewClient() {
            // @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mStart = System.currentTimeMillis();
                Log.e(TAG, "start=" + mStart);
                super.onPageStarted(view, url, favicon);
            }

        });

        wb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    Log.d(TAG, "start=" + mStart + ",now=" + System.currentTimeMillis()
                            + ",elapsed=" + (System.currentTimeMillis() - mStart) / 1000.0);
                    mStart = 0;
                    Picture capture = wb.capturePicture();
                    File capPic = new File(Environment.getExternalStorageDirectory(), "pageCap.png");
                    try {
                        if (!capPic.exists()) {
                            capPic.createNewFile();
                        }
                        OutputStream os = new FileOutputStream(capPic);
                        // pic.writeToStream(os);
                        // os.flush();
                        // os.close();

                        int width = capture.getWidth();
                        int height = capture.getHeight();
                        Log.d(TAG, "capture pic width = " + width + ",height=" + height);
                        if (width == 0) {
                            Log.d(TAG, "wield,width=0 ");
                            width = 480;
                        }
                        if (height == 0) {
                            Log.d(TAG, "wield,height=0 ");
                            height = 800;
                        }
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bmp);
                        capture.draw(canvas);

                        bmp.compress(Bitmap.CompressFormat.PNG, 90, os);
                        os.close();

                        // ImageView m = new ImageView(AndroidDemos.this);
                        // m.setDrawingCacheEnabled(true);
                        // m.setImageDrawable(new PictureDrawable(pic));
                        //
                        // view.buildDrawingCache();
                        // Bitmap bitmap = view.getDrawingCache();
                        // bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
                        // os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                super.onProgressChanged(view, newProgress);
            }

        });

        //
        wb.loadUrl("http://www.aol.com");
    }

}
