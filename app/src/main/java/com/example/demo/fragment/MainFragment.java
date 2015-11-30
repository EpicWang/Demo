package com.example.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.R;

/**
 * Created by wangxiaolong on 2015-11-23 0023.
 */
public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_recycleview,container,false);
        TextView temp = new TextView(getActivity());
        temp.setText("Welcome to Demo!");
        rl.addView(temp);
        return rl;
    }
}
