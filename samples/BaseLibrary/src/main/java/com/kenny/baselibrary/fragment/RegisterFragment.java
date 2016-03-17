package com.kenny.baselibrary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenny.baselibrary.R;


/**
 * 屏幕百分比自动适配案例展示界面
 * @author kenny
 * @time 2015/12/21 22:37
 */
public class RegisterFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_register, container,false);
	}

}
