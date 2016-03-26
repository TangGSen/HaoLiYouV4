package com.sen.haoliyou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.sen.haoliyou.R;
import com.sen.haoliyou.widget.CustomerDialog;

public class DevelopingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developing);

        showDevelopingActivity();
    }

    private void showDevelopingActivity() {
        final CustomerDialog dialog = new CustomerDialog(DevelopingActivity.this, 260, 150, R.layout.customer_tip_developing_dialog, R.style.Theme_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AppCompatButton exit_btn = (AppCompatButton) dialog.findViewById(R.id.develop_exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
               finish();
            }
        });

    }
}
