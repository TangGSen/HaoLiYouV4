package com.sen.haoliyou.activity.study;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.ActSearchLesson;
import com.sen.haoliyou.adapter.LessonCatalogAdapter;
import com.sen.haoliyou.adapter.StudyRecyclerAdapter;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.EnventLessonLoveChange;
import com.sen.haoliyou.mode.EventComentCountForResouce;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.mode.ResouceType;
import com.sen.haoliyou.mode.ResourceLessonHomeBean;
import com.sen.haoliyou.mode.ResourseBean;
import com.sen.haoliyou.mode.ResourseKindBean;
import com.sen.haoliyou.tools.AcountManager;
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
 * Created by Administrator on 2016/3/10.
 */
public class ActRepositoryDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String catalogId;

    @Bind(R.id.resourse_detail_listview)
    RecyclerView resourse_detail_listview;
    StudyRecyclerAdapter studyRecyclerAdapter;

    @Bind(R.id.resdetail_refreshlayout)
    SwipeRefreshLayout swipe_refresh_widget;

    @Bind(R.id.bt_search)
    AppCompatTextView bt_search;
    @Bind(R.id.second_recouse_back)
    AppCompatImageView second_recouse_back;

    private List<ResourseKindBean> allResourseKindBean;

    private List<LessonItemBean> allCourseList;

    private boolean isReFlesh = false;

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        catalogId = intent.getStringExtra("catalogId");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_resourse_detail);
        ButterKnife.bind(this);
        settingRecyleView();


    }

    private void settingRecyleView() {
        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(this);
        resourse_detail_listview.setLayoutManager(linearnLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        resourse_detail_listview.setHasFixedSize(true);

//        添加分割线
        resourse_detail_listview.addItemDecoration(new RecyleViewItemDecoration(this, R.drawable.shape_recycle_item_decoration));

        swipe_refresh_widget.setColorSchemeResources(R.color.theme_color, R.color.theme_color);
        swipe_refresh_widget.setOnRefreshListener(this);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        allResourseKindBean = new ArrayList<>();
        allCourseList = new ArrayList<>();
        getResourseNet();
    }

    private static final int GETDATA_ERROR = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //下拉刷新和加载更多的时候就不用diaogle
            if (!isReFlesh)
                DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:
                    Toast.makeText(ActRepositoryDetail.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    ResouceType resouceType = (ResouceType) msg.obj;
                    // 当返回的数据为空的时候，那么就要显示这个
                    Log.e("sen", resouceType.getType() + "");
                    if (resouceType == null) {
                        Toast.makeText(ActRepositoryDetail.this, "请求数据失败，请重试", Toast.LENGTH_SHORT).show();
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

    private void showLessonData(ResouceType resouceType) {
        ResourceLessonHomeBean resourseLesson = resouceType.getLessonHomeBean();
        if (resourseLesson == null) {
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LessonItemBean> courseList = resourseLesson.getCourseslist();
        if (courseList == null) {
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        if (courseList.size() == 0) {
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        allCourseList.clear();
        allCourseList.addAll(courseList);
        courseList.clear();
        studyRecyclerAdapter = new StudyRecyclerAdapter(this, allCourseList);
        studyRecyclerAdapter.setOnItemClickListener(new StudyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, LessonItemBean childItemBean) {
                Intent intent = new Intent(ActRepositoryDetail.this, ActResoucesStudyDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemLessonBean", childItemBean);
                bundle.putInt("itemPosition", position);
                intent.putExtra("FragmentStudyBundle", bundle);
                startActivity(intent);
            }
        });
        resourse_detail_listview.setAdapter(studyRecyclerAdapter);
    }

    private void showLessonCatalog(ResouceType resouceType) {
        ResourseBean resourse = resouceType.getResourseBean();
        if (resourse == null) {
            return;
        }
        List<ResourseKindBean> cataLogList = resourse.getKnoeledgeList();
        if (cataLogList.size() == 0 || cataLogList == null) {

            return;
        }
        allResourseKindBean.clear();
        allResourseKindBean.addAll(cataLogList);
        cataLogList.clear();
        LessonCatalogAdapter cataLogAdapter = new LessonCatalogAdapter(this, allResourseKindBean);
        resourse_detail_listview.setAdapter(cataLogAdapter);

        cataLogAdapter.setOnItemClickListener(new LessonCatalogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ResourseKindBean childItemBean) {
                Intent intent = new Intent(ActRepositoryDetail.this, ActRepositoryDetail.class);
                intent.putExtra("catalogId", childItemBean.getId());
                startActivity(intent);
            }
        });
    }

    //接收评论的结果
    public void onEvent(EventComentCountForResouce event) { //接收方法  在发关事件的线程接收
        if (event != null && allCourseList != null && studyRecyclerAdapter != null) {
            LessonItemBean lessonItemBean = allCourseList.get(event.getPosition());
            int count = (Integer.parseInt(lessonItemBean.getComment()) + event.getSucessCount());
            lessonItemBean.setComment(count + "");
            studyRecyclerAdapter.notifyItemChanged(event.getPosition());
        }
    }


    //当课程被收藏状态被改变的时候，
    public void onEvent(EnventLessonLoveChange event) {
        int position = event.getPosition();
        if (allCourseList != null && allCourseList.size() >= position + 1) {
            allCourseList.get(position).setIsselected(event.getLove());
        }
    }

    @OnClick(R.id.bt_search)
    public void toSearch() {
        Intent intent = new Intent(this, ActSearchLesson.class);
        startActivity(intent);
    }

    @OnClick(R.id.second_recouse_back)
    public void back() {
        finish();

    }

    private void getResourseNet() {
        if (!NetUtil.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.has_not_net, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isReFlesh)
            DialogUtils.showDialog(this, "请稍等");
        String url = Constants.PATH + Constants.PATH_Repository;

        OkHttpUtils.post()
                .url(url)
                .addParams("userid", AcountManager.getAcountId())
                .addParams("knoid", catalogId)
                .build()
                .execute(new Callback<ResouceType>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public ResouceType parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        AnimateFirstDisplayListener.displayedImages.clear();
        super.onDestroy();

    }

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
