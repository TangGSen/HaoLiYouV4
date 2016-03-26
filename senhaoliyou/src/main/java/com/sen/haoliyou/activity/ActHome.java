package com.sen.haoliyou.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.tools.AcountManager;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.widget.CustomerDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ActHome extends BaseActivity {

    @Bind(R.id.act_home_study)
    AppCompatImageView act_home_study;
    @Bind(R.id.act_home_test)
    AppCompatImageView act_home_test;
    @Bind(R.id.act_home_resouce)
    AppCompatImageView act_home_resouce;
    @Bind(R.id.act_home_coach)
    AppCompatImageView act_home_coach;


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_act_home);
        ButterKnife.bind(this);
        getVeraon();
        getVersonNet();
    }

    private void getVersonNet() {
        String url = Constants.PATH + Constants.VERSION;
        OkHttpUtils.post()
                .url(url)
                .addParams("version", 2+"")
                .build()
                .execute(new Callback<Boolean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public Boolean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        Boolean success = JSON.parseObject(string).getBoolean("success");
                        String versionNet = JSON.parseObject(string).getString("version");
                        if (success && !TextUtils.isEmpty(version)){
                            if (!version.equals(versionNet)){
                                return true;
                            }
                        }
                       return false;
                    }

                    @Override
                    public void onError(Call call, Exception e) {


                    }

                    @Override
                    public void onResponse(Boolean flag) {
                        if (flag){
                            showUpdateApk();
                        }
                    }
                });
    }

    private void showUpdateApk() {
        final CustomerDialog dialog = new CustomerDialog(ActHome.this, 280, 150, R.layout.customer_tip_update_dialog, R.style.Theme_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AppCompatTextView txt_update_message = (AppCompatTextView) dialog.findViewById(R.id.txt_update_message);
        txt_update_message.setText("新版本已发布，请更新！");
        AppCompatButton update = (AppCompatButton) dialog.findViewById(R.id.update);
        update.setText("立即更新");
        AppCompatButton dismiss_dialog = (AppCompatButton) dialog.findViewById(R.id.update_dismiss_dialog);
        dismiss_dialog.setText("下次再说");
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                Intent intent = new Intent(ActHome.this,NewVersionActivity.class);
                String newversion = Constants.APK_PATH;
                intent.putExtra("newversion", newversion);
                startActivity(intent);
            }
        });
        dismiss_dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    private String version;
    /**
     * 获取项目的版本号
     **/
    private void getVeraon() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.act_home_study)
    public void studyPage() {
        jumpToMain(0);
    }



    @OnClick(R.id.act_home_test)
    public void testPage() {
        jumpToMain(1);
    }

    @OnClick(R.id.act_home_resouce)
    public void resoucePage() {
        jumpToMain(2);
    }

    @OnClick(R.id.act_home_coach)
    public void coachPage() {
        Intent intent = new Intent(ActHome.this,DevelopingActivity.class);
        startActivity(intent);
    }


    public void jumpToMain(int position){
       if (AcountManager.isAcountExist()){
           Intent intent = new Intent(ActHome.this,MainActivity.class);
           intent.putExtra("position",position);
           startActivity(intent);
       }else{
           Intent intent = new Intent(ActHome.this,ActLogin.class);
           intent.putExtra("position",position);
           intent.putExtra("Frome","ActHome");
           startActivity(intent);
       }

    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if((System.currentTimeMillis()-exitTime) >2000)  {
                Toast.makeText(ActHome.this,R.string.two_down_back_exitapp,Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                exitApp();
            }
        }
        return true;
    }


}
