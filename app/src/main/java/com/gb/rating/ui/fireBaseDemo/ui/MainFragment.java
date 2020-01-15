package com.gb.rating.ui.fireBaseDemo.ui;

import com.gb.rating.R;
import com.gb.rating.models.CafeItem;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();

        getActivity().findViewById(R.id.buttonTest).setOnClickListener(v -> mViewModel.testUpdateFirebaseRealtime());
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (!setIMEI()) ; //return
        getLifecycle().addObserver(mViewModel);
        mViewModel.getCafeList().observe(this, new Observer<List<CafeItem>>() {
            @Override
            public void onChanged(List<CafeItem> cafeList) {
                Log.d("Fragment", "Cafe's list changed");
            }
        });
    }

    private boolean setIMEI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Log.d("MainActityDemo", "No permission Manifest.permission.READ_PHONE_STATE");
                return false;
            }
        }

        TelephonyManager tm = (TelephonyManager)
                getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mViewModel.iMEI = tm.getImei();
        } else {
            mViewModel.iMEI = tm.getDeviceId();
        }
        return true;
    }


}
