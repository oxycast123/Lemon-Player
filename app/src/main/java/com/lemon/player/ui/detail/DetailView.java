package com.lemon.player.ui.detail;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lemon.player.tagger.TaggerDialog;
import com.simplecityapps.recycler_adapter.model.ViewModel;

import java.util.List;

interface DetailView {

    void itemsLoaded(List<ViewModel> items);

    void setEmpty(boolean empty);

    void showToast(String message);

    void showTaggerDialog(TaggerDialog taggerDialog);

    void showArtworkDialog(MaterialDialog artworkDialog);

    void showInfoDialog(MaterialDialog infoDialog);

    void showUpgradeDialog(MaterialDialog upgradeDialog);
}
