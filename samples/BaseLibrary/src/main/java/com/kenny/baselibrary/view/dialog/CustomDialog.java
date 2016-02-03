package com.kenny.baselibrary.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenny.baselibrary.R;


/**
 * Created by kenny on 15/5/22.
 */
public class CustomDialog extends DialogFragment {

    private View view;

    private TextView tv_title;

    private TextView tv_content;

    private Button btn_sure;

    private Button btn_cancle;

    private DialogValue dialogValue;

    private DialogListener sureListener;

    private DialogListener cancleListener;

    ProgressWheel pw_two;

    private LinearLayout linear_progress_content;

    public void setDialogValue(DialogValue dialogValue){
        this.dialogValue = dialogValue;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_test,null);

        Dialog dialog = new Dialog(getActivity(),R.style.alert_dialog);
        dialog.setContentView(view);
        initView(view);
        setListener();

        try {
            initData();
        }catch (Exception e){e.printStackTrace();}

        return dialog;
    }

    public void initView(View view){
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_content = (TextView)view.findViewById(R.id.tv_cotent);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        linear_progress_content  = (LinearLayout) view.findViewById(R.id.linear_progress_content);
        pw_two = (ProgressWheel) view.findViewById(R.id.progressBarTwo);

        pw_two.spin();
    }

    public void setListener(){
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureListener != null){
                    sureListener.listener(CustomDialog.this);
                }else {
                    dismiss();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancleListener != null){
                    cancleListener.listener(CustomDialog.this);
                }else {
                    dismiss();
                }
            }
        });
    }

    public void initData(){
        if (dialogValue.title.equals("")){
            tv_title.setVisibility(View.GONE);
        } else{
            tv_title.setText(dialogValue.title);
        }

        if (dialogValue.message.equals("")){
            tv_content.setVisibility(View.GONE);
        }else {
            tv_content.setText(dialogValue.message);
        }

        if (dialogValue.showProgress){
            linear_progress_content.setVisibility(View.VISIBLE);
        }else{
            linear_progress_content.setVisibility(View.GONE);
        }

        if (dialogValue.btns.size() == 0){
            btn_sure.setVisibility(View.GONE);
            btn_cancle.setVisibility(View.GONE);
            return;
        }

        if (dialogValue.btns.get(0)!= null){
            DialogValue.DialogBtnValue dialogBtnValue = dialogValue.btns.get(0);
            btn_sure.setText(dialogBtnValue.name);
            sureListener = dialogBtnValue.listener;
        }

        if (dialogValue.btns.size() >1){
            DialogValue.DialogBtnValue dialogBtnValue = dialogValue.btns.get(1);
            btn_cancle.setText(dialogBtnValue.name);
            cancleListener = dialogBtnValue.listener;
        }else{
            btn_cancle.setVisibility(View.GONE);
        }
    }
}
