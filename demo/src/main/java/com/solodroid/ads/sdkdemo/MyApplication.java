package com.solodroid.ads.sdkdemo;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.solodroid.ads.sdk.format.AppOpenAdManager;
import com.solodroid.ads.sdk.format.AppOpenAdMob;
import com.solodroid.ads.sdk.format.AppOpenMaxManager;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

public class MyApplication extends Application implements ActivityLifecycleCallbacks, LifecycleObserver {

    private static MyApplication mInstance;
    private AppOpenAdMob appOpenAdMob;
    private AppOpenAdManager appOpenAdManager;
    private AppOpenMaxManager appOpenMaxManager;
    Activity currentActivity;

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdMob = new AppOpenAdMob();
        appOpenAdManager = new AppOpenAdManager();
        appOpenMaxManager = new AppOpenMaxManager();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        if (Constant.AD_NETWORK.equals(ADMOB)) {
            appOpenAdMob.showAdIfAvailable(currentActivity, Constant.ADMOB_APP_OPEN_AD_ID);
        } else if (Constant.AD_NETWORK.equals(GOOGLE_AD_MANAGER)) {
            appOpenAdManager.showAdIfAvailable(currentActivity, Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID);
        } else if (Constant.AD_NETWORK.equals(APPLOVIN_MAX)) {
            appOpenMaxManager.showAdIfAvailable(currentActivity, Constant.APPLOVIN_OPEN_AD);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (Constant.AD_NETWORK.equals(ADMOB)) {
            if (!appOpenAdMob.isShowingAd) {
                currentActivity = activity;
            }
        } else if (Constant.AD_NETWORK.equals(GOOGLE_AD_MANAGER)) {
            if (!appOpenAdManager.isShowingAd) {
                currentActivity = activity;
            }
        } else if (Constant.AD_NETWORK.equals(APPLOVIN_MAX)) {
            if (!appOpenMaxManager.isShowingAd) {
                currentActivity = activity;
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {

        if (Constant.AD_NETWORK.equals(ADMOB)) {
            appOpenAdMob.showAdIfAvailable(activity, Constant.ADMOB_APP_OPEN_AD_ID, onShowAdCompleteListener);
        } else if (Constant.AD_NETWORK.equals(GOOGLE_AD_MANAGER)) {
            appOpenAdManager.showAdIfAvailable(activity, Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID, onShowAdCompleteListener);
        } else if (Constant.AD_NETWORK.equals(APPLOVIN_MAX)) {
            appOpenAdManager.showAdIfAvailable(activity, Constant.APPLOVIN_OPEN_AD, onShowAdCompleteListener);
        }
    }

}
