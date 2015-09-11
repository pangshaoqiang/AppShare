package com.appshare;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appshare.adapter.SlidePagerAdapter;
import com.appshare.app.AppInfo;
import com.appshare.fragment.AppFragment;
import com.appshare.fragment.FriendsFragment;
import com.appshare.fragment.LeftMenuFragment;
import com.appshare.fragment.NewsFragment;
import com.appshare.service.MyIntentService;
import com.appshare.view.LeftMenuView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMStringList;

public class MainActivity extends FragmentActivity {

	public List<Fragment> fragments = new ArrayList<Fragment>();

	SlidingMenu side_drawer;
	/** head ͷ�� �����˵� ��ť */
	private ImageView top_head;
	private Button top_more;
	private PopupWindow popupwindow;

	private int mCurPage = 0;// ��ǰҳ
	private ViewPager mPager;// ��������һ��ViewPagerʵ��
	private PagerAdapter mAdapter;// ViewPager������

	private ImageView appImage;
	private ImageView friendsImage;
	private ImageView newsImage;
    private ImageView myhead;

	private TextView appText;
	private TextView friendsText;
	private TextView newsText;

    private String userid;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private String SERVER;
    private String value;
    private String head;
    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragments.add(new AppFragment());
		fragments.add(new FriendsFragment());
		fragments.add(new NewsFragment());
        SERVER = (String) this.getResources().getString(R.string.server_ip);
        share = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = share.edit();
        userid = share.getString("USER_ID", "");
        Census3();
        String head_dir = Environment.getExternalStorageDirectory().getPath()+"/AppShare/head";
        File head_ddir= new File(head_dir);
        if(!head_ddir.exists()) head_ddir.mkdirs(); else System.out.println(head_dir+" exists");
        String icon_dir = Environment.getExternalStorageDirectory().getPath()+"/AppShare/icon";
        File icon_ddir= new File(icon_dir);
        if(!icon_ddir.exists()) icon_ddir.mkdirs(); else System.out.println(icon_dir+" exists");

        //mySQLiteHelper = new MySQLiteHelper(this,"AppShare.db",null,1);
        //db = mySQLiteHelper.getReadableDatabase();

        //SQLtask();
        //Contacter();
        GetMyInfoTask();
		initView();
		side_drawer = new LeftMenuView(this).initSlidingMenu();
		getFragmentManager();
		mPager = (ViewPager) findViewById(R.id.mViewPager);
		mAdapter = new SlidePagerAdapter(this.getSupportFragmentManager(),
				mPager, fragments);

		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setCurrentItem(0);
	}

	private void initView() {
		appImage = (ImageView) findViewById(R.id.app_image);
		appImage.setImageResource(R.drawable.forum_icon_app_pressed);
		friendsImage = (ImageView) findViewById(R.id.friends_image);
		newsImage = (ImageView) findViewById(R.id.news_image);
		appText = (TextView) findViewById(R.id.app_text);
		friendsText = (TextView) findViewById(R.id.friends_text);
		newsText = (TextView) findViewById(R.id.news_text);
		appImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(0);
			}

		});
		friendsImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(1);
			}

		});
		newsImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(2);
			}

		});
		top_head = (ImageView) findViewById(R.id.top_head);
		top_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (side_drawer.isMenuShowing()) {
					side_drawer.showContent();
				} else {
					side_drawer.showMenu();
				}
			}
		});
		top_more=(Button)findViewById(R.id.top_more);
		top_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                System.out.println("pop");
				  if (popupwindow != null&&popupwindow.isShowing()) {  
		                popupwindow.dismiss();  
		                return;  
		            } else {  
		                initmPopupWindowView();  
		                popupwindow.showAsDropDown(v, 0, 5);  
		            }
			}
		});
	}

    @Override
    public void onResume(){
        System.out.println("resume");
        userid = share.getString("USER_ID", "");
        System.out.println("user_id:"+userid);
        GetMyInfoTask();
        side_drawer = new LeftMenuView(this).initSlidingMenu();
        super.onResume();
    }
    @Override
    public void onStart(){
        System.out.println("start");
        userid = share.getString("USER_ID", "");
        System.out.println("user_id:"+userid);
        GetMyInfoTask();
        side_drawer = new LeftMenuView(this).initSlidingMenu();
        super.onStart();
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

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (side_drawer.isMenuShowing()
					|| side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, this.getResources().getString(R.string.out), Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					System.exit(0);
					//finish();
				}
			}
			return true;
		}
		// ����MENU��ť����¼����������κβ���
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageSelected(int arg0) {

			System.out.print("��ǰview��" + arg0);
			switch (arg0) {
			case 0:
				if (mCurPage == 1) {
					appImage.setImageResource(R.drawable.forum_icon_app_pressed);
					appText.setTextColor(Color.WHITE);
					friendsImage
							.setImageResource(R.drawable.contacts_unselected);
					friendsText.setTextColor(Color.BLACK);
				}
				break;
			case 1:
				if (mCurPage == 0) {
					appImage.setImageResource(R.drawable.forum_icon_app);
					appText.setTextColor(Color.BLACK);
					friendsImage
							.setImageResource(R.drawable.contacts_selected);
					friendsText.setTextColor(Color.WHITE);
				}
				if (mCurPage == 2) {
					friendsImage
							.setImageResource(R.drawable.contacts_selected);
					friendsText.setTextColor(Color.WHITE);
					newsImage
							.setImageResource(R.drawable.news_unselected);
					newsText.setTextColor(Color.BLACK);
				}
				break;
			case 2:
				if (mCurPage == 1) {
					newsImage
							.setImageResource(R.drawable.news_selected);
					newsText.setTextColor(Color.WHITE);
					friendsImage
							.setImageResource(R.drawable.contacts_unselected);
					friendsText.setTextColor(Color.BLACK);
				}
				break;
			}
			mCurPage = arg0;
		}
	}
	
	public void initmPopupWindowView() {  
		  
        // // ��ȡ�Զ��岼���ļ�pop.xml����ͼ  
        View customView = getLayoutInflater().inflate(R.layout.main_popwindow,  
                null, false);  
        // ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�  
        popupwindow = new PopupWindow(customView, 250, 280);  
        // ���ö���Ч�� [R.style.AnimationFade ���Լ����ȶ���õ�]  
        popupwindow.setAnimationStyle(R.style.AnimationFade);
        //�ɴ������ಿλ
        popupwindow.setOutsideTouchable(true);
        // �Զ���view��Ӵ����¼�  
        customView.setOnTouchListener(new OnTouchListener() { 

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupwindow.dismiss();
					
					return true;
				}

				return true;
			}
        });  
  
        /** ���������ʵ���Զ�����ͼ�Ĺ��� */  
        final LinearLayout add_friend_btnButton=(LinearLayout)customView.findViewById(R.id.add_friend_btn);
        add_friend_btnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Contacter();
                //Census();
                //Census2();
                //Census3();
                System.out.println("pop");
			}
		});
  
    }
    public void GetMyInfo(String userid) {
        Integer res = 0;
        URL url = null;
        try {
            try {
                url = new URL(SERVER+ "/get_user_info.php?uid=" + userid);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //System.out.println(url);
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            InputStream iStream = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(iStream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            iStream.close();
            JSONObject jsonObject = new JSONObject(builder.toString());
            editor.putString("PHONE", jsonObject.getString("phone"));
            editor.putString("HEAD", jsonObject.getString("head"));
            editor.putString("USER_NAME", jsonObject.getString("user_name"));
            editor.putString("SIGN", jsonObject.getString("sign"));
            editor.putString("SEX", jsonObject.getString("sex"));
            System.out.println(builder.toString());
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

    public void GetMyInfoTask(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                GetMyInfo(userid);
                head = share.getString("HEAD","");
                Bitmap headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                if(headimg == null) DownTheHead(head);
                //System.out.println(head);
                //System.out.println(share.getString("PHONE",""));
                //System.out.println(share.getString("USER_NAME",""));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                value = result;
                SetMyHead(R.id.top_head);
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

    public void SetMyHead(int id){
        myhead = (ImageView) findViewById(id);
        Bitmap tmp = BitmapFactory.decodeFile("/sdcard/AppShare/head/"+head);
        Drawable drawable = new BitmapDrawable(null,tmp);
        if(drawable!=null)
        myhead.setImageDrawable(drawable);
    }



    public void SQLtask(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日");
                Date curDate = new   Date(System.currentTimeMillis());
                String  today   =  formatter.format(curDate);
                String insert = "insert into app_use(appname,thedate) values('haha','" + today +"');";
                db.execSQL(insert);
                Cursor cursor = db.query("app_use",new String[]{"appname","thedate"},null,null,null,null,null);

                System.out.println("app_use");
                while(cursor.moveToNext()){
                    String appname = cursor.getString(cursor.getColumnIndex("appname"));
                    System.out.println(appname);
                    String time = cursor.getString(cursor.getColumnIndex("thedate"));
                    System.out.println(time);
                }
                db.close();
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

    public void Contacter(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                int num=0;
                userid = share.getString("USER_ID", "");
                HttpClient httpclinet = new DefaultHttpClient();
                HttpPost request = new HttpPost(SERVER+"/add_friend.php");


                ArrayList phonenums = new ArrayList();
                JSONObject jsonObject = new JSONObject();
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                if(cur.moveToFirst()){
                    int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
                    int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    do{
                        String contactId = cur.getString(idColumn);
                        String disPlayName = cur.getString(displayNameColumn);
                        int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if(phoneCount>0){
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId,null,null);
                            if(phones.moveToFirst()){
                                do{
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    phonenums.add(phoneNumber.replace("-","").replace(" ",""));
                                    System.out.println(phoneNumber);
                                    try {
                                        jsonObject.put(String.valueOf(num),phoneNumber.replace("-","").replace(" ",""));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }while(phones.moveToNext());

                            }

                        }
                    }while(cur.moveToNext());
                }
                nameValuePair.add(new BasicNameValuePair("user_id",userid));
                nameValuePair.add(new BasicNameValuePair("contacter",jsonObject.toString()));
                HttpResponse response=null;
                try {
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePair);
                    request.setEntity(formEntity);
                    response = httpclinet.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(response!=null)
                System.out.println(response.toString());
                System.out.println("running");
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

    public void Census(){
        //PackageManager pm =getPackageManager();
        //ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        new ArrayList<Map<String, Object>>();
        List<PackageInfo> packages = getPackageManager()
                .getInstalledPackages(0);
        new HashMap<String, Object>();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
                    getPackageManager()).toString();
            String dir = packageInfo.applicationInfo.publicSourceDir;
            if(packageInfo.applicationInfo.className!=null)
            getCount(packageInfo.applicationInfo);
            System.out.println(packageInfo.applicationInfo.packageName+":"+packageInfo.applicationInfo.className);
        }
    }

    public void getCount(ApplicationInfo app){

        ComponentName Name = new ComponentName(app.packageName,app.className);
        int LaunchCount = 0;
        long UseTime = 0;
        try {
            //获得ServiceManager类
            Class<?> ServiceManager = Class
                    .forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", String.class);

            //调用getService获取RemoteService
            Object oRemoteService = null;
            try {
                oRemoteService = getService.invoke(null, "usagestats");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            //获得IUsageStats.Stub类
            Class<?> cStub = Class
                    .forName("com.android.internal.app.IUsageStats$Stub");
            //获得asInterface方法
            Method asInterface =null;
            if(cStub!=null) {
               asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            }
            //调用asInterface方法获取IUsageStats对象
            Object oIUsageStats = null;
            try {
                if(asInterface != null)
                oIUsageStats = asInterface.invoke(null, oRemoteService);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //获得getPkgUsageStats(ComponentName)方法
            Method getPkgUsageStats= null;
            if(oIUsageStats!=null) {
                getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
            }//调用getPkgUsageStats 获取PkgUsageStats对象
            Object Stats = null;
            try {
                if(getPkgUsageStats!=null)
                Stats = getPkgUsageStats.invoke(oIUsageStats, Name);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            //获得PkgUsageStats类

            Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
            if(PkgUsageStats!=null) {
                LaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(Stats);
                UseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(Stats);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    System.out.println("count:"+LaunchCount+";"+UseTime);

    }

    public void Census2(){

        //PackageInfo pi = PackageInfo.getInstance(this);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        PackageManager pm =getPackageManager();
        List<String> apps = new ArrayList<String>();
        //List<PackageInfo> packages = pm.getInstalledPackages(0);

        try {
            for(ActivityManager.RunningAppProcessInfo ra : run){
                if(ra.processName.equals("system") || ra.processName.equals("com.Android.phone")){ //可以根据需要屏蔽掉一些进程
                    continue;
                }
                PackageInfo packageInfo = pm.getPackageInfo(ra.processName,0);
                //System.out.println(ra.processName+":"+ra.uid +":" + ra.pid);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    //System.out.println(packageInfo.applicationInfo.loadLabel(pm).toString());
                    apps.add(packageInfo.applicationInfo.loadLabel(pm).toString());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        apps.add("new");
        apps.add("new");
        System.out.println("size:"+apps.size());
        for(int i=0;i<apps.size();i++)
            System.out.println(apps.get(i));
    }

    public void Census3(){
        Intent intent=new Intent(this,MyIntentService.class);
        stopService(intent);
        startService(intent);
    }
}
