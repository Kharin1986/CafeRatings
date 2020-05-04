package com.gb.rating.ui.review;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.lifecycle.ViewModelProvider;

import com.gb.rating.MainActivity;
import com.gb.rating.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class QrScanFragment extends Fragment {

    //by lazy :)
    private ReviewSharedViewModel model;

    private ReviewSharedViewModel getSharedViewModel() {
        if (model == null) {
            if (getActivity() != null) {
                model = new ViewModelProvider(getActivity()).get(ReviewSharedViewModel.class);
                return model;
            } else return null;
        } else return model;
    }

    private SurfaceView surfaceView;
    private View view;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 10;
    private boolean taskFinished = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getSharedViewModel();

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
                if (qrCodes.size() != 0 && !taskFinished) {
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(300);
                    //создаем фрагмент отзыва
                    ReviewFragment reviewFragment = new ReviewFragment();

                    if (model.firstPageInfo(qrCodes.valueAt(0).displayValue)) { //проверяем правильность определенного кода
                        taskFinished = true;
                        new Handler(Looper.getMainLooper()).post(new Runnable() { //камера снимает не в главном потоке
                            @Override
                            public void run() {
                                if (getActivity() != null) {
                                    ((MainActivity) getActivity()).getMKeepStateNavigator().navigate(R.id.navigation_review_second_page, true);
                                }
                            }
                        });
                    }
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
        if (getActivity() != null) {
            ((MainActivity) getActivity()).getMKeepStateNavigator().navigate(R.id.navigation_review, true);
        }
    }
}
