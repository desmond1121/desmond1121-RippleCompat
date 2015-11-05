package com.desmond.rippledemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.desmond.ripple.RippleCompat;
import com.desmond.rippledemo.fragments.PaletteFragment;
import com.desmond.rippledemo.fragments.ScaleTypeFragment;
import com.desmond.rippledemo.fragments.WidgetTestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiayi Yao on 2015/11/5.
 */
public class NormalActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        RippleCompat.init(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(getAdapter());
    }

    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WidgetTestFragment(), "Widget");
        adapter.addFragment(new ScaleTypeFragment(), "ScaleType");
        adapter.addFragment(new PaletteFragment(), "Palette");
        return adapter;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
