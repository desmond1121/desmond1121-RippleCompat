package com.desmond.rippledemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.desmond.ripple.view.RippleCompat;
import com.desmond.ripple.config.RippleConfig;
import com.desmond.rippledemo.R;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class WidgetTestFragment extends Fragment implements View.OnClickListener{
    private int[] color = new int[] {
            0x70ff0000,
            0x7000ff00,
            0x700000ff,
            0x70ff00ff
    };

    private View someView;
    private Button button;
    private TextView textView;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_widget, container, false);

        textView = (TextView) view.findViewById(R.id.test_textView);
        button = (Button) view.findViewById(R.id.test_button);
        editText = (EditText) view.findViewById(R.id.test_et);
        someView = view.findViewById(R.id.test_heart);

        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RippleCompat.apply(textView, color[0]);
        RippleCompat.apply(button, color[1]);
        RippleCompat.apply(editText, color[2]);
        RippleCompat.apply(someView, getDefaultConfig());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.test_button) {
            Snackbar.make(v, "Button Pressed!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private RippleConfig getDefaultConfig() {
        return new RippleConfig()
                .setRippleColor(color[3])
                .setIsFull(true);
    }
}
