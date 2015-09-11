package com.appshare.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshare.R;
import com.appshare.app.ShareInfo;

public class ShareListAdapter  extends BaseAdapter{
	private Activity mContext;
	private LayoutInflater mInflater;
	List<ShareInfo> mshareDataList = null;

	public ShareListAdapter(Activity context, List<ShareInfo> shareDataList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mshareDataList = shareDataList;
	}

	@Override
	public int getCount() {
		return mshareDataList.size();
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
			convertView = mInflater.inflate(R.layout.item_share_row, null);
			holder.appName = (TextView) convertView.findViewById(R.id.share_app_name);
			holder.userName = (TextView) convertView.findViewById(R.id.share_user_name);
            holder.sign = (TextView) convertView.findViewById(R.id.share_user_sign);
			holder.appIntro=(TextView)convertView.findViewById(R.id.share_app_intro);
            holder.likenum=(TextView)convertView.findViewById(R.id.like_num);
            holder.favnum=(TextView)convertView.findViewById(R.id.fav_num);
            holder.commentnum=(TextView)convertView.findViewById(R.id.comment_num);
            holder.sharetime=(TextView)convertView.findViewById(R.id.share_time);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.share_app_icon);
            holder.userIcon=(ImageView)convertView.findViewById(R.id.share_user_icon);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * data
		 */
        holder.sign.setText(mshareDataList.get(position).sign);
        holder.userName.setText(mshareDataList.get(position).userName);
		holder.userIcon.setImageDrawable(mshareDataList.get(position).userIcon);
		holder.appIcon.setImageDrawable(mshareDataList.get(position).appIcon);
		holder.appName.setText(mshareDataList.get(position).appName);
		holder.appIntro.setText(mshareDataList.get(position).appIntro);
        holder.likenum.setText(mshareDataList.get(position).likeNum);
        holder.favnum.setText(mshareDataList.get(position).favNum);
        holder.commentnum.setText(mshareDataList.get(position).commentNum);
        holder.sharetime.setText(mshareDataList.get(position).shareTime);
		return convertView;
	}

	static final class ViewHolder {
		public TextView appName;
		public TextView userName;
		public ImageView appIcon;
		public ImageView userIcon;
		public TextView appIntro;
		public TextView favnum;
		public TextView commentnum;
        public TextView likenum;
		public TextView sharetime;
        public TextView sign;
	}
}
