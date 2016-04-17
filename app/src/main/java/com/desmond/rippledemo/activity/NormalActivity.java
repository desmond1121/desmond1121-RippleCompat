package com.desmond.rippledemo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.rippledemo.R;
import com.desmond.rippledemo.adapter.ViewPagerAdapter;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class NormalActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        //Allow passing "this" reference to this method, pass ApplicationContext as param
        //to avoid memory leaks.
        RippleCompat.init(getApplicationContext());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(ViewPagerAdapter.getDefaultAdapter(getSupportFragmentManager()));
    }
}
