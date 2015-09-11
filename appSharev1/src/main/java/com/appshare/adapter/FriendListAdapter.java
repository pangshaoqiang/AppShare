package com.appshare.adapter;

import java.util.List;

import com.appshare.R;
import com.appshare.app.FriendInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{
	private Activity mContext;
	private LayoutInflater mInflater;
	List<FriendInfo> mFriendDataList = null;

	public FriendListAdapter(Activity context, List<FriendInfo> FriendDataList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mFriendDataList = FriendDataList;
	}

	@Override
	public int getCount() {
		return mFriendDataList.size();
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
			convertView = mInflater.inflate(R.layout.item_friend_row, null);
			holder. FriendName= (TextView) convertView.findViewById(R.id.person_name);
			holder. FriendTel= (TextView) convertView
					.findViewById(R.id.person_telephone);
			holder.FriendIcon= (ImageView) convertView
					.findViewById(R.id.person_icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * data
		 */

		holder.FriendName.setText(mFriendDataList.get(position).FriendName + "");
		holder.FriendTel.setText(mFriendDataList.get(position).FriendTel
				+ "");
		holder.FriendIcon.setImageDrawable(mFriendDataList.get(position).FriendIcon);

		return convertView;
	}

	static final class ViewHolder {
		public TextView FriendName;
		public TextView FriendTel;
		public ImageView FriendIcon;
	}
}
