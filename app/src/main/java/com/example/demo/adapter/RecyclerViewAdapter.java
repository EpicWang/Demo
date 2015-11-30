package com.example.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxiaolong on 2015-11-23 0023.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private List<Integer> mHeight;
    private boolean isStraggered;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view,int postion);
        void onItemLongClick(View view,int postion);
    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public RecyclerViewAdapter(Context context, List<String> datas, boolean isStaggered) {
        this.isStraggered = isStaggered;
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
        mHeight = new ArrayList<Integer>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeight.add((int) (Math.random() * 300) + 100);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (isStraggered) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            lp.height = mHeight.get(position);
            holder.itemView.setLayoutParams(lp);
        }

        holder.mTextView.setText(mDatas.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void addDatas(int pos){
        mDatas.add("new item");
        notifyItemInserted(pos);
    }
    public void deleteDates(int pos){
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView mTextView;

    public MyViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.id_recyclerview_item);
    }
}