package com.kenny.baselibrary;

/**
 * 实现fragment懒加载
 * 如界面中的fragment需要先加载完成view后，再加载数据，则可继承LazyFragment就可以实现
 * Created by kenny on 2015/6/21.
 */
public abstract class LazyFragment extends BaseFragment {

    protected boolean mIsVisible;

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }

    protected void onInvisible(){}

    protected abstract void lazyLoad();
}