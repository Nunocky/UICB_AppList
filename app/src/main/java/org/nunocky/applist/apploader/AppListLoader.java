package org.nunocky.applist.apploader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
    private List<AppEntry> mAppList;
    private final PackageManager mPackageManager;
    private PackageChangeReceiver mPackageChangeReceiver;

    //    private static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
    //        private final Collator sCollator = Collator.getInstance();
    //
    //        @Override
    //        public int compare(AppEntry lhs, AppEntry rhs) {
    //            return sCollator.compare(lhs.getLabel(), rhs.getLabel());
    //        }
    //    };

    public AppListLoader(Context context) {
        super(context);
        mPackageManager = getContext().getPackageManager();
    }

    @Override
    public List<AppEntry> loadInBackground() {
        int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;

        List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(flags);

        if (apps == null) {
            apps = new ArrayList<>();
        }

        List<AppEntry> entries = new ArrayList<>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            ApplicationInfo info = apps.get(i);
            AppEntry entry = new AppEntry(mPackageManager, info);
            entries.add(entry);
        }

        Collections.sort(
                entries,
                // ALPHA_COMPARATOR
                new Comparator<AppEntry>() {
                    private final Collator sCollator = Collator.getInstance();

                    @Override
                    public int compare(AppEntry lhs, AppEntry rhs) {
                        return sCollator.compare(lhs.getLabel(), rhs.getLabel());
                    }
                }
        );
        return entries;
    }

    @Override
    public void deliverResult(List<AppEntry> apps) {
        if (isReset()) {
            if (apps != null) {
                onReleaseResources(apps);
            }
            return;
        }

//        List<AppEntry> oldApps = apps;
        mAppList = apps;

        if (isStarted()) {
            super.deliverResult(apps);
        }

//        if (oldApps != null && oldApps != apps) {
//            onReleaseResources(oldApps);
//        }
    }

    @Override
    protected void onStartLoading() {
        if (mAppList != null) {
            deliverResult(mAppList);
        }

        if (mPackageChangeReceiver == null) {
            mPackageChangeReceiver = new PackageChangeReceiver(this);
        }

        if (takeContentChanged() || mAppList == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void onCanceled(List<AppEntry> apps) {
        super.onCanceled(apps);
        if (apps != null) {
            onReleaseResources(apps);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (mAppList != null) {
            onReleaseResources(mAppList);
            mAppList = null;
        }

        if (mPackageChangeReceiver != null) {
            getContext().unregisterReceiver(mPackageChangeReceiver);
            mPackageChangeReceiver = null;
        }
    }

    protected void onReleaseResources(List<AppEntry> apps) {
    }


}
