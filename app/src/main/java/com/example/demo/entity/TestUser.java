package com.example.demo.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxiaolong on 2015-12-3 0003.
 */
public class TestUser implements Parcelable {



    //实现反序列化功能
    public static final Parcelable.Creator<TestUser> CREATOR = new Parcelable.Creator<TestUser>() {
        @Override
        public TestUser createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public TestUser[] newArray(int i) {
            return new TestUser[0];
        }
    };

    //内容描述功能
    @Override
    public int describeContents() {
        return 0;
    }

    //序列化功能
    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
