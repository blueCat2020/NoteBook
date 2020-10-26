package com.swufe.zbp.notebook.ui.theme_style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.swufe.zbp.notebook.R;

public class ThemeStyleFragment extends Fragment {

    private ThemeStyleViewModel themeStyleViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        themeStyleViewModel =
                new ViewModelProvider(this).get(ThemeStyleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_theme_style, container, false);
        final TextView textView = root.findViewById(R.id.text_themeStyle);
        themeStyleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}