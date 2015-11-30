package com.example.demo.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.demo.constant.Constant;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by wangxiaolong on 2015-11-24 0024.
 */
public class DetectUtils {


    public interface CallBack {
        void success(JSONObject jsonObject);

        void error(FaceppParseException exception);
    }


    public static void detect(final Bitmap bitmap, final CallBack callBack) {

        final HttpRequests httpRequests = new HttpRequests(Constant.FACEPP_KEY, Constant.FACEPP_SECRET, true, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap smallBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                smallBitmap =  ImageCompressUtil.compressByQuality(smallBitmap,1024);
                smallBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                PostParameters params = new PostParameters();
                params.setImg(byteArray);
                Log.i("Tag", "pic size: "+byteArray.length+" byte");
                try {
                    System.setProperty("http.keepAlive", "false");
                    //Log.i("Tag", httpRequests.detectionDetect(params).toString());
                    JSONObject jsonObject = httpRequests.detectionDetect(params);
                    if (callBack != null) {
                        callBack.success(jsonObject);
                    }

                    Log.i("Tag",jsonObject.toString());
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.error(e);
                    }
                }

            }
        }).start();


    }
}
