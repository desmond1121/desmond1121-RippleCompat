package com.desmond.rippledemo.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.desmond.rippledemo.fragment.PaletteFragment;
import com.desmond.rippledemo.fragment.ScaleTypeFragment;
import com.desmond.rippledemo.fragment.WidgetTestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonatan Salas
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    @NonNull
    private final List<Fragment> fragments = new ArrayList<>();

    @NonNull
    private final String[] titles = new String[] {
            "Widget",
            "ScaleType",
            "Palette"
    };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter addFragment(Fragment fragment) {
        fragments.add(fragment);
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public static ViewPagerAdapter getDefaultAdapter(FragmentManager fragmentManager) {
        return new ViewPagerAdapter(fragmentManager)
                .addFragment(new WidgetTestFragment())
                .addFragment(new ScaleTypeFragment())
                .addFragment(new PaletteFragment());
    }
}
