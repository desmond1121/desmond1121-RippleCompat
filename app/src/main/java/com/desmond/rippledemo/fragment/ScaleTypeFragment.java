package com.desmond.rippledemo.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.ripple.drawable.RippleCompatDrawable;
import com.desmond.ripple.config.RippleConfig;
import com.desmond.rippledemo.R;
import com.desmond.rippledemo.fragment.base.BaseFragment;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class ScaleTypeFragment extends BaseFragment {
    private ImageView.ScaleType[] types = new ImageView.ScaleType[] {
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.MATRIX
    };

    @Override
    public void onResume() {
        super.onResume();
        RippleCompat.apply(getImageView(), getDefaultConfig());
        RippleCompat.apply(getSpinner());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RippleCompat.setScaleType(getImageView(), types[position]);
    }

    @Override
    public String[] getStringArray() {
        return getResources().getStringArray(R.array.scale_types);
    }

    @Override
    public RippleConfig getDefaultConfig() {
        return new RippleConfig()
                .setIsFull(true)
                .setType(RippleCompatDrawable.Type.HEART);
    }
}
