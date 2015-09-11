package com.appshare;

import com.appshare.adapter.CommentListAdapter;
import com.appshare.adapter.ShareListAdapter;
import com.appshare.app.CommentInfo;
import com.appshare.app.FriendInfo;
import com.appshare.app.ShareInfo;
import com.appshare.base.AppTrans;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;import android.view.LayoutInflater;import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;import android.widget.AdapterView;import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;import java.io.FileOutputStream;import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class  ShareDetailActivity extends Activity{
	private Button back_btn;
    private Button reflash_btn;
	private Button fav_btn;
	private Button like_btn;
	private Button comment_btn;
    private ListView list;
    private View rootview;
    private ILoadingLayout startLabels;// ����
    private ILoadingLayout endLabels;// ����
    private PullToRefreshListView ptrlist;
    private CommentListAdapter commentadapter;
    private ArrayList<CommentInfo> comment;
    private JSONArray array;
    private JSONObject obj;
	private ImageView share_app_icon;
	private ImageView share_user_icon;
	private TextView share_app_name;
	private TextView share_app_intro;
	private TextView share_user_name;
	private TextView detail_intro;
    private String share_id;
    private String user_id;
    private String app_name;
    private SharedPreferences share;
    private String SERVER;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_detail);
        share_id = ((AppTrans)getApplication()).getShareId();
        share = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        user_id = share.getString("USER_ID", "");
        app_name = ((AppTrans)getApplication()).getAppName();
        SERVER = this.getResources().getString(R.string.server_ip);

        //CommentInfo commentinfo = new CommentInfo();
        //commentinfo.content = "just test!";
        //comment.add(commentinfo);
        //commentadapter = new CommentListAdapter(this,comment);
        //list.setAdapter(commentadapter);
        initView();
		init();
        commentlist();
	}


    @Override
    public void onResume(){
        share_id = ((AppTrans)getApplication()).getShareId();
        user_id = share.getString("USER_ID", "");
        app_name = ((AppTrans)getApplication()).getAppName();
        super.onResume();
    }
    @Override
    public void onStart(){
        share_id = ((AppTrans)getApplication()).getShareId();
        user_id = share.getString("USER_ID", "");
        app_name = ((AppTrans)getApplication()).getAppName();
    super.onStart();
    }
	private void initView(){
		back_btn=(Button)findViewById(R.id.detail_return_btn);
		fav_btn=(Button)findViewById(R.id.detai_fav_icon);
		like_btn=(Button)findViewById(R.id.detail_share_icon);
		comment_btn=(Button)findViewById(R.id.detail_comment_icon);
		share_app_icon=(ImageView)findViewById(R.id.share_app_icon);
		share_user_icon=(ImageView)findViewById(R.id.share_user_icon);
		share_app_name=(TextView)findViewById(R.id.share_app_name);
		share_app_intro=(TextView)findViewById(R.id.share_app_intro);
		share_user_name=(TextView)findViewById(R.id.share_user_name);
		detail_intro=(TextView)findViewById(R.id.detail_intro);
        ptrlist = (PullToRefreshListView)findViewById(R.id.comment_list);
        list = ptrlist.getRefreshableView();
        //list =(ListView) findViewById(R.id.comment_list);
    }
	
	private void init(){
		share_app_icon.setImageDrawable(((AppTrans) getApplication()).getAppIcon());
		share_user_icon.setImageDrawable(((AppTrans)getApplication()).getShareIcon());
		share_app_name.setText(((AppTrans)getApplication()).getAppName());
		share_user_name.setText(((AppTrans)getApplication()).getShareName());
		share_app_intro.setText(((AppTrans)getApplication()).getBriefIntro());
        detail_intro.setText(((AppTrans)getApplication()).getDetailIntro());
        startLabels = ptrlist.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(this.getResources().getString(R.string.reflash));// ������ʱ����ʾ����ʾ
        startLabels.setRefreshingLabel(this.getResources().getString(R.string.reflashing));// ˢ��ʱ
        startLabels.setReleaseLabel(this.getResources().getString(R.string.reflashend));// �����ﵽһ������ʱ����ʾ����ʾ

        endLabels = ptrlist.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(this.getResources().getString(R.string.load));// ������ʱ����ʾ����ʾ
        endLabels.setRefreshingLabel(this.getResources().getString(R.string.loading));// ˢ��ʱ
        endLabels.setReleaseLabel(this.getResources().getString(R.string.loadend));// �����ﵽһ������ʱ����ʾ����ʾ
        ptrlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                String label = DateUtils.formatDateTime(ShareDetailActivity.this,
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
		fav_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                share_id = ((AppTrans)getApplication()).getShareId();
                user_id = share.getString("USER_ID", "");
                app_name = ((AppTrans)getApplication()).getAppName();
                DoACollection();

			}
		});


		like_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                DoALike();
                Toast.makeText(ShareDetailActivity.this, getResources().getString(R.string.dlike), Toast.LENGTH_LONG).show();
			}
		});
		comment_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShareDetailActivity.this,
						CommentActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,anim.fade_out);
			}
		});
		back_btn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				overridePendingTransition( anim.fade_in,R.anim.out_from_right); 
			}
		});
	}

    private class DownFinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            commentlist();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            commentadapter.notifyDataSetChanged();
            ptrlist.onRefreshComplete();
            commentadapter = new CommentListAdapter(ShareDetailActivity.this,comment);
            list.setAdapter(commentadapter);

        }
    }

    public void DoALike(){
        new AsyncTask<String, Float, String>(){

            @Override
            protected String doInBackground(String... arg0){
                DoLike();
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
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
    public String DoLike(){
        JSONObject jsonObject;
        URL url= null;
        String res = "";
        try {
            String appname= URLEncoder.encode(app_name, "UTF-8");
            url = new URL(SERVER+ "/do_like.php?appname=" + appname+"&shareid="+share_id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream iStream = null;
        try {
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            iStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(iStream);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            iStream.close();
            jsonObject = new JSONObject(builder.toString());
            res = jsonObject.getString("res");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void DoACollection(){
        new AsyncTask<String, Float, String>(){

            @Override
            protected String doInBackground(String... arg0){
                return DoCollect();
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(result.equals("success"))
                    Toast.makeText(ShareDetailActivity.this, getResources().getString(R.string.col), Toast.LENGTH_LONG).show();
                else if(result.equals("defect")) Toast.makeText(ShareDetailActivity.this, getResources().getString(R.string.coldef), Toast.LENGTH_LONG).show();
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

    public String DoCollect(){
        String res="";
        JSONObject jsonObject;
        URL url= null;
        try {
            String appname= URLEncoder.encode(app_name, "UTF-8");
            url = new URL(SERVER+ "/do_collect.php?app_name=" + appname+"&share_id="+share_id+"&collecter_id="+user_id);
            System.out.println(url.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream iStream = null;
        try {
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            iStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(iStream);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            iStream.close();
            jsonObject = new JSONObject(builder.toString());
            res = jsonObject.getString("res");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;

    }

    public void commentlist(){
        new AsyncTask<String, Float, String>(){

            @Override
            protected String doInBackground(String... arg0){
                comment = new ArrayList<CommentInfo>();
                String head = "";
                Drawable drawable;
                CommentInfo commentinfo;
                try {
                    String appname = URLEncoder.encode(app_name,"UTF-8");
                    System.out.println("get comment");
                    String theurl;
                    theurl = SERVER+"/comments.php?app_name="+appname+"&user_id="+share_id;
                    System.out.println(app_name+share_id);
                    URL url = new URL(theurl);
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
                            System.out.println("have");
                            commentinfo = new CommentInfo();
                            obj = array.getJSONObject(i);
                            commentinfo.user_name = obj.getString(String.valueOf(6));
                            commentinfo.content = obj.getString(String.valueOf(4));
                            commentinfo.time = obj.getString(String.valueOf(2));
                            head = obj.getString(String.valueOf(9));
                            Bitmap headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                            if(headimg == null) DownTheHead(head);
                            headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);;

                            drawable = new BitmapDrawable(getResources(),headimg);
                            System.out.println("tt");
                            commentinfo.head = drawable;
                            //friendInfo.FriendIcon = getResources().getDrawable(R.drawable.browser_share);
                            comment.add(commentinfo);
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
                commentadapter = new CommentListAdapter(ShareDetailActivity.this,comment);
                list.setAdapter(commentadapter);
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
}
