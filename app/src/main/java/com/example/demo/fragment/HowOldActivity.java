package com.example.demo.fragment;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.demo.R;
import com.example.demo.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HowOldActivity extends AppCompatActivity implements SurfaceHolder.Callback {


    private static final int CAMERA_FRONT = 0;
    private static final int CAMERA_BACK = 0;
    private String filePath;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            //将拍摄的照片传回到上一个activity
            if (FileUtils.isExternalStorageWritable()) {
                //String filePath = Environment.getExternalStorageDirectory() + "/" + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + ".jpg";
                File file = new File(filePath);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();

                    //                    Intent intent = new Intent();
                    //                    intent.putExtra("filePath",filePath);
                    //                    setResult(RESULT_OK, intent);//将这个intent返回
                    HowOldActivity.this.finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_old);
        initViews();
        filePath = getIntent().getStringExtra("filePath");

    }

    private void initViews() {

        mSurfaceView = (SurfaceView) findViewById(R.id.id_surfaceview);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }


    //设置拍照按钮的点击事件
    public void capture(View view) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setPictureFormat(ImageFormat.JPEG);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {
                    camera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(CAMERA_BACK);
        }
        if (mHolder != null) {
            try {
                //开始实时预览相机画面
                startPreview(mCamera, mHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    //获取一个硬件Camera
    public Camera getCamera(int frontOrBack) {
        int cameraCount = 0;
        Camera camera = null;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.i("TAG","共发现 "+cameraCount+" 个摄像头");
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (frontOrBack == CAMERA_FRONT && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            if (frontOrBack == CAMERA_BACK && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        }

        return camera;

    }

    //开始预览
    public void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);//将相机转为垂直方向
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //释放相机资源
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.stopPreview();
        startPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }
}
