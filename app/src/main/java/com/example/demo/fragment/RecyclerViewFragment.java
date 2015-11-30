package com.example.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.demo.R;
import com.example.demo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.itemanimator.SlideScaleInOutRightItemAnimator;


/**
 * Created by wangxiaolong on 2015-11-23 0023.
 */
public class RecyclerViewFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private RecyclerViewAdapter mAdapter;

    public RecyclerViewFragment() {

        mDatas = new ArrayList<String>();
        for (int i = 'A'; i <= 'z'; i++) {
            mDatas.add((char) i + "");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置这个fragment专属menu
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_recycleview, container, false);
        mRecyclerView = (RecyclerView) rl.findViewById(R.id.id_recyclerview_main);
        mAdapter = new RecyclerViewAdapter(getActivity(), mDatas,false);
        mAdapter.SetOnItemClickListener(this);
       // AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mAdapter, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));//设置分隔线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));

        //这里首先使用RelativeLayout获取RecyclerView，然后删除所有View，否则会报错
        rl.removeAllViews();

        return mRecyclerView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        System.out.print("id:"+id);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list) {
            mAdapter = new RecyclerViewAdapter(getActivity(), mDatas,false);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(mRecyclerView));
            return true;
        } else if (id == R.id.action_grid) {
            mAdapter = new RecyclerViewAdapter(getActivity(), mDatas,false);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(this);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mRecyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(mRecyclerView));
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));//设置分隔线
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
            return true;
        } else if (id == R.id.action_hor_gridview) {
            mAdapter = new RecyclerViewAdapter(getActivity(), mDatas,false);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(this);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL));
            mRecyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(mRecyclerView));
            return true;

        } else if (id == R.id.action_staggered) {
            mAdapter = new RecyclerViewAdapter(getActivity(), mDatas,true);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(this);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(mRecyclerView));
            return true;
        }else if (id == R.id.action_add) {
            mAdapter.addDatas(1);
            return true;
        }else if (id == R.id.action_delete) {
            mAdapter.deleteDates(1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.recycleview_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onItemClick(View view, int postion) {
        Log.i("TAG","ItemClick  pos:"+postion);
    }

    @Override
    public void onItemLongClick(View view, int postion) {
        Log.i("TAG","ItemLongClick  pos:"+postion);
    }
}
