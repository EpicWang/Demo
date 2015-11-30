package com.example.demo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.utils.DetectUtils;
import com.facepp.error.FaceppParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by wangxiaolong on 2015-11-24 0024.
 */
public class HowOldFragment extends Fragment implements View.OnClickListener {


    private static final int RES_CODE = 0x001;
    private static final int RES_SUCCESS = 0x002;
    private static final int RES_ERROR = 0x003;
    private Button mDetect, mChose;
    private TextView mShowText;
    private View mProgressBar;
    private ImageView mImageView;
    private String mCurrentImageStr;
    private Bitmap mBitMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        RelativeLayout relativeLayout = initViews(inflater, container, savedInstanceState);

        initEvents();

        return relativeLayout;//这里记得返回创建的view
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RES_CODE) {

            if (data != null) {
                Uri uri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);//取到图片
                    mCurrentImageStr = cursor.getString(idx);
                    cursor.close();
                }
                resizePhoto();
                //这里不知道什么情况，图片会自动旋转90°，所以先旋转一下
                Matrix matrix = new Matrix();
                matrix.postRotate(readPictureDegree(mCurrentImageStr));
                Log.i("TAG", readPictureDegree(mCurrentImageStr) + "");
                mBitMap = Bitmap.createBitmap(mBitMap, 0, 0, mBitMap.getWidth(), mBitMap.getHeight(), matrix, true);
                mImageView.setImageBitmap(mBitMap);
                mShowText.setText("Click Detect");
            }
        }
    }

    /**
     * @param path
     * @return
     * @desc <pre>获取图片旋转的角度</pre>
     * @author Weiliang Hu
     * @date 2013-9-18
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private void resizePhoto() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载，只获取宽高
        BitmapFactory.decodeFile(mCurrentImageStr, options);

        //获取一个合理的图片压缩比率
        double ratio = Math.max(options.outWidth * 0.1d / 1024f, options.outHeight * 0.1d / 1024f);
        options.inSampleSize = (int) Math.ceil(ratio);

        options.inJustDecodeBounds = false;// 加载图片
        mBitMap = BitmapFactory.decodeFile(mCurrentImageStr, options);
    }

    private RelativeLayout initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_howold, container, false);

        mDetect = (Button) relativeLayout.findViewById(R.id.id_detect);
        mChose = (Button) relativeLayout.findViewById(R.id.id_chose);
        mShowText = (TextView) relativeLayout.findViewById(R.id.id_face_info);
        mProgressBar = relativeLayout.findViewById(R.id.id_watting);
        mImageView = (ImageView) relativeLayout.findViewById(R.id.id_imageview);
        return relativeLayout;
    }

    private void initEvents() {
        mDetect.setOnClickListener(this);
        mChose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_detect:
                mProgressBar.setVisibility(View.VISIBLE);
                DetectUtils.detect(mBitMap, new DetectUtils.CallBack() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        Message message = new Message();
                        message.what = RES_SUCCESS;
                        message.obj = jsonObject;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void error(FaceppParseException exception) {
                        Message message = new Message();
                        message.what = RES_ERROR;
                        message.obj = exception.getErrorMessage();
                        handler.sendMessage(message);
                    }
                });
                break;
            case R.id.id_chose:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RES_CODE);
                break;
        }

    }

    private android.os.Handler handler = new android.os.Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            mProgressBar.setVisibility(View.GONE);
            switch (message.what) {

                case RES_SUCCESS:
                    parseMessage(message.obj.toString());
                    mImageView.setImageBitmap(mBitMap);
                    break;
                case RES_ERROR:
                    String error = (String) message.obj;
                    if (TextUtils.isEmpty(error)) {
                        mShowText.setText("Error.");
                    } else {
                        mShowText.setText(error);
                    }
                    break;
            }
            return true;
        }
    });

    //解析返回的json数据
    private void parseMessage(String message) {


        Bitmap bitmap = Bitmap.createBitmap(mBitMap.getWidth(), mBitMap.getHeight(), mBitMap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        //画出原图
        canvas.drawBitmap(mBitMap, 0, 0, null);

        //初始化画笔
        Paint mPaint = new Paint();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            int responseCode = jsonObject.getInt("response_code");
            JSONArray jsonArray = jsonObject.getJSONArray("face");
            mShowText.setText("find " + jsonArray.length() + " face");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);//监测到的第一张脸

                int age = (int) obj.getJSONObject("attribute").getJSONObject("age").get("value");
                String male = (String) obj.getJSONObject("attribute").getJSONObject("gender").get("value");

                //取到脸部中心点的百分比和脸部的长宽。
                float x = new BigDecimal(obj.getJSONObject("position").getJSONObject("center").get("x") + "").floatValue();
                float y = new BigDecimal(obj.getJSONObject("position").getJSONObject("center").get("y") + "").floatValue();
                float w = new BigDecimal(obj.getJSONObject("position").get("width") + "").floatValue();
                float h = new BigDecimal(obj.getJSONObject("position").get("height") + "").floatValue();
                Log.i("TAG", responseCode + "," + age + "," + male + "," + x + "," + y + "," + w + "," + h);

                //将取到的百分比转换为实际像素
                x = x / 100 * bitmap.getWidth();
                y = y / 100 * bitmap.getHeight();
                w = w / 100 * bitmap.getWidth();
                h = h / 100 * bitmap.getHeight();

                mPaint.setColor(0xffffffff);
                mPaint.setStrokeWidth(3f);//划线的宽度

                //在原图上画脸框
                canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y - h / 2, x + w / 2, y - h / 2, mPaint);
                canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2, mPaint);

                //提示气泡
                Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(male));
                int ageWidth = ageBitmap.getWidth();
                int ageHeight = ageBitmap.getHeight();
                //设置气泡缩放比
                if (bitmap.getWidth() < mImageView.getWidth() && bitmap.getHeight() < mImageView.getHeight()) {
                    float ratio = Math.min(bitmap.getWidth()*1.0f / mImageView.getWidth(), bitmap.getHeight()*1.0f  / mImageView.getHeight());
                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int) (ageWidth * ratio), (int) (ageHeight * ratio), false);
                }
                canvas.drawBitmap(ageBitmap,x-w/2,y-h/2-ageBitmap.getHeight(),null);
                //canvas.drawBitmap(ageBitmap, x, y, null);
                //将原图显示
                mBitMap = bitmap;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private Bitmap buildAgeBitmap(int age, boolean isMale) {

        TextView textView = (TextView) mProgressBar.findViewById(R.id.id_age);

        String msg = "";
        if (isMale) {
            msg = "帅哥 ";
            //设置性别图片
            //textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
        } else {
            msg = "美女 ";
            // 这种方式也可以
            //            Drawable drawable= getResources().getDrawable(R.drawable.female);
            //            drawable.setBounds(0, 0, 100, 20);
            //            textView.setCompoundDrawables(drawable, null, null, null);
           // textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
        }
        textView.setText(msg+age);
        textView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(textView.getDrawingCache());
        textView.destroyDrawingCache();

        return bitmap;
    }

}
