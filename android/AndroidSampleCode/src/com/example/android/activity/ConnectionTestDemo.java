/**
 * 
 */

package com.example.android.activity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @author calvin
 */
public class ConnectionTestDemo extends Activity {

    private static final String TAG = ConnectionTestDemo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mTelephonyMgr.listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

        getLocalIpAddress();

        Log.d(TAG, "getWifiIpAddress:" + getWifiIpAddress());
    }

    private String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = Formatter.formatIpAddress(ipAddress);
        return ip;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        Log.i(TAG, "Address:" + inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        private long mStart;

        @Override
        public void onDataConnectionStateChanged(int state) {
            if (state == TelephonyManager.DATA_CONNECTING) {
                Log.d(TAG, "Start connecting data service...");
                mStart = System.currentTimeMillis();
            } else if (state == TelephonyManager.DATA_CONNECTED) {
                Log.d(TAG,
                        "Connect data service finished!Elapsed time="
                                + (System.currentTimeMillis() - mStart) / 1000.0);
            } else if (state == TelephonyManager.DATA_DISCONNECTED) {
                Log.w(TAG, "Data connection disconnected!");
            } else if (state == TelephonyManager.DATA_SUSPENDED) {
                Log.w(TAG, "Data connection suspended!");
            }
            super.onDataConnectionStateChanged(state);
        }

    }

    private ConnectivityManager mConnectivityMgr;

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkInfo net = mConnectivityMgr.getActiveNetworkInfo();

                boolean connected = true;
                if (net == null || !net.isConnected()) {
                    connected = false;
                }

                if (!connected) {

                }
            }

        }
    };

}
