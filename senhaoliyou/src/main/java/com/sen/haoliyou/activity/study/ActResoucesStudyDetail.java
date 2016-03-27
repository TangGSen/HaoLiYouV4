package com.sen.haoliyou.activity.study;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.VideoPlayerActivity;
import com.sen.haoliyou.adapter.SectionsAdapter;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.mode.EnventLessonLoveChange;
import com.sen.haoliyou.mode.EventComentCountForResouce;
import com.sen.haoliyou.mode.EventLoveClickFromRescouce;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.mode.SectionBean;
import com.sen.haoliyou.mode.SectionItemBean;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ActResoucesStudyDetail extends BaseActivity {

    private LessonItemBean childItemBean;

    private int itemPosition;

    @Bind(R.id.btn_resouce_back)
    AppCompatTextView btn_resouce_back;
    @Bind(R.id.view_line)
    View view_line;
    @Bind(R.id.tv_head_name)
    AppCompatTextView tv_head_name;
    @Bind(R.id.btn_lesson_collection)
    AppCompatTextView btn_lesson_collection;
    @Bind(R.id.btn_startPlayer)
    AppCompatImageView btn_startPlayer;
    @Bind(R.id.listview_lesson)
    RecyclerView listview_lesson;
    @Bind(R.id.layout_user_comment)
    RelativeLayout layout_user_comment;
    @Bind(R.id.tv_lesson_remark)
    AppCompatTextView tv_lesson_remark;
    @Bind(R.id.tv_comment_count)
    AppCompatTextView tv_comment_count;


    private List<SectionItemBean> sectionList;

    private static final int GETDATA_ERROR = 0;
    private static final int SHOW_DATA = 1;
    private static final int COLLECTE_LESSON=2;

    private boolean isSeleted = false;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            DialogUtils.closeDialog();
            switch (msg.what) {
                case 0:
                    view_line.setVisibility(View.GONE);
                    Toast.makeText(ActResoucesStudyDetail.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    view_line.setVisibility(View.VISIBLE);
                    SectionBean homeBeam = (SectionBean) msg.obj;
                    sectionList = homeBeam.getSectionlist();
                    //创建并设置Adapter
                    SectionsAdapter adapter = new SectionsAdapter(ActResoucesStudyDetail.this, sectionList, childItemBean.getId());
                    listview_lesson.setAdapter(adapter);
                    break;

                case 2:
                    boolean selecte = (boolean) msg.obj;
                    //如果当前是选的，那么就finish 就行了
                    if (isSeleted && selecte) {
                        //最后还有
                        //isSeleted = !isSeleted;
                        btn_lesson_collection.setSelected(false);
                        isSeleted = !isSeleted;
                        EventBus.getDefault().post(new EventLoveClickFromRescouce());
                        showSelecedDialog(ResourcesUtils.getResString(ActResoucesStudyDetail.this, R.string.str_unlove_lesson));
                    } else if (!isSeleted && selecte) {
                        btn_lesson_collection.setSelected(true);
                        isSeleted = !isSeleted;
                        EventBus.getDefault().post(new EventLoveClickFromRescouce());
                        showSelecedDialog(ResourcesUtils.getResString(ActResoucesStudyDetail.this, R.string.str_love_lesson));
                    } else {
                        //
                        Toast.makeText(ActResoucesStudyDetail.this, "操作失败，请稍后重试", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("FragmentStudyBundle");
        childItemBean = (LessonItemBean) bundle.getSerializable("itemLessonBean");
        itemPosition = bundle.getInt("itemPosition");

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setContentView(R.layout.activity_lesson_detail);
        ButterKnife.bind(this);
        view_line.setVisibility(View.GONE);
        settingRecyleView();


    }

    private void settingRecyleView() {
        LinearLayoutManager linearnLayoutManager = new LinearLayoutManager(this);
        listview_lesson.setLayoutManager(linearnLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        listview_lesson.setHasFixedSize(true);

//        添加分割线
        listview_lesson.addItemDecoration(new RecyleViewItemDecoration(this, R.drawable.shape_recycle_item_decoration));


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initViewData();
        sectionList = new ArrayList<>();
        boolean hasNet = NetUtil.isNetworkConnected(this);
        if (hasNet) {
            getVedioData();
        } else {
            Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
        }

    }

    private void initViewData() {
        tv_head_name.setText(childItemBean.getName());
        tv_lesson_remark.setText(childItemBean.getRemark());
        // 对收藏是否显示的操作
        if ("0".equals(childItemBean.getIsselected())) {
            // 显示未收藏的按钮
            isSeleted = false;
            btn_lesson_collection.setSelected(false);
        } else {
            isSeleted = true;
            btn_lesson_collection.setSelected(true);
        }
        tv_comment_count.setText("("+childItemBean.getComment()+"条)");
    }


    private void getVedioData() {
        DialogUtils.showDialog(this, "请稍等");
        String url = Constants.PATH + Constants.PATH_GETSECTION;
        OkHttpUtils.post()
                .url(url)
                .addParams("leid", childItemBean.getId())
                .build()
                .execute(new Callback<SectionBean>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public SectionBean parseNetworkResponse(Response response) throws Exception {

                        String string = response.body().string();
                        Log.e("sen", string);
                        SectionBean lesssonBean = JSON.parseObject(string, SectionBean.class);
                        return lesssonBean;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);


                    }

                    @Override
                    public void onResponse(SectionBean homeBeam) {
                        Message message = Message.obtain();
                        message.obj = homeBeam;
                        message.what = SHOW_DATA;
                        mHandler.sendMessage(message);

                    }
                });
    }

    //返回
    @OnClick(R.id.btn_resouce_back)
    public void clickOnBack() {
        sendEventChangeLove();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    //选课功能

    @OnClick(R.id.btn_lesson_collection)
    public void setCollectionLesson() {
        //如果已经选择那么就请求取消
        if (isSeleted) {
            collectionFromNet("2");
        } else {
            collectionFromNet("1");
        }
    }


    private void showSelecedDialog(String msg) {
        BaseDialogCumstorTip.getDefault().showOneMsgOneBtnDilog(new BaseDialogCumstorTip.DialogButtonOnclickLinster() {
            @Override
            public void onLeftButtonClick(CustomerDialog dialog) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

            }

            @Override
            public void onRigthButtonClick(CustomerDialog dialog) {

            }
        }, 220,130,ActResoucesStudyDetail.this, msg, "确定");
    }


    //还没完成
    private void collectionFromNet(String flag) {
        String url = Constants.PATH + Constants.PATH_CourseSelection;
        OkHttpUtils.post()
                .url(url)
                .addParams("flag", flag)
                .addParams("userid", AcountManager.getAcountId())
                .addParams("leid", childItemBean.getId())
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
                        return success;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mHandler.sendEmptyMessage(GETDATA_ERROR);


                    }

                    @Override
                    public void onResponse(Boolean homeBeam) {
                        Message message = Message.obtain();
                        message.obj =homeBeam;
                        message.what = COLLECTE_LESSON;
                        mHandler.sendMessage(message);



                    }
                });

    }
//当评论数据提交的时候接受


    public void onEvent(EventComentCountForResouce event) {
        if (event!=null && tv_comment_count!=null){
            int count = (Integer.parseInt(childItemBean.getComment())+event.getSucessCount());
            Log.e("sen","___"+count);
            tv_comment_count.setText("("+count+"条)");
            childItemBean.setComment(count+"");

        }
    }


    @OnClick(R.id.btn_startPlayer)
    public void startVideo() {
        videoStartPlay(0);
    }

    // 播放Video
    //点击播放视频

    public void videoStartPlay(int postion) {
        if (sectionList==null){
            return;
        }
        if (sectionList.size()==0){
            Toast.makeText(ActResoucesStudyDetail.this, "没有可播放的视频,请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constants.PATH_PLAYER + childItemBean.getId() + "/" + sectionList.get(postion).getSectionurl();
        Intent startPlayIntent = new Intent(ActResoucesStudyDetail.this, VideoPlayerActivity.class);
        startPlayIntent.setData(Uri.parse(url));
        startActivity(startPlayIntent);
    }


    //看评论
    @OnClick(R.id.layout_user_comment)
    public void startComents() {
        Intent in = new Intent(this, ActCommentList.class);
        in.putExtra("leid", childItemBean.getId());
        in.putExtra("from","ActResoucesStudyDetail");
        startActivity(in);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendEventChangeLove();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendEventChangeLove() {
        EnventLessonLoveChange love;
        if (isSeleted) {
            love = new EnventLessonLoveChange("1", itemPosition);
        } else {
            love = new EnventLessonLoveChange("0", itemPosition);
        }
        EventBus.getDefault().post(love);

    }


}
