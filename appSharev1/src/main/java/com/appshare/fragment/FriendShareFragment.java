package com.appshare.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.appshare.R;
import com.appshare.adapter.ShareListAdapter;
import com.appshare.app.ShareInfo;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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

public class FriendShareFragment extends Fragment {
	private ListView list;
	private PullToRefreshListView ptrlist;
	private ShareListAdapter shareadapter;
	private ILoadingLayout startLabels;// ����
	private ILoadingLayout endLabels;// ����
	private ArrayList<ShareInfo> sharelist;
    private JSONArray array;
    private JSONObject obj;
    private String SERVER;
    private SharedPreferences share;
    private String friend_id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        SERVER = this.getString(R.string.server_ip);
        //ShareList();
        //ShareList(SERVER + "/share.php");
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.share_layout, container,false);
       friend_id=share.getString("FriendId","");
        sharelist=new ArrayList<ShareInfo>();
		ptrlist = (PullToRefreshListView) rootview.findViewById(R.id.share_list);
		ptrlist.setMode(Mode.DISABLED);
		list = ptrlist.getRefreshableView();
         ShareList();
		//shareadapter = new ShareListAdapter(getActivity(), sharelist);
		//list.setAdapter(shareadapter);
		//list = (ListView) rootview.findViewById(R.id.share_list);
		//list.setAdapter(new ShareListAdapter(this.getActivity(), getFriendShareData() ));

		return rootview;
	}

    public void ShareList(){
        new AsyncTask<String, Float, String>(){
            String icon;
            BitmapDrawable drawable;
            String sd = Environment.getExternalStorageDirectory().getPath();
            @Override
            protected String doInBackground(String... arg0){
                try {
                    System.out.println("t1");
                    String nurl=SERVER+"/share.php?uid="+friend_id;
                    System.out.println(nurl);
                    URL url = new URL(nurl);
                    URLConnection connection = url.openConnection();
                    long total = connection.getContentLength();
                    InputStream iStream = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(iStream);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = br.readLine())!=null){
                        builder.append(line);
                        publishProgress((float)builder.toString().length()/total);
                    }
                    br.close();
                    iStream.close();
                    try {
                        array = new JSONArray(builder.toString());
                        for(int i=0;i<array.length();i++){
                            ShareInfo shareinfo = new ShareInfo();
                            obj = array.getJSONObject(i);
                            shareinfo.userName = obj.getString(String.valueOf(10));
                            shareinfo.appName = obj.getString(String.valueOf(0));
                            shareinfo.appIntro= obj.getString(String.valueOf(3));
                            shareinfo.comment= obj.getString(String.valueOf(4));
                            shareinfo.user_id= obj.getString(String.valueOf(2));
                            shareinfo.user_head= obj.getString(String.valueOf(13));
                            shareinfo.app_icon= obj.getString(String.valueOf(1));
                            shareinfo.shareTime= obj.getString(String.valueOf(5));
                            shareinfo.commentNum= obj.getString(String.valueOf(8));
                            shareinfo.likeNum= obj.getString(String.valueOf(6));
                            shareinfo.favNum= obj.getString(String.valueOf(7));
                            shareinfo.sign= "";//obj.getString(String.valueOf(14));
                            Bitmap headimg = BitmapFactory.decodeFile(sd+"/AppShare/head/" + shareinfo.user_head);
                            if(headimg == null) {
                                DownTheHead(shareinfo.user_head);
                                headimg = BitmapFactory.decodeFile(sd+"/AppShare/head/" + shareinfo.user_head);
                            }
                            if(headimg != null) {
                                drawable = new BitmapDrawable(getActivity().getResources(),headimg);
                                shareinfo.userIcon = drawable;
                            }
                            Bitmap iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                            if(iconimg == null) {
                                System.out.println("ic1");
                                DownTheIcon(shareinfo.app_icon);
                                iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                            }
                            if(iconimg !=null) {
                                System.out.println("ic2");
                                drawable = new BitmapDrawable(getActivity().getResources(), iconimg);
                                shareinfo.appIcon = drawable;
                            }
                            //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                            /*friend.add(friendInfo);*/
                            sharelist.add(shareinfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //res = re_tag;

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
                shareadapter = new ShareListAdapter(getActivity(),sharelist);
                list.setAdapter(shareadapter);
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
    public void DownTheIcon(String icon){
        String SERVER = (String) this.getResources().getString(R.string.server_ip);;
        String headurl = SERVER + "/icon/" + icon;
        OutputStream output=null;
        try {
            URL url = new URL(headurl);
            URLConnection connection = url.openConnection();
            String DownPath = "/sdcard/AppShare/icon/" + icon;
            File file = new File(DownPath);
            InputStream input=connection.getInputStream();
            file.createNewFile();
            output=new FileOutputStream(file);
            byte[] buffer=new byte[4*1024];
            while(input.read(buffer)!=-1){
                output.write(buffer);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void DownTheHead(String head){
        String SERVER = (String) this.getResources().getString(R.string.server_ip);;
        String headurl = SERVER + "/head/" + head;
        OutputStream output=null;
        try {
            URL url = new URL(headurl);
            URLConnection connection = url.openConnection();
            String DownPath = "/sdcard/AppShare/head/" + head;
            File file = new File(DownPath);
            InputStream input=connection.getInputStream();
            file.createNewFile();
            output=new FileOutputStream(file);
            byte[] buffer=new byte[4*1024];
            while(input.read(buffer)!=-1){
                output.write(buffer);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
