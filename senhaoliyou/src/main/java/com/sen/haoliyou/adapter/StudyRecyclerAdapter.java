package com.sen.haoliyou.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sen.haoliyou.R;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.LessonItemBean;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.ImageLoadOptions;
import com.sen.haoliyou.tools.StringUItils;

import java.util.List;

/**
 * Created by Sen on 2016/2/22.
 */
public class StudyRecyclerAdapter extends RecyclerView.Adapter<StudyRecyclerAdapter.ViewHolder> {
    private List<LessonItemBean> mData;
    private Context mContext;
    int width;

    public StudyRecyclerAdapter(Context context, List<LessonItemBean> data) {
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
        void onItemClick(View view, int position, LessonItemBean childItemBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.onItemClick(holder.itemView, position, mData.get(position));
                }

            });
        }
        LessonItemBean itemBean = mData.get(position);
        String imgUrl = Constants.PATH + Constants.PATH_PICTURE + itemBean.getPicture();
        holder.mImageCourse.setTag(imgUrl);
        holder.item_tv_name.setText( itemBean.getName());
        holder.item_tv_time.setText("上传时间：" + StringUItils.delString(itemBean.getCreatedate(),10));
        holder.item_tv_brief.setText(  itemBean.getRemark());
        ImageLoader.getInstance().displayImage((String) holder.mImageCourse.getTag(), holder.mImageCourse, ImageLoadOptions.getStudyImageOptions(),new AnimateFirstDisplayListener());

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView item_tv_time, item_tv_name, item_tv_brief;
        public AppCompatImageView mImageCourse;

        public ViewHolder(View view) {
            super(view);
            item_tv_name = (AppCompatTextView) view.findViewById(R.id.item_tv_name);
            item_tv_brief = (AppCompatTextView) view.findViewById(R.id.item_tv_brief);
            item_tv_time = (AppCompatTextView) view.findViewById(R.id.item_tv_time);
            mImageCourse = (AppCompatImageView) view.findViewById(R.id.item_imv_course);
        }


    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
