package com.gb.rating.ui.review;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gb.rating.R;

public class QRFragment extends Fragment {

    private View view;
    private TextView qrText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qr,
                container, false);

        qrText = view.findViewById(R.id.qr_text);

        Bundle bundle = getArguments();
        if (bundle != null) {
            qrText.setText(bundle.getString("tag"));
        }

        return view;
    }

}
