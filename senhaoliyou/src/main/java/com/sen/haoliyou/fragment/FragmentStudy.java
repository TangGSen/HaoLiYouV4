package com.sen.haoliyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.ActLogin;
import com.sen.haoliyou.activity.DownloadManagerActivity;
import com.sen.haoliyou.activity.MainActivity;
import com.sen.haoliyou.activity.study.ActStudyDetail;
import com.sen.haoliyou.adapter.StudyRecyclerAdapter;
import com.sen.haoliyou.base.BaseFragment;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.EventComentCountForStudy;
import com.sen.haoliyou.mode.EventKillPositonStudy;
import com.sen.haoliyou.mode.EventLoveClickFromRescouce;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.mode.MyLessonHomeBean;
import com.sen.haoliyou.tools.AcountManager;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.tools.ResourcesUtils;
import com.sen.haoliyou.widget.BaseDialogCumstorTip;
import com.sen.haoliyou.widget.CustomerDialog;
import com.sen.haoliyou.widget.RecyleViewItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sen on 2016/3/3.
 */
public class FragmentStudy extends BaseFragment {

    private View rootView;
    @Bind(R.id.study_toolbar)
    Toolbar study_toolbar;
    @Bind(R.id.btn_down_manager)
    AppCompatImageView btn_down_manager;
    @Bind(R.id.btn_exit_app)
    AppCompatImageView btn_exit_app;
    @Bind(R.id.tv_lesson_theme)
    AppCompatTextView tv_lesson_theme;
    @Bind(R.id.study_lesson_recyclerview)
    RecyclerView study_lesson_recyclerview;
    @Bind(R.id.study_refresh_layout)
    MaterialRefreshLayout study_refresh_layout;
    @Bind(R.id.tip_null_data)
    FrameLayout tip_null_data;

    private List<LessonItemBean> mLesssListData;
    private List<LessonItemBean> allLesssListData;
    private StudyRecyclerAdapter adapter;
    private boolean isLoad = false;
    private boolean isReFlesh = false;

    private static final int GETDATA_ERROR = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //下拉刷新和加载更多的时候就不用diaogle
            if (!isReFlesh)
                DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), R.string.net_error_retry, Toast.LENGTH_SHORT).show();
                    break;

                case 1:

                    MyLessonHomeBean homeBeam = (MyLessonHomeBean) msg.obj;
                    mLesssListData = homeBeam.getCourselist();
                    // 当返回的数据为空的时候，那么就要显示这个
                    if (mLesssListData == null) {
                        setDataTip(true);
                        Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                    if (mLesssListData.size() == 0) {
                        setDataTip(true);
                        Toast.makeText(getContext(), R.string.has_null_data, Toast.LENGTH_SHORT).show();
                    }else{
                        setDataTip(false);
                    }

                    allLesssListData.addAll(mLesssListData);
                    mLesssListData.clear();

                    showRecyclerviewItemData(allLesssListData);

                    break;

            }
            return false;
        }
    });

    private void showRecyclerviewItemData(List<LessonItemBean> LesssListData) {
        if (adapter == null) {
            //创建并设置Adapter
            adapter = new StudyRecyclerAdapter(getActivity(), LesssListData);
            study_lesson_recyclerview.setAdapter(adapter);
            //设置Item增加、移除动画
            study_lesson_recyclerview.setItemAnimator(new DefaultItemAnimator());
            adapter.setOnItemClickListener(new StudyRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, LessonItemBean childItemBean) {
                    Intent intent = new Intent(getActivity(), ActStudyDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itemLessonBean", childItemBean);
                    bundle.putInt("itemPosition", position);
                    intent.putExtra("FragmentStudyBundle", bundle);
                    getActivity().startActivity(intent);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private  void setDataTip(boolean hasData){
        tip_null_data.setVisibility(!hasData?View.GONE:View.VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
        //当资源库点击收藏或者取消选课的时候
        if (isReFlesh && allLesssListData != null) {
            Log.e("sen", "资源库发生变化了");
            allLesssListData.clear();
            getDataFromNet(AcountManager.getAcountId());
            isReFlesh = false;
        }

    }

    @Override
    protected void dealAdaptationToPhone() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_study, container, false);
        ButterKnife.bind(this, rootView);
        settingRecyleView();

        if (savedInstanceState == null) {
            //去加载数据
            isLoad = true;
            isReFlesh = false;
        } else {
            isLoad = false;
            Log.e("sen", "老数据");
            allLesssListData = (List<LessonItemBean>) savedInstanceState.getSerializable("LesssListData");
            showRecyclerviewItemData(allLesssListData);
        }

        return rootView;
    }

    public void onEventMainThread(LessonItemBean childItemBean) {


    }

    public void onEvent(EventComentCountForStudy event) { //接收方法  在发关事件的线程接收
        //allLesssListData.size()>=event.getPosition();防止用户点击取消课程，防止这个会角标异常
        if (event != null && allLesssListData != null && allLesssListData.size() >= event.getPosition()) {
            LessonItemBean lessonItemBean = allLesssListData.get(event.getPosition());
            int count = (Integer.parseInt(lessonItemBean.getComment()) + event.getSucessCount());
            lessonItemBean.setComment(count + "");
            adapter.notifyItemChanged(event.getPosition());
        }
    }

    public void onEvent(EventLoveClickFromRescouce event) { //接收方法  在发关事件的线程接收
        isReFlesh = true;
    }

    public void onEvent(EventKillPositonStudy event) { //接收方法  在发关事件的线程接收
        adapter.removeItem(event.getPosition());
        if (allLesssListData.size()==0){
            setDataTip(true);
        }
    }


    private void settingRecyleView() {
        mLesssListData = new ArrayList<>();
        mLesssListData.clear();
        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(getActivity());
        study_lesson_recyclerview.setLayoutManager(linearnLayoutManager);
//        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        study_lesson_recyclerview.setHasFixedSize(true);
        study_lesson_recyclerview.setItemAnimator(new DefaultItemAnimator());
        study_lesson_recyclerview.addItemDecoration(new RecyleViewItemDecoration(getContext(), R.drawable.shape_recycle_item_decoration));
        study_refresh_layout.setLoadMore(false);
        study_refresh_layout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        isReFlesh = true;
                        allLesssListData.clear();
                        getStudyData();
                        study_refresh_layout.finishRefresh();
                        isReFlesh = false;
                    }
                }, 1000);

            }
        });

    }

    @Override
    protected void initData() {
        allLesssListData = new ArrayList<>();
        if (isLoad) {
            getStudyData();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LesssListData", (Serializable) allLesssListData);
    }

    private void getStudyData() {
        boolean hasNet = NetUtil.isNetworkConnected(getActivity());
        if (hasNet) {
            getDataFromNet(AcountManager.getAcountId());
        } else {
            Toast.makeText(getContext(), R.string.has_not_net, Toast.LENGTH_SHORT).show();
        }
    }


    private void getDataFromNet(String userid) {
        //下拉刷新和加载更多就不用show
        if (!isReFlesh)
            DialogUtils.showDialog(getActivity(), ResourcesUtils.getResString(getContext(), R.string.dialog_show_wait));
        String url = Constants.PATH + Constants.PATH_AllOfMyCourses;
        OkHttpUtils.post()
                .url(url)
                .addParams("userid", userid)
                .build()
                .execute(new Callback<MyLessonHomeBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public MyLessonHomeBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        MyLessonHomeBean lesssonBean = JSON.parseObject(string, MyLessonHomeBean.class);
                        return lesssonBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);
                    }

                    @Override
                    public void onResponse(MyLessonHomeBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = 1;
                        mHandler.sendMessage(message);

                    }
                });
    }
//


    //点击事件
    @OnClick(R.id.btn_down_manager)
    public void downloadManager() {
        Intent intent = new Intent(getActivity(), DownloadManagerActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.btn_exit_app)
    public void exitAcount() {
        BaseDialogCumstorTip.getDefault().showTwoBtnDialog(new BaseDialogCumstorTip.DialogButtonOnclickLinster() {
            @Override
            public void onLeftButtonClick(CustomerDialog dialog) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AcountManager.deleteAcount();
                Intent intent = new Intent(getContext(), ActLogin.class);
                intent.putExtra("Frome", "changeAcount");
                startActivity(intent);
                ((MainActivity) getActivity()).killAll();
            }

            @Override
            public void onRigthButtonClick(CustomerDialog dialog) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }, getContext(), "退出提示", "是否注销该账号?", "确定", "取消", true, true);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AnimateFirstDisplayListener.displayedImages.clear();
    }


}
