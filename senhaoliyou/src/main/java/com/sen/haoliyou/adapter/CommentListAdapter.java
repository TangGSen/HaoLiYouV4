package com.sen.haoliyou.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sen.haoliyou.R;
import com.sen.haoliyou.imgloader.AnimateFirstDisplayListener;
import com.sen.haoliyou.mode.CommentItemBean;
import com.sen.haoliyou.tools.Constants;
import com.sen.haoliyou.tools.ImageLoadOptions;

import java.util.List;

/**
 * Created by Sen on 2016/2/22.
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private List<CommentItemBean> mData;
    private Context mContext;

    public CommentListAdapter(Context context, List<CommentItemBean> data) {
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
        void onItemClick(View view, int position, CommentItemBean childItemBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_show, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        CommentItemBean itemBean = mData.get(position);
       String userPhoUrl = Constants.PATH_PORTRAIT+itemBean.getUserphoto();

        holder.user_common_name.setText(itemBean.getUsername());
        holder.tv_comment_time.setText(dealString(itemBean.getCtime()));
        holder.tv_comment_content.setText(itemBean.getContent());
        holder.imv_commemt_user.setTag(userPhoUrl);

        ImageLoader.getInstance().displayImage((String) holder.imv_commemt_user.getTag(), holder.imv_commemt_user, ImageLoadOptions.getCommentImageOptions(),new AnimateFirstDisplayListener());

    }

   private String dealString(String time){
       if (!TextUtils.isEmpty(time)){
         return  time = time.substring(0 ,16);
       }
       return "";
   }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView user_common_name,tv_comment_content,tv_comment_time;
        public AppCompatImageView imv_commemt_user;

        public ViewHolder(View view) {
            super(view);
            user_common_name = (AppCompatTextView) view.findViewById(R.id.tv_comment_name);
            tv_comment_content = (AppCompatTextView) view.findViewById(R.id.tv_comment_content);
            tv_comment_time = (AppCompatTextView) view.findViewById(R.id.tv_comment_time);
            imv_commemt_user = (AppCompatImageView) view.findViewById(R.id.imv_commemt_user);

        }


    }
}
