package com.lemon.player.ui.dialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.billingclient.api.BillingClient;
import com.lemon.player.LemonApplication;
import com.lemon.player.R;
import com.lemon.player.billing.BillingManager;
import com.lemon.player.constants.Config;
import com.lemon.player.ui.activities.BaseActivity;
import com.lemon.player.ui.activities.MainActivity;
import com.lemon.player.utils.LemonUtils;

public class UpgradeDialog {

    private static final String TAG = "UpgradeDialog";

    private UpgradeDialog() {
        //no instance
    }

    public static MaterialDialog getUpgradeDialog(@NonNull Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(activity.getResources().getString(R.string.get_pro_title))
                .content(activity.getResources().getString(R.string.upgrade_dialog_message))
                .positiveText(R.string.btn_upgrade)
                .onPositive((dialog, which) -> {
                    if (LemonUtils.isAmazonBuild()) {
                        Intent storeIntent = LemonUtils.getLemonStoreIntent("com.lemon.player");
                        if (storeIntent.resolveActivity(LemonApplication.getInstance().getPackageManager()) != null) {
                            activity.startActivity(storeIntent);
                        } else {
                            activity.startActivity(LemonUtils.getLemonWebIntent("com.lemon.player"));
                        }
                    } else {
                        purchaseUpgrade(activity);
                    }
                })
                .negativeText(R.string.get_pro_button_no)
                .build();
    }

    private static void purchaseUpgrade(@NonNull Activity activity) {
        if (!(activity instanceof BaseActivity)) {
            Log.e(TAG, "Purchase may only be initiated with a BaseActivity");
            return;
        }
        BillingManager billingManager = ((BaseActivity) activity).getBillingManager();
        if (billingManager != null) {
            billingManager.initiatePurchaseFlow(Config.SKU_PREMIUM, BillingClient.SkuType.INAPP);
        }
    }

    public static MaterialDialog getUpgradeSuccessDialog(@NonNull Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(activity.getResources().getString(R.string.upgraded_title))
                .content(activity.getResources().getString(R.string.upgraded_message))
                .positiveText(R.string.restart_button)
                .onPositive((materialDialog, dialogAction) -> {
                    Intent intent = new Intent(activity, MainActivity.class);
                    ComponentName componentName = intent.getComponent();
                    Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                    activity.startActivity(mainIntent);
                })
                .build();
    }
}