package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * listview/gridview通用adapter
 * Created by kenny on 2016/2/17.
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;
	
	public CommonAdapter(Context context,List<T> datas,int layoutId){
		this.mContext = context;
		this.mDatas = datas;
		mInflater = LayoutInflater.from(mContext);
		this.layoutId = layoutId;
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		CommonViewHolder holder = CommonViewHolder.get(mContext, convertView, parent, layoutId,position);
		
		convert(holder, getItem(position));
		
		return holder.getConvertView();
	}

	public abstract void convert(CommonViewHolder holder,T t);
}
