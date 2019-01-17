package com.adityaarora.liveedgedetection.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.adityaarora.liveedgedetection.R;
import com.adityaarora.liveedgedetection.constants.ScanConstants;
import com.adityaarora.liveedgedetection.enums.ScanHint;
import com.adityaarora.liveedgedetection.interfaces.IScanner;
import com.adityaarora.liveedgedetection.util.ScanUtils;
import com.adityaarora.liveedgedetection.view.PolygonPoints;
import com.adityaarora.liveedgedetection.view.PolygonView;
import com.adityaarora.liveedgedetection.view.Quadrilateral;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class initiates camera and detects edges on live view
 */
public class SelfScanActivity extends AppCompatActivity implements IScanner {
    private static final String TAG = SelfScanActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;


    private static final String mOpenCvLibrary = "opencv_java3";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 270);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 2);
        ORIENTATIONS.append(Surface.ROTATION_270, 3);
    }


    public final static Stack<PolygonPoints> allDraggedPointsStack = new Stack<>();
    private PolygonView polygonView;
    private ImageView cropImageView;

    private Bitmap copyBitmap;

    public CameraDevice mCameraDevice;
    private TextureView textureView;
    private ImageView buttonSelf;
    private File file;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private Size imageDimension;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private String cameraId;
    private ImageReader imageReader;
    private TextView tvSelf;

    private static String NAME = "NAME";

    public static Intent newIntent(String name, Activity activity) {
        Intent intent = new Intent(activity, SelfScanActivity.class);
        intent.putExtra(NAME, name);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        cropImageView = findViewById(R.id.crop_image_view);
        polygonView = findViewById(R.id.polygon_view);
        textureView = findViewById(R.id.texture);
//        buttonSelf = findViewById(R.id.bt_take_self);
        tvSelf = findViewById(R.id.tx_self_scan);

        if (getIntent().hasExtra(NAME)) {
            String name = getIntent().getStringExtra(NAME);
            tvSelf.setText(String.format(getString(R.string.tx_scan_photo), name));
        } else {
            tvSelf.setVisibility(View.GONE);
        }


        buttonSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

//        cropAcceptBtn.setOnClickListener(this);
//        cropRejectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                    TransitionManager.beginDelayedTransition(containerScan);
//                cropLayout.setVisibility(View.GONE);
//                mImageSurfaceView.setPreviewCallback();
//            }
//        });
        checkCameraPermissions();


    }

    private void openCamera2() {
        if (checkCameraPermissions()) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                cameraId = cameraManager.getCameraIdList()[1];
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                cameraManager.openCamera(cameraId, stateCallback, null);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            Matrix matrix = new Matrix();
            matrix.setScale(-1, 1);
            //move it back to in view otherwise it'll be off to the left.
            matrix.postTranslate(i, 0);
            textureView.setTransform(matrix);

            openCamera2();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            closeCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {


        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    };

    private Boolean checkCameraPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    void starBackgroundTread() {
        mBackgroundThread = new HandlerThread("Camera Back");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        if (mCameraDevice == null) {
            return;
        }

        if (!checkCameraPermissions()) {
            return;
        }

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraDevice.getId());
            Size[] jpegSizes = null;

            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            int width = 640;
            int height = 480;

            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            final ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outSurfaces = new ArrayList<>(2);
            outSurfaces.add(reader.getSurface());
            outSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (output != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onPictureClicked(ScanUtils.decodeBitmapFromFile(file.getPath(), ""));
                                }
                            });
                            output.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);

                    createCameraPreview();
                }
            };

            mCameraDevice.createCaptureSession(outSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), captureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //do nothing
                }
            }, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setCropButtonListener() {
//        buttonSelf.setText(R.string.tx_crop);
        buttonSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage();
            }
        });
    }

    private void cropImage() {
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
                ScanConstants.IMAGE_NAME, SelfScanActivity.this, 90)[0];
        setResult(Activity.RESULT_OK, new Intent().putExtra(ScanConstants.SCANNED_SELF_RESULT, path));
//                bitmap.recycle();
        System.gc();
        finish();
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (mCameraDevice == null) {
                        return;
                    }

                    mCameraCaptureSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //do nothing
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            mCameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        starBackgroundTread();
        if (textureView.isAvailable()) {
            openCamera2();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopBackgroundThread();
    }

    @Override
    public void displayHint(ScanHint scanHint) {
    }

    @Override
    public void clearAndInvalidateCanvas() {
    }

    @Override
    public void invalidateCanvas() {
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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                    TransitionManager.beginDelayedTransition(containerScan);
                textureView.setVisibility(View.GONE);
                cropImageView.setImageBitmap(copyBitmap);
                cropImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                setCropButtonListener();
//                cropImage();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    static {
        System.loadLibrary(mOpenCvLibrary);
    }
}
