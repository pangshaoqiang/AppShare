package com.appshare.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appshare.R;
import com.appshare.ShareActivity;
import com.appshare.adapter.AppListAdapter;
import com.appshare.app.AppInfo;
import com.appshare.base.AppTrans;

import android.R.anim;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class AppFragment extends Fragment {

	private ListView list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview=inflater.inflate(R.layout.app_layout, container, false);
		list = (ListView) rootview.findViewById(R.id.app_list);
		list.setAdapter(new AppListAdapter(this.getActivity(), getData()));
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
			//	View dialog_view = inflater.inflate(R.layout.dialog, null); 
				AlertDialog.Builder addFriend=new AlertDialog.Builder(getActivity());
				addFriend.setTitle(getResources().getString(R.string.Share))
				.setPositiveButton(getResources().getString(R.string.Sure), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						
						ImageView img=(ImageView)view.findViewById(R.id.iv_icon);
						Drawable icon=img.getDrawable();
						TextView text=(TextView)view.findViewById(R.id.tv_name);
						String app_name=(String) text.getText();

						((AppTrans) getActivity().getApplication()).setIcon(icon);
						((AppTrans) getActivity().getApplication()).setValue(app_name);
						
						Intent intent=new Intent(getActivity(),ShareActivity.class);
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.in_from_right, anim.fade_out); 
					}               
					//close this activity
				})
				.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
				})
				.show();

				return false;
			}
		});
		
		return rootview;
	}

	private ArrayList<AppInfo> getData() {
		PackageManager pm =this.getActivity().getPackageManager();
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		new ArrayList<Map<String, Object>>();
		List<PackageInfo> packages = this.getActivity().getPackageManager()
				.getInstalledPackages(0);
		new HashMap<String, Object>();
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					this.getActivity().getPackageManager()).toString();
			String dir = packageInfo.applicationInfo.publicSourceDir;
			int  size=Integer.valueOf((int) new File(dir).length());
			String new_size;
			if(size<(1024*1024)){
			    new_size = String.valueOf((double)(size/(1024)))+"KB";
			    
			}else{
				new_size=String.valueOf((double)(size/(1024*1024)))+"MB";
			}
	//		String date=Long.toString(new File(dir).lastModified());
//            String date =new SimpleDateFormat(formatType).format(data); 
//            		new DateFormat(new File(dir).lastModified());
            tmpInfo.packageName=new_size;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.appIcon = pm.getApplicationIcon(packageInfo.applicationInfo);
			// Only display the non-system app info
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appList.add(tmpInfo);
			}
		}

		return appList;
	}
	
}
