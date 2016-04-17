package com.desmond.rippledemo.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.ripple.drawable.RippleCompatDrawable;
import com.desmond.ripple.config.RippleConfig;
import com.desmond.rippledemo.R;
import com.desmond.rippledemo.fragment.base.BaseFragment;

import static android.widget.ImageView.ScaleType;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class ScaleTypeFragment extends BaseFragment {
    private ScaleType[] types = new ScaleType[] {
            ScaleType.FIT_CENTER,
            ScaleType.FIT_XY,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE,
            ScaleType.MATRIX
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
