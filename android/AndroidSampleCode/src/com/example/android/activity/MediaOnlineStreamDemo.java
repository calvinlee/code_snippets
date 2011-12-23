/**
 * 
 */

package com.example.android.activity;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;

import com.example.android.R;

/**
 * @author calvin
 */
public class MediaOnlineStreamDemo extends Activity implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = "MediaOnlineStreamDemo";

    private MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);

        try {
            mMediaPlayer.setDataSource("http://www.wwa3.com/lj/wwa3/xiao1/beatit.mp3");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.prepareAsync();
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i(TAG, "onBufferingUpdate == " + percent);
    }

    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion");

    }

    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared");
        mMediaPlayer.start();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError what=" + what + " extra=" + extra);
        return false;
    }

    public void onSeekComplete(MediaPlayer mp) {
        // TODO Auto-generated method stub

    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.release();
        super.onDestroy();
    }

}
