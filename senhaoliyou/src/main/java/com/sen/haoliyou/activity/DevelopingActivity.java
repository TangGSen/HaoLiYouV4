package com.sen.haoliyou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sen.haoliyou.R;
import com.sen.haoliyou.widget.BaseDialogCumstorTip;
import com.sen.haoliyou.widget.CustomerDialog;

public class DevelopingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developing);

        showDevelopingActivity();
    }

    private void showDevelopingActivity() {
        BaseDialogCumstorTip.getDefault().showOneBtnDilog(new BaseDialogCumstorTip.DialogButtonOnclickLinster() {
            @Override
            public void onLeftButtonClick(CustomerDialog dialog) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onRigthButtonClick(CustomerDialog dialog) {

            }
        },260,150,DevelopingActivity.this,"温馨提示","该模块正在开发中，敬请后续关注，谢谢！","关闭",true,true);

    }
}
