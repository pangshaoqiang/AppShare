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

public class MyShareListAdapter extends BaseAdapter{
	private Activity mContext;
	private LayoutInflater mInflater;
	List<ShareInfo> mMyshareDataList = null;

	public MyShareListAdapter(Activity context, List<ShareInfo> MyshareDataList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mMyshareDataList = MyshareDataList;
	}

	@Override
	public int getCount() {
		return mMyshareDataList.size();
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
			convertView = mInflater.inflate(R.layout.item_my_share, null);
			holder.appName = (TextView) convertView.findViewById(R.id.share_app_name);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.share_app_icon);
			holder.appIntro=(TextView)convertView.findViewById(R.id.share_app_intro);
			
			//fav
			//comment
			//date
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * data
		 */

		holder.appIcon.setImageDrawable(mMyshareDataList.get(position).appIcon);
		holder.appName.setText(mMyshareDataList.get(position).appName);
		holder.appIntro.setText(mMyshareDataList.get(position).appIntro);
		//fav
		//comment
		//date
		
		return convertView;
	}

	static final class ViewHolder {
		public TextView appName;
		public ImageView appIcon;
		public TextView appIntro;
		public TextView favnum;
		public TextView commentnum;
		public TextView date;
	}
}
