package com.lemon.player.ui.settings;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.lemon.player.LemonApplication;
import com.lemon.player.ui.presenters.Presenter;
import com.lemon.player.utils.SettingsManager;
import com.lemon.player.utils.LemonUtils;

import javax.inject.Inject;

public class SupportPresenter extends Presenter<SupportView> {

    @Inject
    public SupportPresenter() {

    }

    @Override
    public void bindView(@NonNull SupportView view) {
        super.bindView(view);

        setAppVersion();
    }

    private void setAppVersion() {
        SupportView supportView = getView();
        if (supportView != null) {
            supportView.setVersion("Lemon Music Player 1.5" );
        }
    }

   /* public void faqClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        SupportView supportView = getView();
        if (supportView != null) {
            supportView.showFaq(intent);
        }
    }
*/
  /*  public void helpClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/communities/112365043563095486408"));
        SupportView supportView = getView();
        if (supportView != null) {
            supportView.showHelp(intent);
        }
    }*/

    public void rateClicked() {

        SettingsManager.getInstance().setHasRated();

        SupportView supportView = getView();
        if (supportView != null) {

            Intent intent = LemonUtils.getLemonStoreIntent(LemonApplication.getInstance().getPackageName());
            supportView.showRate(intent);
        }
    }
}