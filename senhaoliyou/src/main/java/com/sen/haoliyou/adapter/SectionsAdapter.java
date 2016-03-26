package com.sen.haoliyou.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sen.haoliyou.R;
import com.sen.haoliyou.activity.VideoPlayerActivity;
import com.sen.haoliyou.mode.SectionItemBean;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.DownloadUtils;

import java.util.List;


/**
 * Created by Sen on 2016/2/22.
 */
public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {
    private List<SectionItemBean> mData;
    private Context mContext;
    private String mLessId;

    public SectionsAdapter(Context context, List<SectionItemBean> data, String lessId) {
        mContext = context;
        mData = data;
        mLessId = lessId;
    }

//    public void addLessonBeanData(List<LessonItemBean> data){
//        mData.addAll(data);
//        notifyDataSetChanged();
//    }

    private OnItemClickListener onItemClickListener = null;


    //Item click thing
    public interface OnItemClickListener {
        void onItemClick(View view, int position, SectionItemBean childItemBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_setion_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        SectionItemBean itemBean = mData.get(position);

        holder.text_section_name.setText(itemBean.getSectionname());


        holder.text_section_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPlayIntent = new Intent(mContext, VideoPlayerActivity.class);
                startPlayIntent.setData(Uri.parse(Constants.PATH_PLAYER + mLessId + "/" +mData.get(position).getSectionurl()));
                mContext.startActivity(startPlayIntent);
            }
        });
        holder.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DownloadUtils.insterDownload(mContext, Constants.PATH_PLAYER + mLessId + "/" +mData.get(position).getSectionurl(),mData.get(position).getSectionname(),mData.get(position).getId());
            }
        });


    }




    @Override
    public int getItemCount() {
        return mData.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView text_section_name;
        public AppCompatTextView btn_down;

        public ViewHolder(View view) {
            super(view);
            text_section_name = (AppCompatTextView) view.findViewById(R.id.text_section_name);
            btn_down = (AppCompatTextView) view.findViewById(R.id.btn_down);

        }


    }
}
