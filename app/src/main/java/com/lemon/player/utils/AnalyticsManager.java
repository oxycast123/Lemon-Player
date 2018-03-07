package com.lemon.player.utils;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lemon.player.BuildConfig;
import com.lemon.player.LemonApplication;

public class AnalyticsManager {

    private static final String TAG = "AnalyticsManager";

    private static boolean analyticsEnabled() {
        return !BuildConfig.DEBUG;
    }

    public @interface UpgradeType {
        String NAG = "Nag";
        String FOLDER = "Folder";
        String UPGRADE = "Upgrade";
    }

    public static void logChangelogViewed() {
        if (!analyticsEnabled()) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "changelog");
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");

   /*     FirebaseAnalytics.getInstance(LemonApplication.getInstance())
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/
       // Answers.getInstance().logCustom(new CustomEvent("Changelog Viewed"));
    }

    public static void logUpgrade(@UpgradeType String upgradeType) {
        if (!analyticsEnabled()) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY, 0);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, upgradeType);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "upgrade");

        FirebaseAnalytics.getInstance(LemonApplication.getInstance())
                .logEvent(FirebaseAnalytics.Event.PRESENT_OFFER, bundle);
    }

    public static void logScreenName(Activity activity, String name) {
        if (!analyticsEnabled()) {
            return;
        }

     /*   CrashlyticsCore.getInstance().log(String.format("Screen: %s", name));
        FirebaseAnalytics.getInstance(LemonApplication.getInstance()).setCurrentScreen(activity, name, null);*/
    }

    public static void setIsUpgraded() {
        if (!analyticsEnabled()) {
            return;
        }

        FirebaseAnalytics.getInstance(LemonApplication.getInstance()).setUserProperty("Upgraded", String.valueOf(LemonUtils.isUpgraded()));
    }

    public static void logInitialTheme(ThemeUtils.Theme theme) {
        if (!analyticsEnabled()) {
            return;
        }

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(theme.id));
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, String.format("%s-%s-%s", theme.primaryColorName, theme.accentColorName, theme.isDark));
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "themes");
        FirebaseAnalytics.getInstance(LemonApplication.getInstance()).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }

    public static void logRateShown() {
        if (!analyticsEnabled()) {
            return;
        }

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "show_rate_snackbar");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "show_rate_snackbar");
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "rate_app");

        FirebaseAnalytics.getInstance(LemonApplication.getInstance())
                .logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }

    public static void logRateClicked() {
        if (!analyticsEnabled()) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "rate_snackbar");
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");

        FirebaseAnalytics.getInstance(LemonApplication.getInstance())
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void didSnow(){
        if (!analyticsEnabled()) {
            return;
        }

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "show_snow");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "show_snow");
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "easter_eggs");

        FirebaseAnalytics.getInstance(LemonApplication.getInstance())
                .logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);

    }
}