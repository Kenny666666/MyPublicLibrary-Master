package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 通用ViewHolder
 * @author kenny
 * @time 2016/2/17 22:39
 */
public class CommonViewHolder {
	/**android提供的键值对map，它的key必须是int，比hashMap效率高，*/
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public CommonViewHolder(Context context,ViewGroup parent,int layoutId,int position){
		this.mPosition= position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	/**入口方法*/
	public static CommonViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if (convertView==null) {
			return new CommonViewHolder(context, parent, layoutId, position);
		}else {
			CommonViewHolder holder = (CommonViewHolder) convertView.getTag();
			//更新位置
			holder.mPosition = position;
			return holder;
		}
	}
	
	/**
	 * 使用范型来继承View,通过viewId获取控件
	 * @param viewId 控件id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public View getConvertView(){
		return mConvertView;
	}
	
	/**
	 * 设置TextView的值
	 * @param viewId 控件ID
	 * @param text 控件的值
	 * @return
	 */
	public CommonViewHolder setText(int viewId,String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	//========================给图片控件设置值======================================
	public CommonViewHolder setImageResource(int viewId,int resId){
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}
	
	public CommonViewHolder setImageBitmap(int viewId,Bitmap bitmap){
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}
	
	public CommonViewHolder setImageURI(int viewId,String url){
//		ImageView view = getView(viewId);
		//加载网络图片时要的方法
//		Imageloader.getInstance().loadImg(view,url);
		return this;
	}
	
	public int getPosition(){
		return mPosition;
	}
}
