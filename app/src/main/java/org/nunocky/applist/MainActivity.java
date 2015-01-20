package org.nunocky.applist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;


public class MainActivity extends ActionBarActivity {

    private static final String FRAGMENT_TAG = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MEMO : ダミーのレイアウトを最初に入れておかないと、ft.addしても何も表示されない
        //        → 関連? http://y-anz-m.blogspot.jp/2013/05/support-package-fragment.html
        setContentView(new FrameLayout(this));

        FragmentManager mgr = getSupportFragmentManager();
        AppListFragment fragment = (AppListFragment) mgr.findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new AppListFragment();
            FragmentTransaction ft = mgr.beginTransaction();
            ft.add(android.R.id.content, fragment, FRAGMENT_TAG);
            ft.commit();
        }
    }
}
