package com.appshare;

import com.appshare.base.AppTrans;

import android.R.anim;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ShareActivity extends Activity {

	private TextView appName;
	private ImageView appIcon;
	private TextView brief_intro;
	private TextView app_intro;
	private Button back__btn;
	private Button share_btn;
    private String sketch;
    private String comment;
    private String userid;
    private SharedPreferences share;
    private String SERVER;
    private String uploadFile;
    private String srcPath;
    private String actionURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        SERVER = getResources().getString(R.string.server_ip);
        actionURL = SERVER+"/upload.php";
        srcPath = "/sdcard/AppShare/head/11111.bmp";
        uploadFile = "/sdcard/AppShare/head/11111.bmp";
        share = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        userid = share.getString("USER_ID","");
		String share_app_name=((AppTrans) getApplication()).getValue();
		Drawable share_app_icon=((AppTrans)getApplication()).getIcon();
		setContentView(R.layout.share);
		initview();
		appName.setText(share_app_name);
		appIcon.setImageDrawable(share_app_icon);
		back__btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				finish();
				overridePendingTransition( anim.fade_in,R.anim.out_from_right); 
				
			}
		});
		share_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                sketch = brief_intro.getText().toString();
                comment = app_intro.getText().toString();
                //System.out.println("a");
                share();
                //System.out.println("z");
				Toast.makeText(ShareActivity.this, getResources().getString(R.string.ShareSucc), Toast.LENGTH_LONG).show();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share, menu);
		
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void initview(){	
		appName=(TextView)findViewById(R.id.share_app_name);
		appIcon=(ImageView)findViewById(R.id.share_app_icon);
		back__btn=(Button)findViewById(R.id.share_return_btn);
		share_btn=(Button)findViewById(R.id.share_btn);
		brief_intro=(TextView)findViewById(R.id.brief_app_intro);
		app_intro=(TextView)findViewById(R.id.app_intro);
	}

    public void share(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                userid = share.getString("USER_ID","");
                UpdateInfo();
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
    public void UpdateInfo() {
        JSONObject jsonObject;
        String res;
        URL url=null;
        String appname =((AppTrans) getApplication()).getValue();
        String icon="def.png";
        appname=appname.replace(".","_");
        appname=appname.replace(" ","_");

        //icon = icon.replace(".","");
        //icon = icon.replace(" ","_");
        try {
            appname= URLEncoder.encode(appname,"UTF-8");
            sketch = URLEncoder.encode(sketch,"UTF-8");
            comment = URLEncoder.encode(comment,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            url= new URL((String) this.getResources().getString(R.string.server_ip) + "/share_app.php?uid="+userid+"&app_icon="+icon+"&app_name="+appname+"&sketch="+sketch+"&comment="+comment);
            System.out.println(url.toString());
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
            jsonObject = new JSONObject(builder.toString());
            res = jsonObject.getString("res");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
