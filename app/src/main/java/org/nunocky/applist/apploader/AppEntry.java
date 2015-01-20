package org.nunocky.applist.apploader;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppEntry {
    private PackageManager mManager;
    private ApplicationInfo mInfo;

    private String mLabel;
    private Drawable mIcon;

    public AppEntry(PackageManager pm, ApplicationInfo info) {
        mManager = pm;
        mInfo = info;
    }

    public String getLabel() {
        if (mLabel == null) {
            CharSequence label = mInfo.loadLabel(mManager);
            mLabel = (label != null) ? label.toString() : mInfo.packageName;
        }
        return mLabel;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            mIcon = mInfo.loadIcon(mManager);
        }
        return mIcon;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
