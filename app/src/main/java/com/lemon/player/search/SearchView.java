package com.lemon.player.search;

import android.support.annotation.NonNull;
import android.view.View;

import com.lemon.player.model.Album;
import com.lemon.player.model.AlbumArtist;
import com.lemon.player.tagger.TaggerDialog;
import com.lemon.player.ui.dialog.DeleteDialog;
import com.simplecityapps.recycler_adapter.model.ViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface SearchView {

    void setLoading(boolean loading);

    void setEmpty(boolean empty);

    Disposable setItems(@NonNull List<ViewModel> items);

    void setFilterFuzzyChecked(boolean checked);

    void setFilterArtistsChecked(boolean checked);

    void setFilterAlbumsChecked(boolean checked);

    void showToast(String message);

    void showTaggerDialog(@NonNull TaggerDialog taggerDialog);

    void showDeleteDialog(@NonNull DeleteDialog deleteDialog);

    void goToArtist(AlbumArtist albumArtist, View transitionView);

    void goToAlbum(Album album, View transitionView);

    void showUpgradeDialog();
}