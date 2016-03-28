package com.sen.haoliyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.study.ActResoucesStudyDetail;
import com.sen.haoliyou.adapter.StudyRecyclerAdapter;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.mode.LessonListHomeBean;
import com.sen.haoliyou.tools.AcountManager;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.tools.ToastUtils;
import com.sen.haoliyou.widget.RecyleViewItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ActSearchLesson extends BaseActivity {
    @Bind(R.id.recycle_search)
    RecyclerView recycle_search;
    @Bind(R.id.bt_search_click)
    AppCompatButton bt_search_click;
    @Bind(R.id.et_search)
    AppCompatEditText et_search;
    @Bind(R.id.btn_back_search)
    AppCompatTextView btn_back_search;

    private StudyRecyclerAdapter adapter;

    private static final int GETDATA_ERROR = 0;
    private static final int SHOW_DATA = 1;
    private List<LessonItemBean> mLesssListData;
    private List<LessonItemBean> allLesssListData;

    //延迟去搜索，防止用户快速多点
    private static final int DELAY_TO_SEARCH = 3;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:

                    Toast.makeText(ActSearchLesson.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    LessonListHomeBean homeBeam = (LessonListHomeBean) msg.obj;
                    if (homeBeam==null){
                        return false;
                    }
                    mLesssListData = homeBeam.getCoursesList();
                    if (mLesssListData==null){
                        return false;
                    }
                    // 当返回的数据为空的时候，那么就要显示这个

                    if (mLesssListData.size() == 0) {
                        ToastUtils.showTextToast(ActSearchLesson.this,"没有数据，换关键字搜搜");
                    } else {
                        allLesssListData.addAll(mLesssListData);
                        mLesssListData.clear();
                        showRecyclerviewItemData(allLesssListData);

                    }


                    break;
                case 3:
                    search();
                    break;


            }

            return false;
        }
    });

    private void showRecyclerviewItemData(List<LessonItemBean> LesssListData) {
        if (adapter == null) {
            //创建并设置Adapter
            adapter = new StudyRecyclerAdapter(ActSearchLesson.this, LesssListData);
            recycle_search.setAdapter(adapter);
            adapter.setOnItemClickListener(new StudyRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, LessonItemBean childItemBean) {
                    Intent intent = new Intent(ActSearchLesson.this, ActResoucesStudyDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itemLessonBean", childItemBean);
                    bundle.putInt("itemPosition", position);
                    intent.putExtra("FragmentStudyBundle", bundle);
                    startActivity(intent);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        settingRecyleView();
    }

    private void settingRecyleView() {
        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(this);
        linearnLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_search.setLayoutManager(linearnLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recycle_search.setHasFixedSize(true);

//        添加分割线
        recycle_search.addItemDecoration(new RecyleViewItemDecoration(this, R.drawable.shape_recycle_item_decoration));


    }

    private void getSearchList(String search) {
        if (!NetUtil.isNetworkConnected(this)) {
            DialogUtils.closeDialog();
            Toast.makeText(ActSearchLesson.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showDialog(ActSearchLesson.this, "请稍后");
        String url = Constants.PATH + Constants.PATH_Repository;
        OkHttpUtils.post()
                .url(url)
                .addParams("userid", AcountManager.getAcountId())
                .addParams("search", search)
                .build()
                .execute(new Callback<LessonListHomeBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public LessonListHomeBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        LessonListHomeBean lesssonBean = JSON.parseObject(string, LessonListHomeBean.class);
                        return lesssonBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);


                    }

                    @Override
                    public void onResponse(LessonListHomeBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = SHOW_DATA;
                        mHandler.sendMessage(message);

                    }
                });
    }

    @OnClick(R.id.bt_search_click)
    public void searchLesson() {
        allLesssListData.clear();
        mHandler.removeMessages(DELAY_TO_SEARCH);
        //绑定一个msg，内容为接下来需要的button的ID，
        Message msg = Message.obtain();
        msg.what = DELAY_TO_SEARCH;
        //发送消息间隔1秒
        mHandler.sendMessageDelayed(msg, 200);


    }

    private void search() {
        String content = et_search.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showTextToast(ActSearchLesson.this,"请输入内容");
        } else {
            getSearchList(content);
        }
    }

    @OnClick(R.id.btn_back_search)
    public void back() {
       finish();

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        allLesssListData = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
