package com.appshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshare.R;
import com.appshare.app.AppInfo;

import java.util.List;

public class SimpleAppListAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	List<AppInfo> mAppDataList = null;

	public SimpleAppListAdapter(Activity context, List<AppInfo> appDataList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mAppDataList = appDataList;
	}

	@Override
	public int getCount() {
		return mAppDataList.size();
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
			convertView = mInflater.inflate(R.layout.item_simple_app_row, null);
			holder.appName = (TextView) convertView.findViewById(R.id.tv_name);
			//holder.appPackageName = (TextView) convertView
			//		.findViewById(R.id.tv_size);
			//holder.appIcon = (ImageView) convertView
			//		.findViewById(R.id.iv_icon);
			//holder.appVersion=(TextView)convertView.findViewById(R.id.tv_version);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * data
		 */

		holder.appName.setText(mAppDataList.get(position).appName + "");
		//holder.appPackageName.setText(mAppDataList.get(position).packageName
		//		+ "");
		//holder.appIcon.setImageDrawable(mAppDataList.get(position).appIcon);
		//holder.appVersion.setText(mAppDataList.get(position).versionName);
		return convertView;
	}

	static final class ViewHolder {
		public TextView appName;
		//public TextView appPackageName;
		//public ImageView appIcon;
		//public TextView appVersion;
	}
}
 