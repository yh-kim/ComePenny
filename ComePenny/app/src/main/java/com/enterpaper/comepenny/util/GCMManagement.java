package com.enterpaper.comepenny.util;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Kim on 2015-09-28.
 */
public class GCMManagement {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // 구글 플레이 서비스를 이용할 수 있는지 확인
    public static boolean checkPlayServices(Activity at) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(at);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, at,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("ICELANCER", "This device is not supported.");
                at.finish();
            }
            return false;
        }
        return true;
    }
}
