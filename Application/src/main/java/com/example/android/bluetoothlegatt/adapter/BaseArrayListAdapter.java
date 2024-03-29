package com.example.android.bluetoothlegatt.adapter;

import android.widget.BaseAdapter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseArrayListAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;
    private List<T> mDatas;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    public BaseArrayListAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<T>();
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    public BaseArrayListAdapter(Context context, List<T> datas){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (datas != null && datas.size() > 0) {
            mDatas = datas;
        }else{
            mDatas = new ArrayList<T>();
        }
    }

    /**
     * get context
     * <p>Title: getContext
     * <p>Description:
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    /**
     * get screen width
     * <p>Title: getScreenWidth
     * <p>Description:
     * @return
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * get screen height
     * <p>Title: getScreenHeight
     * <p>Description:
     * @return
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    /**
     * return all data
     * <p>Title: getAllData
     * <p>Description:
     * @return
     */
    public List<T> getAllData(){
        return mDatas;
    }

    /**
     * add data to head
     * <p>Title: addHead
     * <p>Description:
     * @param datas
     */
    public void addHead(T datas){
        if (datas != null) {
            mDatas.add(0, datas);
            notifyDataSetChanged();
        }
    }

    /**
     * add data list to head
     * <p>Title: addHead
     * <p>Description:
     * @param datas
     */
    public void addHead(List<T> datas){
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                mDatas.add(i, datas.get(i));
            }
            notifyDataSetChanged();
        }
    }


    public void add(T datas){
        if (datas != null) {
            mDatas.add(datas);
            notifyDataSetChanged();
        }
    }

    public void add(int position, T datas){
        if (position < 0 || position >= mDatas.size() || datas == null) {
            return;
        }
        mDatas.add(position, datas);
        notifyDataSetChanged();
    }

    /**
     * 更新指定位置数据
     * <p>Title: update
     * <p>Description:
     * @param position
     * @param datas
     */
    public void update(int position, T datas){
        if (position < 0 || position >= mDatas.size() || datas == null) {
            return;
        }
        mDatas.remove(position);
        mDatas.add(position, datas);
        notifyDataSetChanged();
    }

    /**
     * add data to adapter
     * <p>Title: addAll
     * <p>Description:
     * @param datas
     */
    public void addAll(List<T> datas){
        if (datas != null) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * refresh data to adapter, history data will be remove
     * <p>Title: refreshData
     * <p>Description:
     * @param datas
     */
    public void refreshData(List<T> datas){
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * clear all data
     * <p>Title: clear
     * <p>Description:
     */
    public void clear(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置数据
     * <p>Title: delete
     * <p>Description:
     * @param position
     */
    public void delete(int position){
        if (position < 0 || position >= mDatas.size()) {
            return;
        }
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (mDatas == null || mDatas.size() <= 0 || position < 0 || position >= mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
