package com.adityaarora.liveedgedetection.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adityaarora.liveedgedetection.R;
import com.adityaarora.liveedgedetection.constants.ScanConstants;
import com.adityaarora.liveedgedetection.enums.ScanHint;
import com.adityaarora.liveedgedetection.interfaces.IScanner;
import com.adityaarora.liveedgedetection.util.ScanUtils;
import com.adityaarora.liveedgedetection.view.PolygonPoints;
import com.adityaarora.liveedgedetection.view.PolygonView;
import com.adityaarora.liveedgedetection.view.ProgressDialogFragment;
import com.adityaarora.liveedgedetection.view.Quadrilateral;
import com.adityaarora.liveedgedetection.view.ScanSurfaceView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This class initiates camera and detects edges on live view
 */
public class ScanActivity extends AppCompatActivity implements IScanner, View.OnClickListener {
    private static final String TAG = ScanActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int MY_PERMISSIONS_REQUEST_WRITE = 102;

    private ViewGroup containerScan;
    private FrameLayout cameraPreviewLayout;
    //    private ScanCanvasView scanCanvasView;
    private ScanSurfaceView mImageSurfaceView;
    private boolean isPermissionNotGranted;
    private static final String mOpenCvLibrary = "opencv_java3";
    private static ProgressDialogFragment progressDialogFragment;
    private TextView captureHintText;
//    private LinearLayout captureHintLayout;

    public final static Stack<PolygonPoints> allDraggedPointsStack = new Stack<>();
    private PolygonView polygonView;
    private ImageView cropImageView;
    private View cropAcceptBtn;
    private View cropRejectBtn;
    private Bitmap copyBitmap;
    private FrameLayout cropLayout;
    private Button btCapture;
    private View captureContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    private void init() {
        containerScan = findViewById(R.id.container_scan);
        cameraPreviewLayout = findViewById(R.id.camera_preview);
//        scanCanvasView      = findViewById(R.id.scan_canvas);
//        captureHintLayout   = findViewById(R.id.capture_hint_layout);
        btCapture = findViewById(R.id.bt_capture);
        captureHintText = findViewById(R.id.capture_hint_text);
        polygonView = findViewById(R.id.polygon_view);
        cropImageView = findViewById(R.id.crop_image_view);
        cropAcceptBtn = findViewById(R.id.crop_accept_btn);
        cropRejectBtn = findViewById(R.id.crop_reject_btn);
        cropLayout = findViewById(R.id.crop_layout);
        captureContainer = findViewById(R.id.capture_container);

        cropAcceptBtn.setOnClickListener(this);
        cropRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    TransitionManager.beginDelayedTransition(containerScan);
                cropLayout.setVisibility(View.GONE);

                captureContainer.setVisibility(View.VISIBLE);

                mImageSurfaceView.setPreviewCallback();
            }
        });

        checkWritePermissions();
    }

    private void checkWritePermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            isPermissionNotGranted = true;

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Enable write permission from settings", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE);
            }
        }else {
            if (!isPermissionNotGranted) {
//                mImageSurfaceView = new ScanSurfaceView(ScanActivity.this,
//                        scanCanvasView, this);
                mImageSurfaceView = new ScanSurfaceView(ScanActivity.this, this);
                cameraPreviewLayout.addView(mImageSurfaceView);
            } else {
                isPermissionNotGranted = false;
            }
        }
    }

    private void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Enable camera permission from settings", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                onRequestCamera(grantResults);
                break;

            case MY_PERMISSIONS_REQUEST_WRITE:
                checkCameraPermissions();
                break;
            default:
                break;
        }
    }

    private void onRequestCamera(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mImageSurfaceView = new ScanSurfaceView(ScanActivity.this,
//                                    scanCanvasView, ScanActivity.this);
                            mImageSurfaceView = new ScanSurfaceView(ScanActivity.this, ScanActivity.this);
                            cameraPreviewLayout.addView(mImageSurfaceView);
                        }
                    });
                }
            }, 500);

        } else {
            Toast.makeText(this, getString(R.string.camera_activity_permission_denied_toast), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    public void displayHint(ScanHint scanHint) {
//        captureHintLayout.setVisibility(View.VISIBLE);
        setCaptureButton(true);
        switch (scanHint) {
            case MOVE_CLOSER:
                captureHintText.setText(getResources().getString(R.string.move_closer));
                captureHintText.setTextColor(getResources().getColor(R.color.red));
//                captureHintLayout.setBackground(getResources().getDrawable(R.drawable.hint_red));
                break;
            case MOVE_AWAY:
                captureHintText.setText(getResources().getString(R.string.move_away));
                captureHintText.setTextColor(getResources().getColor(R.color.red));
//                captureHintLayout.setBackground(getResources().getDrawable(R.drawable.hint_red));
                break;
            case ADJUST_ANGLE:
                captureHintText.setText(getResources().getString(R.string.adjust_angle));
                captureHintText.setTextColor(getResources().getColor(R.color.red));
//                captureHintLayout.setBackground(getResources().getDrawable(R.drawable.hint_red));
                break;
            case FIND_RECT:
                captureHintText.setText(getResources().getString(R.string.finding_rect));
                captureHintText.setTextColor(getResources().getColor(R.color.white));
//                setCaptureButton(true);
//                captureHintLayout.setBackground(getResources().getDrawable(R.drawable.hint_white));
                break;
            case CAPTURING_IMAGE:
                captureHintText.setText(getResources().getString(R.string.hold_still));
                captureHintText.setTextColor(getResources().getColor(R.color.green));
//                setCaptureButton(true);
//                captureHintLayout.setBackground(getResources().getDrawable(R.drawable.hint_green));
                break;
            case NO_MESSAGE:
//                captureHintLayout.setVisibility(GONE);
                captureHintText.setText(getResources().getString(R.string.capturing));
                captureHintText.setTextColor(getResources().getColor(R.color.colorAccent));
                clearAndInvalidateCanvas();
                break;
            default:
                break;
        }
    }

    private void setCaptureButton(boolean visible) {
        if (visible){
            btCapture.setVisibility(View.VISIBLE);
            btCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mImageSurfaceView.takePicture();
                }
            });
        }else {
            btCapture.setVisibility(View.INVISIBLE);
            btCapture.setOnClickListener(null);
        }
    }

    @Override
    public void clearAndInvalidateCanvas() {
//        scanCanvasView.clear();
        invalidateCanvas();
    }

    @Override
    public void invalidateCanvas() {
//        scanCanvasView.invalidate();
    }

    @Override
    public void onPictureClicked(final Bitmap bitmap) {
        try {
            copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            int height = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
            int width = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth();

            copyBitmap = ScanUtils.resizeToScreenContentSize(copyBitmap, width, height);
            Mat originalMat = new Mat(copyBitmap.getHeight(), copyBitmap.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(copyBitmap, originalMat);
            ArrayList<PointF> points;
            Map<Integer, PointF> pointFs = new HashMap<>();
            try {
                Quadrilateral quad = ScanUtils.detectLargestQuadrilateral(originalMat);
                if (null != quad) {
                    double resultArea = Math.abs(Imgproc.contourArea(quad.contour));
                    double previewArea = originalMat.rows() * originalMat.cols();
                    if (resultArea > previewArea * 0.08) {
                        points = new ArrayList<>();
                        points.add(new PointF((float) quad.points[0].x, (float) quad.points[0].y));
                        points.add(new PointF((float) quad.points[1].x, (float) quad.points[1].y));
                        points.add(new PointF((float) quad.points[3].x, (float) quad.points[3].y));
                        points.add(new PointF((float) quad.points[2].x, (float) quad.points[2].y));
                    } else {
                        points = ScanUtils.getPolygonDefaultPoints(copyBitmap);
                    }

                } else {
                    points = ScanUtils.getPolygonDefaultPoints(copyBitmap);
                }

                int index = -1;
                for (PointF pointF : points) {
                    pointFs.put(++index, pointF);
                }

                polygonView.setPoints(pointFs);
                int padding = (int) getResources().getDimension(R.dimen.scan_padding);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(copyBitmap.getWidth() + 2 * padding, copyBitmap.getHeight() + 2 * padding);
                layoutParams.gravity = Gravity.CENTER;
                polygonView.setLayoutParams(layoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    TransitionManager.beginDelayedTransition(containerScan);
                cropLayout.setVisibility(View.VISIBLE);

                captureContainer.setVisibility(View.GONE);

                cropImageView.setImageBitmap(copyBitmap);
                cropImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private synchronized void showProgressDialog(String message) {
        if (progressDialogFragment != null && progressDialogFragment.isVisible()) {
            // Before creating another loading dialog, close all opened loading dialogs (if any)
            progressDialogFragment.dismissAllowingStateLoss();
        }
        progressDialogFragment = null;
        progressDialogFragment = new ProgressDialogFragment(message);
        FragmentManager fm = getFragmentManager();
        progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
    }

    private synchronized void dismissDialog() {
        progressDialogFragment.dismissAllowingStateLoss();
    }

    static {
        System.loadLibrary(mOpenCvLibrary);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, PointF> points = polygonView.getPoints();

        Bitmap croppedBitmap;

        if (ScanUtils.isScanPointsValid(points)) {
            Point point1 = new Point(points.get(0).x, points.get(0).y);
            Point point2 = new Point(points.get(1).x, points.get(1).y);
            Point point3 = new Point(points.get(2).x, points.get(2).y);
            Point point4 = new Point(points.get(3).x, points.get(3).y);
            croppedBitmap = ScanUtils.enhanceReceipt(copyBitmap, point1, point2, point3, point4);
        } else {
            croppedBitmap = copyBitmap;
        }

        String path = ScanUtils.saveToInternalMemory(croppedBitmap, ScanConstants.IMAGE_DIR,
                ScanConstants.IMAGE_NAME, ScanActivity.this, 90)[0];
        setResult(Activity.RESULT_OK, new Intent().putExtra(ScanConstants.SCANNED_RESULT, path));
        //bitmap.recycle();
        System.gc();
        finish();
    }
}
