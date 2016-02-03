package com.kenny.baselibrary.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kenny.baselibrary.R;
import com.kenny.baselibrary.activity.AutoLayoutActivity;
import com.kenny.baselibrary.activity.MainActivity;
import com.kenny.baselibrary.activity.NetWorkActivity;
import com.kenny.baselibrary.activity.ORMActivity;

public class TabFragment extends Fragment{
    private String mTitle = "Default";

    public static final String TITLE = "title";

    private Button bt_orm;
    private Button bt_network;
    private Button bt_auto_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }

        TextView tv = new TextView(getActivity());
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);

//        bt_orm = (Button) this.findViewById(R.id.bt_orm);
//        bt_network = (Button) this.findViewById(R.id.bt_network);
//        bt_auto_layout = (Button) this.findViewById(R.id.bt_auto_layout);
//        bt_orm.setOnClickListener(this);
//        bt_network.setOnClickListener(this);
//        bt_auto_layout.setOnClickListener(this);
        return tv;

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_orm:
//                startActivity(new Intent(getActivity(), ORMActivity.class));
//                break;
//            case R.id.bt_network:
//                startActivity(new Intent(getActivity(), NetWorkActivity.class));
//                break;
//            case R.id.bt_auto_layout:
//                startActivity(new Intent(getActivity(), AutoLayoutActivity.class));
//                break;
//        }
//    }
}
