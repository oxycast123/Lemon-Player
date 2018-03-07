package com.lemon.player.lyrics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.lemon.player.LemonApplication;
import com.lemon.player.R;
import com.lemon.player.model.Song;
import com.lemon.player.utils.LemonUtils;
import com.lemon.player.utils.TypefaceManager;

/**
 * QuickLyric helpers
 */
public class QuickLyricUtils {

    private static final String QUICKLYRIC_URL = "https://d3khd.app.goo.gl/jdF1";

    public static boolean isQLInstalled() {

        PackageManager pm = LemonApplication.getInstance().getPackageManager();
        try {
            pm.getPackageInfo("com.geecko.QuickLyric", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    static void getLyricsFor(Context context, Song song) {
        Intent intent = new Intent("com.geecko.QuickLyric.getLyrics");
        intent.putExtra("TAGS", new String[]{song.artistName, song.name});
        if (intent.resolveActivity(LemonApplication.getInstance().getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    static Intent getQuickLyricIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(QUICKLYRIC_URL));
    }

    /**
     * @return true if the Play Store is available, and this QuickLyric can be downloaded.
     */
    static boolean canDownloadQuickLyric() {
        if (LemonUtils.isAmazonBuild()) return false;
        return getQuickLyricIntent().resolveActivity(LemonApplication.getInstance().getPackageManager()) != null;
    }

    /**
     * @return a {@link SpannableStringBuilder}, using the correct font as per the QuickLyric branding.
     */
    static Spannable getSpannedString() {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(LemonApplication.getInstance().getString(R.string.quicklyric)); // "Quick" must use roboto light
        ssBuilder.setSpan(new TypefaceSpan(TypefaceManager.SANS_SERIF_LIGHT), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ssBuilder;
    }
}
