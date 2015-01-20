package org.nunocky.applist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.nunocky.applist.apploader.AppEntry;
import org.nunocky.applist.apploader.AppListLoader;

import java.util.List;

public class AppListFragment extends ListFragment {

    private AppListAdapter mAppListAdapter;

    private LoaderManager.LoaderCallbacks<List<AppEntry>> mCallbacks = new LoaderManager.LoaderCallbacks<List<AppEntry>>() {

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
            mAppListAdapter.setData(data);

            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            mAppListAdapter.setData(null);
        }

    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No applications");
        setHasOptionsMenu(true);

        mAppListAdapter = new AppListAdapter(getActivity());
        setListAdapter(mAppListAdapter);

        setListShown(false);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, mCallbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        SearchView sv = new SearchView(getActivity());
        sv.setIconifiedByDefault(true);
        sv.setSubmitButtonEnabled(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                String filterText = !TextUtils.isEmpty(newText) ? newText : null;
                mAppListAdapter.getFilter().filter(filterText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getLoaderManager().restartLoader(0, null, mCallbacks);
                return true;
            }
        });

        MenuItemCompat.setActionView(item, sv);
    }

}

