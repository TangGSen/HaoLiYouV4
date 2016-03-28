package com.sen.haoliyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.mode.UserInfoBean;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.tools.ResourcesUtils;
import com.sen.haoliyou.tools.SenApplication;
import com.sen.haoliyou.tools.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ActLogin extends BaseActivity {
    @Bind(R.id.login_logo)
    AppCompatImageView login_logo;
    @Bind(R.id.et_username)
    AppCompatEditText et_username;
    @Bind(R.id.et_password)
    AppCompatEditText et_password;
    @Bind(R.id.btn_login)
    AppCompatButton btn_login;

    private boolean isChangeCount = false;

    private static final int GETDATA_ERROR = 0;
    private static final int SHOW_DATA = 1;
    private int mianPosition;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DialogUtils.closeDialog();
                    Toast.makeText(ActLogin.this, R.string.net_error_retry, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    DialogUtils.closeDialog();
                    UserInfoBean userInfo = (UserInfoBean) msg.obj;
                    if ("true".equals(userInfo.getSuccess())) {
                        //不保存用户密码
                        userInfo.setPassword("");
                        userInfo.save();
                        SenApplication.getInstance().setUserId(userInfo.getUserid());
                        if (isChangeCount){
                            Intent intent = new Intent(ActLogin.this,ActHome.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ActLogin.this,MainActivity.class);
                            intent.putExtra("position",mianPosition);
                            startActivity(intent);

                        }

                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right);

                    }else{
                        Toast.makeText(ActLogin.this, R.string.str_login_error_check, Toast.LENGTH_SHORT).show();
                    }


                    break;


            }
            return false;
        }
    });


    @Override
    protected void init() {
        super.init();
        Intent intent =getIntent();
        String frome = intent.getStringExtra("Frome");
        if ("changeAcount".equals(frome)){
            isChangeCount =true;
        }else{
            isChangeCount = false;
            mianPosition = intent.getIntExtra("position",0);
        }


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (TextUtils.isEmpty(et_username.getText())) {
            Toast.makeText(ActLogin.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_password.getText())) {
            Toast.makeText(ActLogin.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = et_username.getText().toString();
        String password = et_password.getText().toString();

        sumbmitUser(userName, password);


    }

    private void sumbmitUser(String userName, String password) {
        if (!NetUtil.isNetworkConnected(this)) {
            Toast.makeText(ActLogin.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showDialog(this, "请稍后");
        String url = Constants.PATH + Constants.PATH_LOGIN;
        OkHttpUtils.post()
                .url(url)
                .addParams("username", userName)
                .addParams("password", password)
                .build()
                .execute(new Callback<UserInfoBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public UserInfoBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        UserInfoBean userInfo = JSON.parseObject(string, UserInfoBean.class);
                        return userInfo;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                          mHandler.sendEmptyMessage(GETDATA_ERROR);


                    }

                    @Override
                    public void onResponse(UserInfoBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = SHOW_DATA;
                        mHandler.sendMessage(message);

                    }
                });
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if((System.currentTimeMillis()-exitTime) >2000)  {
                ToastUtils.showTextToast(ActLogin.this, ResourcesUtils.getResString(ActLogin.this,R.string.two_down_back_exitapp));
                exitTime = System.currentTimeMillis();
            }else {
                exitApp();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        //解决handler 内存溢出
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
