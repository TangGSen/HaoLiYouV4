package com.sen.haoliyou.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.sen.haoliyou.R;

/**
 * Created by Administrator on 2016/3/21.
 */
public class BaseDialogCumstorTip {
    private  DialogButtonOnclickLinster mDialogListener = null;
    private CustomerDialog btnOneDialog=null;

    private volatile static BaseDialogCumstorTip single;

    private BaseDialogCumstorTip() {
    }

    public static BaseDialogCumstorTip getDefault() {
        if (single == null) {
            synchronized (BaseDialogCumstorTip.class) {
                if (single == null) {
                    single = new BaseDialogCumstorTip();
                }
            }
        }
        return single;
    }

    public interface DialogButtonOnclickLinster {
        void onLeftButtonClick(CustomerDialog dialog);
        void onRigthButtonClick(CustomerDialog dialog);
    }



    public  void showTwoBtnDialog(DialogButtonOnclickLinster dialogListener, Context context, String title, String msg, String leftBtnStr, String rightBtnStrl, boolean isTitleShow, boolean isMsgShow) {
        this.mDialogListener = dialogListener;
        final CustomerDialog dialog = new CustomerDialog(context, 250, 150, R.layout.base_twobtn_customer_tip_dialog, R.style.Theme_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AppCompatTextView txt_title = (AppCompatTextView) dialog.findViewById(R.id.txt_title);
        AppCompatTextView txt_message = (AppCompatTextView) dialog.findViewById(R.id.txt_message);

        AppCompatButton btn_ok = (AppCompatButton) dialog.findViewById(R.id.btn_ok);
        AppCompatButton btn_dismiss = (AppCompatButton) dialog.findViewById(R.id.btn_dismiss);

        if (isTitleShow) {
            txt_title.setText(title);
        } else {
            txt_title.setVisibility(View.GONE);
        }
        if (isMsgShow) {
            txt_message.setText(msg);
        } else {
            txt_message.setVisibility(View.GONE);
        }
        btn_ok.setText(leftBtnStr);
        btn_dismiss.setText(rightBtnStrl);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mDialogListener != null) {
                    mDialogListener.onLeftButtonClick(dialog);
                }

            }
        });
        btn_dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mDialogListener != null) {
                    mDialogListener.onRigthButtonClick(dialog);
                }

            }
        });

    }public  void showOneBtnDilog(DialogButtonOnclickLinster dialogListener,int width,int height, Context context, String title, String msg, String leftBtnStr, boolean isTitleShow, boolean isMsgShow) {
        this.mDialogListener = dialogListener;
        btnOneDialog= new CustomerDialog(context, width, height, R.layout.base_onebtn_customer_tip_dialog, R.style.Theme_dialog);
        btnOneDialog.setCanceledOnTouchOutside(false);
        btnOneDialog.show();
        btnOneDialog.setCancelable(false);
        AppCompatTextView txt_title = (AppCompatTextView) btnOneDialog.findViewById(R.id.txt_onebtn_title);
        AppCompatTextView txt_message = (AppCompatTextView) btnOneDialog.findViewById(R.id.txt_onebtn_message);
        AppCompatButton btn_ok = (AppCompatButton) btnOneDialog.findViewById(R.id.onebtn_ok);

        if (isTitleShow) {
            txt_title.setText(title);
        } else {
            txt_title.setVisibility(View.GONE);
        }
        if (isMsgShow) {
            txt_message.setText(msg);
        } else {
            txt_message.setVisibility(View.GONE);
        }
        btn_ok.setText(leftBtnStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mDialogListener != null) {
                    mDialogListener.onLeftButtonClick(btnOneDialog);
                }
            }
        });


    }

    public boolean isShowOneDialog(){
        if (btnOneDialog != null && btnOneDialog.isShowing()) {
            return true;
        }
        return false;
    }


}
