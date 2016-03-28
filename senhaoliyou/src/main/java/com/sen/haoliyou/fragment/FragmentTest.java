package com.sen.haoliyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.exam.ActExamDetail;
import com.sen.haoliyou.adapter.ExamListAdapter;
import com.sen.haoliyou.base.BaseFragment;
import com.sen.haoliyou.exam.ExamHomeBean;
import com.sen.haoliyou.exam.ExamItemBean;
import com.sen.haoliyou.mode.EventSubmitAnswerSucess;
import com.sen.haoliyou.tools.AcountManager;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.widget.RecyleViewItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sen on 2016/3/3.
 */
public class FragmentTest extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener {
    private View rootView;
    @Bind(R.id.test_recylerview)
    RecyclerView test_recylerview;
    @Bind(R.id.test_swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;


    private static final int GETDATA_ERROR = 0;
    private boolean isLoad = false;
    private boolean isReFlesh = false;
    private List<ExamItemBean> examItemBeanList;
    private List<ExamItemBean> allExamItemBeanList;
    private ExamListAdapter examAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //下拉刷新和加载更多的时候就不用diaogle
            if (!isReFlesh)
                DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:

                    Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    ExamHomeBean homeBean = (ExamHomeBean) msg.obj;
                    // 当返回的数据为空的时候，那么就要显示这个
                    if (homeBean == null) {
                        Toast.makeText(getContext(), "请求数据失败，刷新一下", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    examItemBeanList = homeBean.getExamList();
                    if (examItemBeanList == null) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (examItemBeanList.size() == 0) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    allExamItemBeanList.clear();
                    allExamItemBeanList.addAll(examItemBeanList);
                    examItemBeanList.clear();
                    showExamData(allExamItemBeanList);


                    break;
            }
            return false;
        }
    });

    private void showExamData(List<ExamItemBean> examItemBeanList) {


        if (examAdapter == null) {
            examAdapter = new ExamListAdapter(getActivity(), examItemBeanList);
            test_recylerview.setAdapter(examAdapter);
        } else {
            examAdapter.notifyDataSetChanged();
        }

        examAdapter.setOnItemClickListener(new ExamListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ExamItemBean childItemBean) {
                Intent intent = new Intent(getActivity(), ActExamDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ExamItemBean", childItemBean);
                intent.putExtra("ExamItemBeanBundle", bundle);
                getActivity().startActivity(intent);
            }
        });


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
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, rootView);
        settingRecyleView();

        if (savedInstanceState == null) {
            //去加载数据
            isLoad = true;
        } else {
            isLoad = false;
            Log.e("sen", "老数据");
            allExamItemBeanList = (List<ExamItemBean>) savedInstanceState.getSerializable("ExamListData");
            showExamData(allExamItemBeanList);
        }
        return rootView;
    }

    private void settingRecyleView() {

        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(getActivity());
        test_recylerview.setLayoutManager(linearnLayoutManager);
//        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        test_recylerview.setHasFixedSize(true);

        test_recylerview.addItemDecoration(new RecyleViewItemDecoration(getContext(), R.drawable.shape_recycle_item_decoration));
        swipe_refresh_widget.setColorSchemeResources(R.color.theme_color,R.color.theme_color);
        swipe_refresh_widget.setOnRefreshListener(this);
    }

    public void onEvent(EventSubmitAnswerSucess event) {
       /* if (allExamItemBeanList != null) {
            allExamItemBeanList.clear();
        }else{
            allExamItemBeanList = new ArrayList<>();
        }*/
        getExamListData();
    }


    @Override
    protected void initData() {
        allExamItemBeanList = new ArrayList<>();
        if (isLoad) {
            getExamListData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ExamListData", (Serializable) allExamItemBeanList);
    }

    private void getExamListData() {
        if (!NetUtil.isNetworkConnected(getActivity())) {
            Toast.makeText(getContext(), R.string.has_not_net, Toast.LENGTH_SHORT).show();
            return;
        }
        //下拉刷新和加载更多就不用show
        if (!isReFlesh)
            DialogUtils.showDialog(getActivity(), "请稍等");
        String url = Constants.PATH + Constants.GETEXAM;
        OkHttpUtils.post()
                .url(url)
                .addParams("userid", AcountManager.getAcountId())
                .build()
                .execute(new Callback<ExamHomeBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public ExamHomeBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        ExamHomeBean lesssonBean = JSON.parseObject(string, ExamHomeBean.class);
                        return lesssonBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);
                    }

                    @Override
                    public void onResponse(ExamHomeBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = 1;
                        mHandler.sendMessage(message);

                    }
                });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                isReFlesh = true;
                getExamListData();
                swipe_refresh_widget.setRefreshing(false);
                isReFlesh = false;
            }
        }, 1000);
    }
}
