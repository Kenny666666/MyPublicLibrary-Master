package com.kenny.baselibrary.activity;

import android.os.Bundle;
import android.os.Message;

import com.kenny.baselibrary.BaseActivity;
import com.kenny.baselibrary.R;
import com.kenny.baselibrary.dao.DefectDao;
import com.kenny.baselibrary.entity.DefectModel;
import com.kenny.baselibrary.utils.common.T;
import com.kenny.baselibrary.utils.common.Utility;

import java.util.List;

/**
 * ORM数据库框架测试界面
 * @author kenny
 * @time 2016/1/31 22:31
 */
public class ORMActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orm_test);

        DefectDao dao = new DefectDao(this);
        List<?> list = dao.queryAllDefect();
        if (!Utility.isEmpty(list)){
            DefectModel defectModel = (DefectModel) list.get(0);
            T.showShort(this,defectModel.toString());
        }
    }

    @Override
    protected void handler(Message msg) {

    }

}
