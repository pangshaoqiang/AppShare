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
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshare.adapter.ShareListAdapter;
import com.appshare.adapter.SlidePagerAdapter;
import com.appshare.app.ShareInfo;
import com.appshare.base.AppTrans;
import com.appshare.fragment.FriendRecentFragment;
import com.appshare.fragment.FriendShareFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendDetailActivity extends FragmentActivity{

		public List<Fragment> fragments = new ArrayList<Fragment>();

		private ViewPager mPager;// ��������һ��ViewPagerʵ��
		private PagerAdapter mAdapter;// ViewPager������

		private TextView friend_recent;
		private TextView friend_share;
		private TextView friend_name;
		private TextView friend_phone;
		private ImageView friend_icon;
        private TextView sign;
        private TextView friend_share_num;
        private String SERVER;
        private JSONArray array;
        private String friend_id;


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.friend_detail);
            SERVER = this.getResources().getString(R.string.server_ip);
			fragments.add(new FriendRecentFragment());
			fragments.add(new FriendShareFragment());
			setWindow();
			initView();
			getFragmentManager();
            friend_id=((AppTrans)getApplication()).getFriendId();
            ShareNum();
			mPager = (ViewPager) findViewById(R.id.friend_pager);
			mAdapter = new SlidePagerAdapter(this.getSupportFragmentManager(), mPager,fragments);
			
			mPager.setAdapter(mAdapter);
			mPager.setCurrentItem(0);
		}

		private void initView() {
			friend_name=(TextView)findViewById(R.id.friend_name);
			friend_phone=(TextView)findViewById(R.id.friend_phone);
			friend_icon=(ImageView)findViewById(R.id.friend_head_icon);
			friend_name.setText(((AppTrans)getApplication()).getFriendName());
			friend_phone.setText(((AppTrans)getApplication()).getFriendphone());
			friend_icon.setImageDrawable(((AppTrans)getApplication()).getFriendIcon());
			friend_recent= (TextView) findViewById(R.id.friend_recent_app);
			friend_share= (TextView) findViewById(R.id.friend_share);
            friend_share_num= (TextView) findViewById(R.id.friend_share_num);
            sign = (TextView) findViewById(R.id.friend_sign);
            sign.setText(((AppTrans)getApplication()).getSign());
            System.out.println(((AppTrans)getApplication()).getSign());
			friend_recent.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPager.setCurrentItem(0);
				}
				
			});
			friend_share.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPager.setCurrentItem(1);
				}
				
			});
		}


		private void setWindow() {
			WindowManager m = getWindowManager();
			Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ�?��
			Point outsize = new Point();
			LayoutParams p = getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
			d.getSize(outsize);
			p.height = (int) (outsize.y * 0.8); // �߶�����Ϊ��Ļ��0.8
			p.width = (int) (outsize.x * 0.8); // �������Ϊ��Ļ��0.8
			p.alpha = 1.0f; // ���ñ���͸����
			p.dimAmount = 0.0f; // ���úڰ���

			getWindow().setAttributes(p); // ������Ч
			getWindow().setGravity(Gravity.CENTER); // ���ÿ��Ҷ���
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
    public void ShareNum(){
        new AsyncTask<String, Float, String>(){
            BitmapDrawable drawable;
            String sd = Environment.getExternalStorageDirectory().getPath();
            @Override
            protected String doInBackground(String... arg0){

                    System.out.println(sd);
                    String nurl=SERVER+"/share.php?uid="+friend_id;
                    System.out.println(nurl);
                    URL url=null;
                try {
                    url = new URL(nurl);
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
                    array = new JSONArray(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("leng");
                System.out.println(array.length());
                 return String.valueOf(array.length());
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                friend_share_num.setText(result);
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
