package com.desmond.rippledemo.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.ripple.drawable.RippleCompatDrawable;
import com.desmond.ripple.config.RippleConfig;
import com.desmond.ripple.util.RippleUtil;
import com.desmond.rippledemo.R;
import com.desmond.rippledemo.fragment.base.BaseFragment;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class PaletteFragment extends BaseFragment {
    private RippleUtil.PaletteMode[] paletteModes = new RippleUtil.PaletteMode[] {
            RippleUtil.PaletteMode.VIBRANT,
            RippleUtil.PaletteMode.VIBRANT_LIGHT,
            RippleUtil.PaletteMode.VIBRANT_DARK,
            RippleUtil.PaletteMode.MUTED,
            RippleUtil.PaletteMode.MUTED_LIGHT,
            RippleUtil.PaletteMode.MUTED_DARK
    };

    @Override
    public void onResume() {
        super.onResume();
        final RippleConfig cfg = getDefaultConfig();

        RippleCompat.apply(getImageView(), cfg);
        RippleCompat.apply(getSpinner(), cfg);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RippleCompat.setPaletteMode(getImageView(), paletteModes[position]);
    }

    @Override
    public String[] getStringArray() {
        return getResources().getStringArray(R.array.palette_modes);
    }

    @Override
    public RippleConfig getDefaultConfig() {
        return new RippleConfig()
                .setIsEnablePalette(true)
                .setIsFull(true)
                .setIsSpin(true)
                .setType(RippleCompatDrawable.Type.TRIANGLE);
    }
}
