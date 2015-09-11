package com.appshare.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.appshare.R;
import com.appshare.adapter.AppListAdapter;
import com.appshare.adapter.ShareListAdapter;
import com.appshare.adapter.SimpleAppListAdapter;
import com.appshare.app.AppInfo;
import com.appshare.app.ShareInfo;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRecentFragment extends Fragment {
	private ListView list;
    private ArrayList<AppInfo> applist;
    private JSONArray array;
    private JSONObject obj;
    private String SERVER;
    private SharedPreferences share;
    private String friend_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.app_layout, container,
				false);
        share = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        SERVER = this.getString(R.string.server_ip);
		list = (ListView) rootview.findViewById(R.id.app_list);
        applist = new ArrayList<AppInfo>();
        AppList();
		list.setAdapter(new SimpleAppListAdapter(this.getActivity(), applist));

		return rootview;
	}
	
    public void AppList(){
        new AsyncTask<String, Float, String>(){
            String sd = Environment.getExternalStorageDirectory().getPath();
            @Override
            protected String doInBackground(String... arg0){
                friend_id=share.getString("FriendId","");
                    String nurl=SERVER+"/app.php?uid="+friend_id;
                long total = 0;
                InputStream iStream = null;
                try {
                    URL url = new URL(nurl);
                    URLConnection connection = url.openConnection();
                    total = connection.getContentLength();
                    iStream = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader isr = new InputStreamReader(iStream);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    StringBuilder builder = new StringBuilder();
                try {
                    while ((line = br.readLine())!=null){
                        builder.append(line);
                        publishProgress((float)builder.toString().length()/total);
                    }
                    br.close();
                    iStream.close();
                    obj = new JSONObject(builder.toString());
                    for(int i=1;i<6;i++) {
                        AppInfo appinfo = new AppInfo();
                        appinfo.appName = obj.getString(String.valueOf(i));
                        applist.add(appinfo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                list.setAdapter(new SimpleAppListAdapter(getActivity(), applist));
                //list.setAdapter(shareadapter);
                super.onPostExecute(result);
            }

            @Override
            protected void onProgressUpdate(Float... values) {
                // TODO Auto-generated method stub
                System.err.println(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String result) {
                // TODO Auto-generated method stub
                super.onCancelled(result);
            }

            @Override
            protected void onCancelled() {
                // TODO Auto-generated method stub
                super.onCancelled();
            }

        }.execute();



    }
}
