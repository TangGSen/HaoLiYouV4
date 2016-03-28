package com.sen.haoliyou.activity.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.sen.haoliyou.R;
import com.sen.haoliyou.base.BaseActivity;
import com.sen.haoliyou.exam.ExamItemBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ActExamDetail extends BaseActivity {

    @Bind(R.id.tv_exam_title)
    AppCompatTextView tv_exam_title;
    @Bind(R.id.tv_begin_time)
    AppCompatTextView tv_begin_time;
    @Bind(R.id.tv_end_time)
    AppCompatTextView tv_end_time;
    @Bind(R.id.tv_exam_type_detail)
    AppCompatTextView tv_exam_type_detail;
    @Bind(R.id.tv_passs_score)
    AppCompatTextView tv_passs_score;
    @Bind(R.id.tv_has_enter_detail)
    AppCompatTextView tv_has_enter_detail;
    @Bind(R.id.tv_examtime)
    AppCompatTextView tv_examtime;
    @Bind(R.id.tv_exam_class)
    AppCompatTextView tv_exam_class;
    @Bind(R.id.tv_belong_class)
    AppCompatTextView tv_belong_class;
    @Bind(R.id.tv_main_depman)
    AppCompatTextView tv_main_depman;
    @Bind(R.id.tv_exam_introduce)
    AppCompatTextView tv_exam_introduce;
    @Bind(R.id.btn_enter_exam)
    AppCompatButton btn_enter_exam;
    @Bind(R.id.exam_detail_toolbar)
    Toolbar exam_detail_toolbar;
    @Bind(R.id.exam_imgbtn_close)
    AppCompatTextView exam_imgbtn_close;

    ExamItemBean childItemBean;

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ExamItemBeanBundle");
        childItemBean = (ExamItemBean) bundle.getSerializable("ExamItemBean");

        if (childItemBean == null) {
            Toast.makeText(this, "获取考试详情出错", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_exam_item_detail);
        ButterKnife.bind(this);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        showData();
    }

    private void showData() {
        tv_exam_title.setText(childItemBean.getExamname());
        tv_begin_time.setText("开始时间：" + childItemBean.getBegindate() );
        tv_end_time.setText("结束时间：" + childItemBean.getEnddate());

        int examType = Integer.parseInt(childItemBean.getExamtype());
        String examTypeStr = "";
        if (examType == 0) {
            examTypeStr = "未开始";
        } else if (examType == 1) {
            examTypeStr = "进行中";
        } else if (examType == 2) {
            examTypeStr = "已结束";
        }
        tv_exam_type_detail.setText("状态：" + examTypeStr);
        tv_has_enter_detail.setText("已考次数：" + childItemBean.getYetjoincon() + "/" + childItemBean.getJoincon());
        tv_examtime.setText("时长：" + childItemBean.getExamtime() + "分");
        tv_passs_score.setText("通过分数：" + childItemBean.getPassmark() + "分");
        tv_exam_class.setText("考试类别：" + childItemBean.getType());
        tv_belong_class.setText("所属培训班：" + childItemBean.getTrainingname());
        tv_main_depman.setText("主办单位：" + childItemBean.getDeptname());
        tv_exam_introduce.setText("考试说明：" + childItemBean.getRemark());

        if (childItemBean.getIsenter().equals("1") && childItemBean.getExamtype().equals("1")) {
              btn_enter_exam.setEnabled(true);
        } else {
            btn_enter_exam.setEnabled(false);
        }
    }

    @OnClick(R.id.btn_enter_exam)
    public void enterExam() {
        Intent intent = new Intent(this, ActExamTest.class);
        intent.putExtra("examId", childItemBean.getExamid());
        intent.putExtra("examName", childItemBean.getExamname());
        startActivity(intent);
        exit();
    }

    @OnClick(R.id.exam_imgbtn_close)
    public void exitExam() {
        exit();
    }

    private void exit(){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

}
