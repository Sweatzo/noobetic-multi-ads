package com.solodroid.ads.sdk.format;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

import java.util.Date;

public class AppOpenMaxManager {

    private static final String TAG = "MaxOpenAd";

    private MaxAppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AppOpenMaxManager() {
        Log.d(TAG, "AppOpenMaxManager: new openapp");
    }

    private void loadOpenAd(Context context, String ad_unit, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        Log.d(TAG, "loadOpenAd: start loading");

        isLoadingAd = true;

        appOpenAd = new MaxAppOpenAd(ad_unit, context);
        appOpenAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                isLoadingAd = false;
                Log.d(TAG, "onAdLoaded: is loaded");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                appOpenAd = null;
                isShowingAd = false;
                appOpenAd.loadAd();
                onShowAdCompleteListener.onShowAdComplete();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d(TAG, "onAdLoadFailed: " + error.getCode() + error.getMessage() );
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                appOpenAd = null;
                isShowingAd = false;
                appOpenAd.loadAd();
                onShowAdCompleteListener.onShowAdComplete();
            }
        });
        appOpenAd.loadAd();
    }

    public void showAdIfAvailable(Context context, String ad_unit) {
        showAdIfAvailable(context, ad_unit, () -> {

        });
    }

    public void showAdIfAvailable(Context context, String ad_unit, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isShowingAd) {
            Log.d(TAG, "showAdIfAvailable: is showing");
            return;
        }

        if (!isAdAvailable()) {
            loadOpenAd(context, ad_unit, onShowAdCompleteListener);
            onShowAdCompleteListener.onShowAdComplete();
            return;
        }

        Log.d(TAG, "showAdIfAvailable: ad will show");

        isShowingAd = true;
        appOpenAd.showAd();
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }
}
