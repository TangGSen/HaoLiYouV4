package com.sen.haoliyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.ActSearchLesson;
import com.sen.haoliyou.activity.study.ActRepositoryDetail;
import com.sen.haoliyou.adapter.LessonCatalogAdapter;
import com.sen.haoliyou.adapter.StudyRecyclerAdapter;
import com.sen.haoliyou.base.BaseFragment;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.EventComentCountForResouce;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.mode.ResouceType;
import com.sen.haoliyou.mode.ResourceLessonHomeBean;
import com.sen.haoliyou.mode.ResourseBean;
import com.sen.haoliyou.mode.ResourseKindBean;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.widget.RecyleViewItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

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
public class FragmentRepository extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View rootView;
    @Bind(R.id.resourse_et_search)
    AppCompatTextView resourse_et_search;
    @Bind(R.id.resourse_listview)
    RecyclerView resourse_listview;
    @Bind(R.id.resouce_swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;
    private boolean isLoad = false;
    StudyRecyclerAdapter studyRecyclerAdapter;

    private List<ResourseKindBean> allResourseKindBean;

    private List<LessonItemBean> allCourseList;

    private boolean isReFlesh = false;
    private ResouceType resouceType;


    private static final int GETDATA_ERROR = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!isReFlesh)
                DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:
                    Toast.makeText(getContext(), "网络异常，刷新一下", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    resouceType = (ResouceType) msg.obj;
                    // 当返回的数据为空的时候，那么就要显示这个
                    Log.e("sen", resouceType.getType() + "");
                    if (resouceType == null) {
                        Toast.makeText(getContext(), "请求数据失败，请刷新", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    //显示目录
                    if (resouceType.getType() == 1) {
                        showLessonCatalog(resouceType);

                    } else if (resouceType.getType() == 2) {
                        showLessonData(resouceType);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private void showLessonData(ResouceType resouceType) {
        ResourceLessonHomeBean resourseLesson = resouceType.getLessonHomeBean();
        if (resourseLesson == null) {
            Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LessonItemBean> courseList = resourseLesson.getCourseslist();

        if (courseList == null) {
            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        if (courseList.size() == 0) {
            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        allCourseList.clear();
        allCourseList.addAll(courseList);
        courseList.clear();
        studyRecyclerAdapter = new StudyRecyclerAdapter(getActivity(), allCourseList);
        resourse_listview.setAdapter(studyRecyclerAdapter);
    }

    private void showLessonCatalog(ResouceType resouceType) {
        ResourseBean resourse = resouceType.getResourseBean();
        if (resourse == null) {
            return;
        }
        List<ResourseKindBean> cataLogList = resourse.getKnoeledgeList();

        if (cataLogList == null) {
            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cataLogList.size() == 0) {
            Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        allResourseKindBean.clear();
        allResourseKindBean.addAll(cataLogList);
        cataLogList.clear();
        LessonCatalogAdapter cataLogAdapter = new LessonCatalogAdapter(getActivity(), allResourseKindBean);
        resourse_listview.setAdapter(cataLogAdapter);

        cataLogAdapter.setOnItemClickListener(new LessonCatalogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ResourseKindBean childItemBean) {
                Intent intent = new Intent(getActivity(), ActRepositoryDetail.class);
                intent.putExtra("catalogId", childItemBean.getId());
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ResouceType", resouceType);
    }

    @Override
    protected void dealAdaptationToPhone() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_resourse, container, false);
        ButterKnife.bind(this, rootView);
        settingRecyleView();

        if (savedInstanceState == null) {
            //去加载数据
            isLoad = true;
        } else {
            isLoad = false;
            Log.e("sen", "老数据");
            resouceType = (ResouceType) savedInstanceState.getSerializable("ResouceType");
            Message message = Message.obtain();
            message.what = 1;
            message.obj = resouceType;
            mHandler.sendMessage(message);
        }


        return rootView;
    }

    private void settingRecyleView() {

        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(getActivity());
        resourse_listview.setLayoutManager(linearnLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        resourse_listview.setHasFixedSize(true);
//            添加分割线
        resourse_listview.addItemDecoration(new RecyleViewItemDecoration(getContext(), R.drawable.shape_recycle_item_decoration));
        swipe_refresh_widget.setColorSchemeResources(R.color.theme_color,R.color.theme_color);
        swipe_refresh_widget.setOnRefreshListener(this);
    }

    public void onEvent(EventComentCountForResouce event) { //接收方法  在发关事件的线程接收
        if (event != null && allCourseList!=null && studyRecyclerAdapter!=null) {
            LessonItemBean lessonItemBean = allCourseList.get(event.getPosition());
            int count = (Integer.parseInt(lessonItemBean.getComment()) + event.getSucessCount());
            lessonItemBean.setComment(count + "");
            studyRecyclerAdapter.notifyItemChanged(event.getPosition());
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        AnimateFirstDisplayListener.displayedImages.clear();
        super.onDestroy();
    }

    private void getResourseNet() {

        if (!NetUtil.isNetworkConnected(getActivity())) {
            Toast.makeText(getContext(), R.string.has_not_net, Toast.LENGTH_SHORT).show();
            return;
        }
        //下拉刷新和加载更多就不用show
        if (!isReFlesh)
            DialogUtils.showDialog(getActivity(), "请稍等");
        String url = Constants.PATH + Constants.PATH_Repository;
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new Callback<ResouceType>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public ResouceType parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        Boolean success = JSON.parseObject(string).getBoolean("success");
                        ResouceType resouceType = new ResouceType();
                        if (success) {
                            int type = JSON.parseObject(string).getInteger("type");
                            if (type == 1) {
                                ResourseBean lesssonBean = JSON.parseObject(string, ResourseBean.class);
                                resouceType.setResourseBean(lesssonBean);
                            } else if (type == 2) {
                                ResourceLessonHomeBean lessonHomeBean = JSON.parseObject(string, ResourceLessonHomeBean.class);
                                resouceType.setLessonHomeBean(lessonHomeBean);
                            }
                            resouceType.setType(type);
                        }


                        return resouceType;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);
                    }

                    @Override
                    public void onResponse(ResouceType homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = 1;
                        mHandler.sendMessage(message);

                    }
                });


    }

    @Override
    protected void initData() {
        allResourseKindBean = new ArrayList<>();
        allCourseList = new ArrayList<>();
        if (isLoad) {
            getResourseNet();
        }
    }

    @OnClick(R.id.resourse_et_search)
    public void search() {
        Intent intent = new Intent(getActivity(), ActSearchLesson.class);
        startActivity(intent);
    }

//刷新
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                isReFlesh = true;

                getResourseNet();
                swipe_refresh_widget.setRefreshing(false);
                isReFlesh = false;
            }
        }, 1000);
    }
}
