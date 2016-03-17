package com.kenny.baselibrary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import java.util.ArrayList;

/**
 * 此activity用于activity中存在多个fragment成队列式切换时使用，现工程中暂未使用
 * @author kenny
 * @time 2015/12/21 22:20
 */
public abstract  class ManagerFragmentActivity extends BaseActivity {

    /**
     * fragment集合
     */
    private final ArrayList<FragmentInfo> mTabs = new ArrayList<FragmentInfo>();
    /**
     * 当前显示的fragment
     */
    private FragmentInfo mLastInfo;
    /**
     * 当前显示的fragment在队列中的位置
     */
    protected int mCurrentTab = -1;
    /**
     * 界面布局id
     */
    public int mContainerId;
    /**
     * 是否已经添加到窗口
     */
    private boolean mAttached;
    /**
     * 当Activity Frament 数量为0的时候是否需要 销毁当前Activity 默认需要
     */
    private boolean isNeedFinishActivity = true;

    public void setContentResourseId(int containerId){
        mContainerId = containerId;
    }
    public void setNeedFinishActivity(boolean isNeedFinishActivity){
        this.isNeedFinishActivity = isNeedFinishActivity;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTag = getCurrentTabTag();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0;i<mTabs.size();i++){
            FragmentInfo info = mTabs.get(i);

            if (info.fragment != null && !info.fragment.isDetached()){
                if (info.tag.equals(currentTag)){
                    mLastInfo = info;
                }else{
                    ft.detach(info.fragment);
                }
            }
        }
        mAttached = true;
        setCurrentFragmentByTag(currentTag);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    /**
     * 获取当前fragment的tag标识
     * @return
     */
    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabs.size()) {
            return mTabs.get(mCurrentTab).tag;
        }
        return null;
    }

    /**
     * 设置当前界面要显示的fragment，未传参数
     * @param clss
     */
    public void setCurrentFragmentByClass(Class<? extends  BaseFragment> clss){
        setCurrentFragmentByClass(clss,null);
    }

    /**
     * 设置当前界面要显示的fragment，有传参数
     * @param clss fragment类名
     * @param args 参数
     */
    public void setCurrentFragmentByClass(Class<? extends  BaseFragment> clss,Bundle args){
        String tag = clss.getName();
        FragmentInfo info = null;
        boolean contains = false;
        //遍历队列，查看是否已经存在fragment
        for (int i = 0;i<mTabs.size();i++){
            FragmentInfo infoTmp = mTabs.get(i);
            if (infoTmp.tag.equals(tag)){
                contains = true;
                info = infoTmp;
                break;
            }
        }
        //队列不包含，创建并加入到队列
        if (!contains){
            info = new FragmentInfo(clss);
            mTabs.add(info);
        }
        //传入参数
        if (args != null){
            info.args = args;
        }
        //如果当前界面已经显示的是此fragment则不再往下走
        if (mLastInfo == info){
            return;
        }
        //如果此fragment已经加入到窗口
        if (mAttached){
            info.fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(info.tag);
            //非空并且未与界面脱离，则先与界面脱离
            if (info.fragment != null && !info.fragment.isDetached()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
        }
        //设置
        setCurrentFragmentByTag(tag);
    }

    /**
     * 通过tag设置当前要显示的fragment
     * @param tag
     */
    private void setCurrentFragmentByTag(String tag){
        int i ;
        for (i = 0;i<mTabs.size();i++){
            if (mTabs.get(i).tag.equals(tag)){
                setCurrentFragment(i);
                break;
            }
        }
    }

    private void setCurrentFragment(int index){
        if (index < 0 || index > mTabs.size()){
            return;
        }
        if (mCurrentTab == index){
            return;
        }
        mCurrentTab = index;
        final FragmentInfo  info = mTabs.get(mCurrentTab);
        doFragmentChange(info);
    }

    private void doFragmentChange(FragmentInfo info){
        if (info == null) {
            throw new IllegalStateException("No tab known for tag " + info);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mLastInfo != info ){
            if (mLastInfo != null){
                if(mLastInfo.fragment != null){
                    ft.detach(mLastInfo.fragment);
                }
            }
            if (info != null){
                if (info.fragment == null){
                    info.fragment = (BaseFragment) Fragment.instantiate(ManagerFragmentActivity.this, info.clss.getName(), info.args);
                    ft.addToBackStack(null);
                    ft.add(mContainerId,info.fragment,info.tag);
                }else{
                    ft.attach(info.fragment);
                }
                mLastInfo = info;
            }
        }
        ft.commit();
    }

    /**
     * activity因某些原因被回收或销毁时保存当前fragment信息
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mLastInfo != null){
            outState.putString("tag",mLastInfo.tag);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * activity恢复时获取前面保存的fragment显示
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tag = savedInstanceState.getString("tag");
        setCurrentFragmentByTag(tag);
    }

    /**
     * 系统回退按钮监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //回退fragment
            popBackStack();
        }
        //返回true表示事件不往下传递
        return true;
    }

    /***
     * 会退到上一个Fragment
     */
    public void popBackStack(){
        //队列中存在fragment
        if(mTabs.size()>0){
            //回退fragment
            getSupportFragmentManager().popBackStack();
            //将回退的fragment从队列中移除
            mTabs.remove(mTabs.size()-1);
            //回退一个fragment后设置下一个
            if (mTabs.size() == 0){
                mLastInfo = null;
                mCurrentTab = -1;
            }else{
                mLastInfo = mTabs.get(mTabs.size()-1);
                mCurrentTab = mTabs.size()-1;
            }
        }
        //是否需要结束Activity
        if (isNeedFinishActivity && mTabs.size()==0){
            finish();
            //我改掉了82行代码
        }
    }

    /**
     * fragment实体信息类
     */
    public class FragmentInfo {

        /**Fragment的Tag 可以通过FindFragmentByTag 查找Fragment*/
        private final String tag;
        /**Fragment 的类**/
        private final Class<?> clss;
        /**是否需要传递数据*/
        public Bundle args;
        /**Fragment*/
        private BaseFragment fragment;

        public FragmentInfo(Class<?> clss){
            this(clss,null);
        }

        public FragmentInfo(Class<?> clss, Bundle args){
            this(clss.getName(),clss,args);
        }

        public FragmentInfo(String tag, Class<?> clss, Bundle args) {
            this.tag = tag;
            this.clss = clss;
            this.args = args;
        }
    }
}
