package com.kenny.baselibrary.view.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author kenny
 * @time 2015/6/21 22:46
 */
public class DialogValue implements Serializable {

    public static final String TAG = "DialogValue";

    /**
     *
     */
    public boolean showProgress = true;
    /***
     * 标题
     */
    public String title="";

    /***
     * 内容
     */
    public String message="";

    /***
     * btn值
     */
    public List<DialogBtnValue> btns = new ArrayList<DialogBtnValue>();

    private FragmentManager fm;

    public DialogValue(FragmentManager fm) {
        this.fm = fm;
    }

    public DialogValue setTitle(String title){
        this.title = title;
        return  this;
    }

    public DialogValue setMessage(String message){
        this.message = message;
        showProgress = false;
        return  this;
    }

    public DialogValue addButton(String name){
        return  addButton(name,null);
    }

    public DialogValue showProgress(){
        showProgress = true;
        return this;
    }

    public DialogValue addButton(String name,DialogListener dialogListener){
        DialogBtnValue dialogBtnValue = new DialogBtnValue();
        dialogBtnValue.name = name;
        dialogBtnValue.listener = dialogListener;
        btns.add(dialogBtnValue);
        return  this;
    }


    public DialogFragment showConfirmAndCancle(){
        CustomDialog dialog = new CustomDialog();
        dialog.setDialogValue(this);
        dialog.show(fm, dialog.toString());
        return  dialog;
    }



    public DialogFragment showMessage(String message){
        CustomDialog dialog = new CustomDialog();
        this.message = message;
        showProgress  = false;
        dialog.setDialogValue(this);
        dialog.show(fm,dialog.toString());
        return  dialog;
    }


    public class  DialogBtnValue implements Serializable {
        public String name;

        public DialogListener listener;
    }


}
