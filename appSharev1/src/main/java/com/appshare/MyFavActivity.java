package com.appshare;

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

import com.appshare.adapter.ShareListAdapter;
import com.appshare.app.ShareInfo;
import com.appshare.base.AppTrans;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFavActivity extends Activity {
	private ListView myfavlist;
	private Button my_fav_btn;
	private PullToRefreshListView ptrlist;
	private ShareListAdapter myfavadapter;
	private ILoadingLayout startLabels;// ����
	private ILoadingLayout endLabels;// ����
    private ArrayList<ShareInfo> shares;
    private JSONArray array;
    private JSONObject obj;
    private String SERVER;
    private SharedPreferences share;
    private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        share = getSharedPreferences("userInfo", MODE_PRIVATE);
        SERVER = this.getString(R.string.server_ip);
        shares = new ArrayList<ShareInfo>();

		setContentView(R.layout.my_fav);
		ptrlist = (PullToRefreshListView) findViewById(R.id.my_fav_list);
		myfavlist = ptrlist.getRefreshableView();
		my_fav_btn=(Button)findViewById(R.id.my_fav_return_btn);
		init();
        ShareList();
		my_fav_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.in_from_left,
						anim.fade_out);
			}
		});
		ptrlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(MyFavActivity.this,
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
		myfavlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				TextView text1 = (TextView) view
						.findViewById(R.id.share_user_name);
				TextView text2 = (TextView) view
						.findViewById(R.id.share_app_name);
				TextView text3 = (TextView) view
						.findViewById(R.id.share_app_intro);
				ImageView img1 = (ImageView) view
						.findViewById(R.id.share_user_icon);
				ImageView img2 = (ImageView) view
						.findViewById(R.id.share_app_icon);
                ((AppTrans) getApplication())
                        .setDetailIntro(shares.get(position-1).comment);
                ((AppTrans) getApplication())
                        .setShareId(shares.get(position-1).user_id);

				String share_name = (String) text1.getText();
				String share_app_name = (String) text2.getText();
				String brief_intro = (String) text3.getText();
				Drawable share_icon = img1.getDrawable();
				Drawable app_icon = img2.getDrawable();

				((AppTrans) getApplication())
						.setShareName(share_name);
				((AppTrans) getApplication())
						.setAppName(share_app_name);
				((AppTrans) getApplication())
						.setBriefIntro(brief_intro);
				((AppTrans) getApplication())
						.setShareIcon(share_icon);
				((AppTrans) getApplication())
						.setAppIcon(app_icon);

				Intent intent = new Intent(MyFavActivity.this,
						ShareDetailActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						anim.fade_out);
			}
		});

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
            getMyFav();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			myfavadapter.notifyDataSetChanged();
			ptrlist.onRefreshComplete();
            myfavadapter = new ShareListAdapter(MyFavActivity.this, shares);
            myfavlist.setAdapter(myfavadapter);
		}
	}

    public void ShareList(){
        new AsyncTask<String, Float, String>(){
            String icon;
            BitmapDrawable drawable;
            String sd = Environment.getExternalStorageDirectory().getPath();

            @Override
            protected String doInBackground(String... arg0){
                user_id = share.getString("USER_ID", "");
                shares = new ArrayList<ShareInfo>();
                try {
                    System.out.println(sd);
                    String nurl=SERVER+"/collects.php?uid="+user_id;
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
                            shareinfo.userName = obj.getString(String.valueOf(1));
                            shareinfo.appName = obj.getString(String.valueOf(8));
                            shareinfo.appIntro= obj.getString(String.valueOf(14));
                            shareinfo.comment= obj.getString(String.valueOf(15));
                            shareinfo.user_id= obj.getString(String.valueOf(0));
                            shareinfo.user_head= obj.getString(String.valueOf(4));
                            shareinfo.app_icon= obj.getString(String.valueOf(12));
                            shareinfo.shareTime= obj.getString(String.valueOf(16));
                            shareinfo.commentNum= obj.getString(String.valueOf(19));
                            shareinfo.likeNum= obj.getString(String.valueOf(17));
                            shareinfo.favNum= obj.getString(String.valueOf(18));
                            shareinfo.sign= obj.getString(String.valueOf(5));
                            System.out.println(shareinfo.user_head+"tag"+shareinfo.app_icon);
                            Bitmap headimg = BitmapFactory.decodeFile(sd + "/AppShare/head/" + shareinfo.user_head);
                            if(headimg == null) {
                                //DownTheHead(shareinfo.user_head);
                                headimg = BitmapFactory.decodeFile(sd+"/AppShare/head/" + shareinfo.user_head);
                            }
                            if(headimg != null) {
                                System.out.println("head exist");
                                drawable = new BitmapDrawable(getResources(),headimg);
                                shareinfo.userIcon = drawable;
                            }
                            Bitmap iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                            System.out.println(shareinfo.userName);
                            if(iconimg == null) {
                                System.out.println("ic1");
                                DownTheIcon(shareinfo.app_icon);
                                iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                            }
                            if(iconimg !=null) {
                                System.out.println("ic2");
                                drawable = new BitmapDrawable(getResources(), iconimg );
                                shareinfo.appIcon = drawable;
                            }
                            //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                            /*friend.add(friendInfo);*/
                            shares.add(shareinfo);
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
                myfavadapter = new ShareListAdapter(MyFavActivity.this, shares);
                myfavlist.setAdapter(myfavadapter);
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

    public void getMyFav(){
        BitmapDrawable drawable;
        String sd = Environment.getExternalStorageDirectory().getPath();
        user_id = share.getString("USER_ID", "");
        shares = new ArrayList<ShareInfo>();
        try {
        String nurl=SERVER+"/collects.php?uid="+user_id;
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

        }
        br.close();
        iStream.close();
        try {
            array = new JSONArray(builder.toString());
            for(int i=0;i<array.length();i++){
                ShareInfo shareinfo = new ShareInfo();
                obj = array.getJSONObject(i);
                shareinfo.userName = obj.getString(String.valueOf(1));
                shareinfo.appName = obj.getString(String.valueOf(8));
                shareinfo.appIntro= obj.getString(String.valueOf(14));
                shareinfo.comment= obj.getString(String.valueOf(15));
                shareinfo.user_id= obj.getString(String.valueOf(0));
                shareinfo.user_head= obj.getString(String.valueOf(4));
                shareinfo.app_icon= obj.getString(String.valueOf(12));
                shareinfo.shareTime= obj.getString(String.valueOf(16));
                shareinfo.commentNum= obj.getString(String.valueOf(19));
                shareinfo.likeNum= obj.getString(String.valueOf(17));
                shareinfo.favNum= obj.getString(String.valueOf(18));
                shareinfo.sign= obj.getString(String.valueOf(5));
                System.out.println(shareinfo.user_head+"tag"+shareinfo.app_icon);
                Bitmap headimg = BitmapFactory.decodeFile(sd + "/AppShare/head/" + shareinfo.user_head);
                if(headimg == null) {
                    //DownTheHead(shareinfo.user_head);
                    headimg = BitmapFactory.decodeFile(sd+"/AppShare/head/" + shareinfo.user_head);
                }
                if(headimg != null) {
                    System.out.println("head exist");
                    drawable = new BitmapDrawable(getResources(),headimg);
                    shareinfo.userIcon = drawable;
                }
                Bitmap iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                System.out.println(shareinfo.userName);
                if(iconimg == null) {
                    System.out.println("ic1");
                    DownTheIcon(shareinfo.app_icon);
                    iconimg = BitmapFactory.decodeFile(sd+"/AppShare/icon/" + shareinfo.app_icon);
                }
                if(iconimg !=null) {
                    System.out.println("ic2");
                    drawable = new BitmapDrawable(getResources(), iconimg );
                    shareinfo.appIcon = drawable;
                }
                //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                            /*friend.add(friendInfo);*/
                shares.add(shareinfo);
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
}
