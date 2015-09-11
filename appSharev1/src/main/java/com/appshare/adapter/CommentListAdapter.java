package com.appshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.widget.BaseAdapter;

import com.appshare.R;
import com.appshare.app.CommentInfo;
import com.appshare.app.FriendInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2015/5/20.
 */
public class CommentListAdapter extends BaseAdapter{
    private Activity mContext;
    private LayoutInflater mInflater;
    List<CommentInfo> mCommentDataList = null;

    public CommentListAdapter(Activity context,  List<CommentInfo> mCommentDataList) {
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCommentDataList = mCommentDataList;
    }

    @Override
    public int getCount() {
        return mCommentDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_comment, null);
            holder. Name= (TextView) convertView.findViewById(R.id.commenter_name);
            holder. Content= (TextView) convertView
                    .findViewById(R.id.comment_content);
            holder. Time= (TextView) convertView
                    .findViewById(R.id.comment_time);
            holder.Head= (ImageView) convertView
                    .findViewById(R.id.commenter_head);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		/*
		 * data
		 */

        holder.Name.setText(mCommentDataList.get(position).user_name + "");
        holder.Content.setText(mCommentDataList.get(position).content
                + "");
        holder.Time.setText(mCommentDataList.get(position).time + "");
        holder.Head.setImageDrawable(mCommentDataList.get(position).head);

        return convertView;
}
    static final class ViewHolder {
        public TextView Name;
        public TextView Content;
        public ImageView Head;
        public TextView Time;
    }
}
