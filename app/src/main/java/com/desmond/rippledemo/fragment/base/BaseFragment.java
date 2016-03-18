package com.desmond.rippledemo.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.desmond.ripple.config.RippleConfig;
import com.desmond.rippledemo.R;

/**
 * @author Jonatan Salas
 */
public abstract class BaseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scaletype, container, false);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        imageView = (ImageView) view.findViewById(R.id.image_view);

        spinner.setAdapter(getDefaultAdapter());
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        spinner = null;
        imageView = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //You will have to override this method..
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing to do here..
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ArrayAdapter<String> getDefaultAdapter() {
        return new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getStringArray()
        );

    }

    public abstract String[] getStringArray();

    public abstract RippleConfig getDefaultConfig();
}
