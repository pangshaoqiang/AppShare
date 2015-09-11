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

import com.appshare.FriendDetailActivity;
import com.appshare.R;
import com.appshare.adapter.FriendListAdapter;
import com.appshare.app.FriendInfo;
import com.appshare.base.AppTrans;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsFragment extends Fragment {
	private PullToRefreshListView ptrlist;
	private ListView list;
	private View rootview;
	private FriendListAdapter friendadapter;
	private ILoadingLayout startLabels;// ����
	private ILoadingLayout endLabels;// ����
    private String SERVER;
    private String userid;
    private JSONArray array;
    private JSONObject obj;
    private ArrayList<FriendInfo> friend;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        SERVER = this.getString(R.string.server_ip);
        share = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = share.edit();
        userid = share.getString("USER_ID", "");

        friend=new ArrayList<FriendInfo>();
        //friendadapter = new FriendListAdapter(getActivity(), friend);
        //list.setAdapter(friendadapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.friends_layout, container, false);

		ptrlist = (PullToRefreshListView) rootview
				.findViewById(R.id.friends_list);
		list = ptrlist.getRefreshableView();
        init();
        FriendsList(SERVER + "/friends.php",userid);


		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				TextView text1 = (TextView) view.findViewById(R.id.person_name);
				TextView text2 = (TextView) view
						.findViewById(R.id.person_telephone);
				ImageView img1 = (ImageView) view
						.findViewById(R.id.person_icon);

				String friend_name = (String) text1.getText();
				String friend_phone = (String) text2.getText();
				Drawable friend_icon = img1.getDrawable();

				((AppTrans) getActivity().getApplication())
						.setFriendName(friend_name);
				((AppTrans) getActivity().getApplication())
						.setFriendPhone(friend_phone);
				((AppTrans) getActivity().getApplication())
						.setFriendIcon(friend_icon);
                ((AppTrans) getActivity().getApplication())
                        .setSign(friend.get(position-1).sign);
                ((AppTrans) getActivity().getApplication())
                        .setFriendId(friend.get(position-1).FriendId);

                System.out.println(friend.get(position-1).sign+"0");
                editor.putString("FriendId", friend.get(position-1).FriendId);
                editor.putString("FriendName", friend.get(position-1).FriendName);
                editor.putString("FriendHead", friend.get(position-1).FriendHead);
                editor.putString("FriendTel", friend.get(position-1).FriendTel);
                editor.commit();
				Intent intent = new Intent(getActivity(),
						FriendDetailActivity.class);
				startActivity(intent);
			}
		});

		ptrlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				startLabels.setLastUpdatedLabel(label);
				new DownFinishRefresh().execute();
				//friendadapter.notifyDataSetChanged();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

			}

		});
		return rootview;
	}

	// ptr��ʼ��
	private void init() {
		startLabels = ptrlist.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(this.getResources().getString(R.string.reflash));// ������ʱ����ʾ����ʾ
        startLabels.setRefreshingLabel(this.getResources().getString(R.string.reflashing));// ˢ��ʱ
        startLabels.setReleaseLabel(this.getResources().getString(R.string.reflashend));// �����ﵽһ������ʱ����ʾ����ʾ

        endLabels = ptrlist.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(this.getResources().getString(R.string.load));// ������ʱ����ʾ����ʾ
        endLabels.setRefreshingLabel(this.getResources().getString(R.string.loading));// ˢ��ʱ
        endLabels.setReleaseLabel(this.getResources().getString(R.string.loadend));// �����ﵽһ������ʱ����ʾ����ʾ
	}

	private class DownFinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
            getList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			friendadapter.notifyDataSetChanged();

			ptrlist.onRefreshComplete();
            friendadapter = new FriendListAdapter(getActivity(), friend);
            list.setAdapter(friendadapter);
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
            byte[] buffer=new byte[8*1024];
            while(input.read(buffer)!=-1){
                output.write(buffer);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getList(){
        String head;
        BitmapDrawable drawable;
        friend=new ArrayList<FriendInfo>();
        try {
            userid = share.getString("USER_ID", "");
            System.out.println("t1");
            String newurl;
            newurl = SERVER+"/friends.php?uid="+userid;
            System.out.println(newurl);
            URL url = new URL(newurl);
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            InputStream iStream = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(iStream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine())!=null){
                builder.append(line);
            }
            br.close();
            iStream.close();
            try {
                array = new JSONArray(builder.toString());
                for(int i=0;i<array.length();i++){
                    FriendInfo friendInfo=new FriendInfo();
                    obj = array.getJSONObject(i);
                    friendInfo.FriendId = obj.getString(String.valueOf(0));
                    friendInfo.FriendName = obj.getString(String.valueOf(1));
                    friendInfo.FriendTel = obj.getString(String.valueOf(3));
                    friendInfo.sign=obj.getString(String.valueOf(5));

                    head = obj.getString(String.valueOf(4));
                    friendInfo.FriendHead = head;
                    Bitmap headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                    if(headimg == null) DownTheHead(head);
                    else headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                    drawable = new BitmapDrawable(getActivity().getResources(),headimg);
                    System.out.println("tt");
                    friendInfo.FriendIcon = drawable;
                    //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                    friend.add(friendInfo);
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

    }
	// ����ݿ��л�ȡ���
    public void FriendsList(String url,String uid){
        new AsyncTask<String, Float, String>(){
            String head;
            BitmapDrawable drawable;
            @Override
            protected String doInBackground(String... arg0){
                try {
                    friend=new ArrayList<FriendInfo>();
                    userid = share.getString("USER_ID", "");
                    System.out.println("t1");
                    String newurl;
                    newurl = arg0[0]+"?uid="+userid;
                    System.out.println(newurl);
                    URL url = new URL(newurl);
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
                            FriendInfo friendInfo=new FriendInfo();
                            obj = array.getJSONObject(i);
                            friendInfo.FriendId = obj.getString(String.valueOf(0));
                            friendInfo.FriendName = obj.getString(String.valueOf(1));
                            friendInfo.FriendTel = obj.getString(String.valueOf(3));
                            friendInfo.sign=obj.getString(String.valueOf(5));

                            head = obj.getString(String.valueOf(4));
                            friendInfo.FriendHead = head;
                            Bitmap headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                            if(headimg == null) DownTheHead(head);
                            else headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                            drawable = new BitmapDrawable(getActivity().getResources(),headimg);
                            System.out.println("tt");
                            friendInfo.FriendIcon = drawable;
                            //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                            friend.add(friendInfo);
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
                friendadapter = new FriendListAdapter(getActivity(), friend);
                list.setAdapter(friendadapter);
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

        }.execute(url,uid);
    }
}
