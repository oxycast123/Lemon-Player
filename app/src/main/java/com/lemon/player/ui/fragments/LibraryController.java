package com.lemon.player.ui.fragments;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.ViewBackgroundAction;
import com.annimon.stream.Stream;
import com.cantrowitz.rxbroadcast.RxBroadcast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.lemon.player.R;
import com.lemon.player.LemonApplication;
import com.lemon.player.model.Album;
import com.lemon.player.model.AlbumArtist;
import com.lemon.player.model.CategoryItem;
import com.lemon.player.model.Genre;
import com.lemon.player.model.Playlist;
import com.lemon.player.search.SearchFragment;
import com.lemon.player.ui.activities.ToolbarListener;
import com.lemon.player.ui.adapters.PagerAdapter;
import com.lemon.player.ui.detail.AlbumDetailFragment;
import com.lemon.player.ui.detail.ArtistDetailFragment;
import com.lemon.player.ui.detail.BaseDetailFragment;
import com.lemon.player.ui.detail.GenreDetailFragment;
import com.lemon.player.ui.detail.PlaylistDetailFragment;
import com.lemon.player.ui.drawer.NavigationEventRelay;
import com.lemon.player.ui.views.ContextualToolbar;
import com.lemon.player.ui.views.ContextualToolbarHost;
import com.lemon.player.ui.views.multisheet.MultiSheetEventRelay;
import com.lemon.player.utils.DialogUtils;
import com.lemon.player.utils.MusicUtils;
import com.lemon.player.utils.SettingsManager;
import com.lemon.multisheetview.ui.view.MultiSheetView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import test.com.androidnavigation.fragment.FragmentInfo;

import static com.afollestad.aesthetic.Rx.distinctToMainThread;
import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;


public class LibraryController extends BaseFragment implements
        AlbumArtistFragment.AlbumArtistClickListener,
        AlbumFragment.AlbumClickListener,
        SuggestedFragment.SuggestedClickListener,
        PlaylistFragment.PlaylistClickListener,
        GenreFragment.GenreClickListener,
        ContextualToolbarHost {

    private static final String TAG = "LibraryController";

    public static final String EVENT_TABS_CHANGED = "tabs_changed";

    @BindView(R.id.tabs)
    TabLayout slidingTabLayout;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.contextualToolbar)
    ContextualToolbar contextualToolbar;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.adViewlibrary)
    AdView adViewlibrary;

    @Inject
    NavigationEventRelay navigationEventRelay;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Disposable tabChangedDisposable;

    private Unbinder unbinder;

    private boolean refreshPagerAdapter = false;

    private PagerAdapter pagerAdapter;

    public static FragmentInfo fragmentInfo() {
        return new FragmentInfo(LibraryController.class, null, "LibraryController");
    }

    public LibraryController() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LemonApplication.getInstance().getAppComponent()
                .inject(this);

        setHasOptionsMenu(true);

        tabChangedDisposable = RxBroadcast.fromLocalBroadcast(getContext(), new IntentFilter(EVENT_TABS_CHANGED)).subscribe(onNext -> refreshPagerAdapter = true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setupViewPager();

        Aesthetic.get(getContext())
                .colorPrimary()
                .take(1)
                .subscribe(color -> ViewBackgroundAction.create(appBarLayout)
                        .accept(color), onErrorLogAndRethrow());

        compositeDisposable.add(Aesthetic.get(getContext())
                .colorPrimary()
                .compose(distinctToMainThread())
                .subscribe(color -> ViewBackgroundAction.create(appBarLayout)
                        .accept(color), onErrorLogAndRethrow()));
        MobileAds.initialize(getActivity(), String.valueOf(R.string.ad_unit_id));
        adViewlibrary = rootView.findViewById(R.id.adViewlibrary);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewlibrary.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof ToolbarListener) {
            ((ToolbarListener) getActivity()).toolbarAttached(view.findViewById(R.id.toolbar));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!MusicUtils.getQueue().isEmpty()) {
            multiSheetEventRelay.sendEvent(new MultiSheetEventRelay.MultiSheetEvent(MultiSheetEventRelay.MultiSheetEvent.Action.SHOW_IF_HIDDEN, MultiSheetView.Sheet.NONE));
        }

        navigationEventRelay.sendEvent(new NavigationEventRelay.NavigationEvent(NavigationEventRelay.NavigationEvent.Type.LIBRARY_SELECTED, null, false));
    }

    @Override
    public void onDestroyView() {
        pager.setAdapter(null);
        compositeDisposable.clear();
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        tabChangedDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_library, menu);
        setupCastMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
        }
        return false;
    }

    private void setupViewPager() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        CategoryItem.getCategoryItems(sharedPreferences);

        if (pagerAdapter != null && refreshPagerAdapter) {
            pagerAdapter.removeAllChildFragments();
            refreshPagerAdapter = false;
            pager.setAdapter(null);
        }

        int defaultPage = 1;

        pagerAdapter = new PagerAdapter(getChildFragmentManager());
        List<CategoryItem> categoryItems = Stream.of(CategoryItem.getCategoryItems(sharedPreferences))
                .filter(categoryItem -> categoryItem.isChecked)
                .toList();

        int defaultPageType = SettingsManager.getInstance().getDefaultPageType();
        for (int i = 0; i < categoryItems.size(); i++) {
            CategoryItem categoryItem = categoryItems.get(i);
            pagerAdapter.addFragment(categoryItem.getFragment(getContext()));
            if (categoryItem.type == defaultPageType) {
                defaultPage = i;
            }
        }

        int currentPage = Math.min(defaultPage, pagerAdapter.getCount());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        pager.setCurrentItem(currentPage);

        slidingTabLayout.setupWithViewPager(pager);

        pager.postDelayed(() -> {
            if (pager != null) {
                DialogUtils.showRateSnackbar(getActivity(), pager);
            }
        }, 1000);
    }

    private void openSearch() {
        getNavigationController().pushViewController(SearchFragment.newInstance(null), "SearchFragment");
    }

    @Override
    public void onAlbumArtistClicked(AlbumArtist albumArtist, View transitionView) {
        String transitionName = ViewCompat.getTransitionName(transitionView);
        ArtistDetailFragment detailFragment = ArtistDetailFragment.newInstance(albumArtist, transitionName);
        pushDetailFragment(detailFragment, transitionView);
    }

    @Override
    public void onAlbumClicked(Album album, View transitionView) {
        String transitionName = ViewCompat.getTransitionName(transitionView);
        AlbumDetailFragment detailFragment = AlbumDetailFragment.newInstance(album, transitionName);
        pushDetailFragment(detailFragment, transitionView);
    }

    @Override
    public void onGenreClicked(Genre genre) {
        pushDetailFragment(GenreDetailFragment.newInstance(genre), null);
    }

    @Override
    public void onPlaylistClicked(Playlist playlist) {
        pushDetailFragment(PlaylistDetailFragment.newInstance(playlist), null);
    }

    void pushDetailFragment(BaseDetailFragment detailFragment, @Nullable View transitionView) {

        List<Pair<View, String>> transitions = new ArrayList<>();

        if (transitionView != null) {
            String transitionName = ViewCompat.getTransitionName(transitionView);
            transitions.add(new Pair<>(transitionView, transitionName));
            //            transitions.add(new Pair<>(toolbar, "toolbar"));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Transition moveTransition = TransitionInflater.from(getContext()).inflateTransition(R.transition.image_transition);
                detailFragment.setSharedElementEnterTransition(moveTransition);
                detailFragment.setSharedElementReturnTransition(moveTransition);
            }
        }

        getNavigationController().pushViewController(detailFragment, "DetailFragment", transitions);
    }

    @Override
    protected String screenName() {
        return "LibraryController";
    }

    @Override
    public ContextualToolbar getContextualToolbar() {
        return contextualToolbar;
    }
}
