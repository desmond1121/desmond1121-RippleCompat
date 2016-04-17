package com.desmond.rippledemo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.rippledemo.R;
import com.desmond.rippledemo.adapter.ViewPagerAdapter;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class CompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat);

        //Allow passing "this" reference to this method, pass ApplicationContext as param
        //to avoid memory leaks.
        RippleCompat.init(getApplicationContext());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(toolbar);

        if (null != viewPager) {
            viewPager.setAdapter(ViewPagerAdapter.getDefaultAdapter(getSupportFragmentManager()));
        }

        if (null != tabLayout) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
