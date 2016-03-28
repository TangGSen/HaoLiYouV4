package com.sen.haoliyou.activity.study;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sen.haoliyou.R;
import com.sen.haoliyou.adapter.CommentListAdapter;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.CommentHomeBean;
import com.sen.haoliyou.mode.CommentItemBean;
import com.sen.haoliyou.mode.EventComentCountForResouce;
import com.sen.haoliyou.mode.EventComentCountForStudy;
import com.sen.haoliyou.mode.EventSubmitComentSucess;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DialogUtils;
import com.sen.haoliyou.tools.NetUtil;
import com.sen.haoliyou.widget.RecyleViewItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ActCommentList extends BaseActivity  {
    //	protected static final int END_TO_WRITE_COMMENT = 0;

    //	private ImageButton btn_back_comment;
//	private ImageButton btn_write_comment;
    private CommentHomeBean getLessonCommentListBean;

    @Bind(R.id.listview_comment)
    XRecyclerView xRecyclerView;
    @Bind(R.id.common_back)
    AppCompatTextView common_back;
    @Bind(R.id.btn_write_common)
    AppCompatImageButton btn_write_common;

    @Bind(R.id.comment_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;






    private List<CommentItemBean> commonList;
    private List<CommentItemBean> allCommonList;
    private String courseId;
    //这个评论页是从哪个act 来的，EventBus 分发不同
    private String from;

    private int maxPage = 0;
    private int currentPage = 1;

    private static final int GETDATA_ERROR = 0;
    private static final int SHOW_DATA = 1;

    private CommentListAdapter adapter;
    private int sumbmitCount = 0;
    private  boolean isLoadReflesh = false;
    private  boolean isLoadMore = false;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Toast.makeText(ActCommentList.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    CommentHomeBean homeBeam = (CommentHomeBean) msg.obj;
                    maxPage = homeBeam.getMaxPage();
                    commonList = homeBeam.getCommontsList();

                    if (commonList == null) {
                        Toast.makeText(ActCommentList.this, "当前没评论", Toast.LENGTH_SHORT).show();
                    }
                    if (commonList.size() == 0) {
                        Toast.makeText(ActCommentList.this, "当前没评论", Toast.LENGTH_SHORT).show();
                    }

                    allCommonList.addAll(commonList);
                    Collections.sort(allCommonList);
                    commonList.clear();
                    //创建并设置Adapter
                    if (adapter == null) {

                        Log.e("sen", "刷新222444");
                    } else {
                        Log.e("sen", "刷新222");
                        adapter.notifyDataSetChanged();
                    }


                    break;


            }
            DialogUtils.closeDialog();
            return false;
        }
    });
    private LinearLayoutManager linearnLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onEvent(EventSubmitComentSucess event) { //接收方法  在发关事件的线程接收
        sumbmitCount++;
        if (allCommonList != null) {
            allCommonList.clear();
        } else {
            allCommonList = new ArrayList<>();

        }
        getCommntList(1);
    }


    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        courseId = intent.getStringExtra("leid");
        from = intent.getStringExtra("from");

    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
        settingRecyleView();

    }

    private void settingRecyleView() {
        allCommonList = new ArrayList<>();
        linearnLayoutManager = new LinearLayoutManager(this);
        linearnLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(linearnLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        xRecyclerView.setHasFixedSize(true);

        xRecyclerView.addItemDecoration(new RecyleViewItemDecoration(this, R.drawable.shape_recycle_item_decoration));
        adapter = new CommentListAdapter(ActCommentList.this, allCommonList);
        xRecyclerView.setAdapter(adapter);

        swipe_refresh_widget.setColorSchemeResources(R.color.theme_color, R.color.theme_color);
        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        currentPage =1;
                        isLoadReflesh = true;
                        allCommonList.clear();
                        getCommntList(1);
                        isLoadReflesh = false;
                        swipe_refresh_widget.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (maxPage == currentPage) {
                            xRecyclerView.loadMoreComplete();
                            Toast.makeText(ActCommentList.this, "已加载全部数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        isLoadMore = true;
                        currentPage++;
                        getCommntList(currentPage);
                        isLoadMore = false;
                        xRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });



    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        if (courseId != null && NetUtil.isNetworkConnected(this)) {
            getCommntList(1);
        }
    }


    private void getCommntList(int page) {
        if (!isLoadMore && !isLoadReflesh){
            DialogUtils.showDialog(this,"请稍等");
        }
        if (!NetUtil.isNetworkConnected(this)) {
            DialogUtils.closeDialog();
            Toast.makeText(ActCommentList.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constants.PATH + Constants.PATH_GetCommentsList;
        OkHttpUtils.post()
                .url(url)
                .addParams("pageNum", page + "")
                .addParams("leid", courseId)
                .build()
                .execute(new Callback<CommentHomeBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public CommentHomeBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        CommentHomeBean lesssonBean = JSON.parseObject(string, CommentHomeBean.class);
                        return lesssonBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);


                    }

                    @Override
                    public void onResponse(CommentHomeBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = SHOW_DATA;
                        mHandler.sendMessage(message);

                    }
                });
    }

    //返回
    @OnClick(R.id.common_back)
    public void clickOnBack() {
        //分发不同的EventBus
        sendDifulentEventBus();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    @OnClick(R.id.btn_write_common)
    public void clickOnWriteComment() {

        Intent in = new Intent(this, ActWriteComment.class);
        in.putExtra("leid", courseId);
        startActivity(in);


    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        AnimateFirstDisplayListener.displayedImages.clear();
        super.onDestroy();

    }
    public void sendDifulentEventBus(){
        if (!TextUtils.isEmpty(from)) {
            if ("ActStudyDetail".equals(from)) {
                EventBus.getDefault().post(new EventComentCountForStudy(sumbmitCount));
            }else {
                EventBus.getDefault().post(new EventComentCountForResouce(sumbmitCount));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendDifulentEventBus();
            finish();
            return true;
        }
        return false;
    }




}
