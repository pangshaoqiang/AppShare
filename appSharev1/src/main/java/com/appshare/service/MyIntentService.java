package com.appshare.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appshare.MySQLiteHelper;
import com.appshare.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private int count;
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.appshare.service.action.FOO";
    private static final String ACTION_BAZ = "com.appshare.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.appshare.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.appshare.service.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
        MySQLiteHelper mySQLiteHelper;
        SQLiteDatabase db;
        String user_id;
        ActivityManager am;
        List<ActivityManager.RunningAppProcessInfo> run ;
        PackageManager pm;
        List<String> apps;
        mySQLiteHelper = new MySQLiteHelper(this,"AppShare.db",null,1);
        db = mySQLiteHelper.getReadableDatabase();
        String SERVER = this.getResources().getString(R.string.server_ip);
        share = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = share.edit();
        for(int i=0;i<=144*7;i++){
            try {
                Thread.sleep(1000*60*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user_id = share.getString("USER_ID", "");
            count = share.getInt("COUNT", 0);
            //System.out.println(count);
            count ++;
            editor.putInt("COUNT",count);
            editor.commit();
            am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            run = am.getRunningAppProcesses();
            pm =getPackageManager();
            apps = new ArrayList<String>();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            for(ActivityManager.RunningAppProcessInfo ra : run){
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(ra.processName,0);
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //System.out.println(packageInfo.applicationInfo.loadLabel(pm).toString());
                        apps.add(packageInfo.applicationInfo.loadLabel(pm).toString());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            for(int j=0;j<apps.size();j++){
                int recorder = 0;
                Cursor cursor = db.rawQuery("SELECT record FROM app_use WHERE appname=? and user_id=?",new String[]{apps.get(j),user_id});
                if(cursor.moveToNext()) {
                    recorder = cursor.getInt(cursor.getColumnIndex("record"));
                    System.out.println(recorder);
                    recorder = recorder + 1;
                    ContentValues value = new ContentValues();
                    value.put("record",recorder);
                    db.update("app_use",value,"appname=? and user_id=?",new String[]{apps.get(j),user_id});
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put("record",0);
                    values.put("appname",apps.get(j));
                    values.put("user_id",user_id);
                    db.insert("app_use",null,values);
                }
                            //apps.get(j);
                }

            if(count>=144) {
            //update
                Cursor cursor = db.rawQuery("SELECT appname FROM app_use WHERE user_id=? order by record desc limit 5",new String[]{user_id});

                HttpClient httpclinet = new DefaultHttpClient();
                HttpPost request = new HttpPost(SERVER+"/app_use_update.php");
                int d=0;
                while(cursor.moveToNext()){
                    if(d==0) nameValuePair.add(new BasicNameValuePair("a_app",cursor.getString(cursor.getColumnIndex("appname"))));
                    else if(d==1) nameValuePair.add(new BasicNameValuePair("b_app",cursor.getString(cursor.getColumnIndex("appname"))));
                    else if(d==2) nameValuePair.add(new BasicNameValuePair("c_app",cursor.getString(cursor.getColumnIndex("appname"))));
                    else if(d==3) nameValuePair.add(new BasicNameValuePair("d_app",cursor.getString(cursor.getColumnIndex("appname"))));
                    else if(d==4) nameValuePair.add(new BasicNameValuePair("e_app",cursor.getString(cursor.getColumnIndex("appname"))));
                    d++;
                }
                nameValuePair.add(new BasicNameValuePair("user_id",user_id));
                //nameValuePair.add(new BasicNameValuePair("contacter",jsonObject.toString()));
                try {

                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8);
                    request.setEntity(formEntity);
                    httpclinet.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //i=0
                db.delete("app_use","user_id=?",new String[]{user_id});
                count = 0;
                editor.putInt("COUNT",count);
                editor.commit();
            }

            }


        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("service starts");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void Census(){

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        PackageManager pm =getPackageManager();
        List<String> apps = new ArrayList<String>();


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
}
