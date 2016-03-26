package com.sen.haoliyou.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sen.haoliyou.R;
import com.sen.haoliyou.mode.ResourseKindBean;
import com.sen.haoliyou.tools.ResourcesUtils;

import java.util.List;

/**
 * Created by Sen on 2016/2/22.
 */
public class LessonCatalogAdapter extends RecyclerView.Adapter<LessonCatalogAdapter.ViewHolder> {
    private List<ResourseKindBean> mData;
    private Context mContext;

    public LessonCatalogAdapter(Context context, List<ResourseKindBean> data) {
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
        void onItemClick(View view, int position, ResourseKindBean childItemBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resourse_catalog, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ResourseKindBean itemBean = mData.get(position);

        holder.item_catalog_name.setText(itemBean.getName());
        if (position%2==0){
            holder.viewBar.setBackgroundColor(ResourcesUtils.getResColor(mContext,R.color.view_bar_blue));
        }else{
            holder.viewBar.setBackgroundColor(ResourcesUtils.getResColor(mContext,R.color.view_bar_orgen));
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
        public AppCompatTextView item_catalog_name;
        public View viewBar;

        public ViewHolder(View view) {
            super(view);
            item_catalog_name = (AppCompatTextView) view.findViewById(R.id.item_catalog_name);
            viewBar = (View) view.findViewById(R.id.view_bar);

        }


    }
}
