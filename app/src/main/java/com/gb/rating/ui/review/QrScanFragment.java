package com.gb.rating.ui.review;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gb.rating.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class QrScanFragment extends Fragment {

    private SurfaceView surfaceView;
    private View view;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qr_scan,
                container, false);

        if (checkForCameraPermission()) {
            initViews(view);
            startCamera();
            startScanQR();
        } else {
            requestCameraPermission();
        }

        return view;
    }

        private void initViews(View view) {
        surfaceView = view.findViewById(R.id.camera_view);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .build();
        }

    private void startScanQR() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(300);
                    //создаем фрагмент отзыва
                    ReviewFragment reviewFragment = new ReviewFragment();
                    //сохраняем строку с QR кода
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", qrCodes.valueAt(0).displayValue);
//                    reviewFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, reviewFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }
        });
    }

    private void startCamera() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    private boolean checkForCameraPermission() {
        return (checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                refreshFragment();
            } else {
                refreshFragment();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void refreshFragment() {
        getFragmentManager()
                .beginTransaction()
                .detach(QrScanFragment.this)
                .attach(QrScanFragment.this)
                .commit();
    }
}
