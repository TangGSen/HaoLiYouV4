package com.sen.haoliyou.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sen.haoliyou.R;
import com.sen.haoliyou.exam.ExamItemBean;
import com.sen.haoliyou.tools.ResourcesUtils;
import com.sen.haoliyou.tools.StringUItils;

import java.util.List;

/**
 * Created by Sen on 2016/2/22.
 */
public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ViewHolder> {
    private List<ExamItemBean> mData;
    private Context mContext;

    public ExamListAdapter(Context context, List<ExamItemBean> data) {
        mContext = context;
        mData = data;
    }

//    public void addLessonBeanData(List<LessonItemBean> data){
//        mData.addAll(data);
//        notifyDataSetChanged();
//    }

    private OnItemClickListener onItemClickListener = null;


    //Item click thing
    public interface OnItemClickListener {
        void onItemClick(View view, int position, ExamItemBean childItemBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_examlist_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ExamItemBean itemBean = mData.get(position);

        holder.tv_exam_name.setText(itemBean.getExamname());
        holder.tv_has_enter.setText("已考："+itemBean.getYetjoincon()+"         允许："+itemBean.getJoincon());
        String time ="时间："+ StringUItils.delString(itemBean.getBegindate(),10)+"至"+StringUItils.delString(itemBean.getEnddate(),10);
        holder.tv_test_time.setText(time);

        int examType =Integer.parseInt(itemBean.getExamtype());
        String examTypeStr = "";
        if (examType==0){
            examTypeStr ="未开始";
            holder.tv_exam_type.setBackgroundDrawable(ResourcesUtils.getResDrawable(mContext,R.drawable.bg_exam_unenter));
        }else if (examType==1){
            examTypeStr ="进行中";
            holder.tv_exam_type.setBackgroundDrawable(ResourcesUtils.getResDrawable(mContext,R.drawable.bg_exam_type));
        }else if (examType ==2){
            holder.tv_exam_type.setBackgroundDrawable(ResourcesUtils.getResDrawable(mContext,R.drawable.bg_exam_unenter));
            examTypeStr ="已结束";
        }
        holder.tv_exam_type.setText(examTypeStr);
        if (position%2==0){
            holder.test_view_bar.setBackgroundColor(ResourcesUtils.getResColor(mContext,R.color.view_bar_blue));
        }else{
            holder.test_view_bar.setBackgroundColor(ResourcesUtils.getResColor(mContext,R.color.view_bar_orgen));
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.onItemClick(holder.itemView, position,mData.get(position));
                }

            });
        }




    }







    @Override
    public int getItemCount() {
        return mData.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_exam_name,tv_test_time,tv_exam_type,tv_has_enter;
        public View test_view_bar;

        public ViewHolder(View view) {
            super(view);
            tv_exam_name = (AppCompatTextView) view.findViewById(R.id.tv_exam_name);
            tv_test_time = (AppCompatTextView) view.findViewById(R.id.tv_test_time);
            tv_exam_type = (AppCompatTextView) view.findViewById(R.id.tv_exam_type);
            tv_has_enter = (AppCompatTextView) view.findViewById(R.id.tv_has_enter);
            test_view_bar = (View) view.findViewById(R.id.test_view_bar);


        }


    }
}
