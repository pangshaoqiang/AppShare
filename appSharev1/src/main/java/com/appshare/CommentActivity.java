package com.appshare;

import android.R.anim;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appshare.base.AppTrans;

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

public class CommentActivity extends Activity{
	private Button back_btn,comment_btn;
	private EditText content;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private String user_id;
    private String commenter_id;
    private String app_name;
    String SERVER;
    String comment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_layout);
        SERVER = getResources().getString(R.string.server_ip);
        share = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		initView();
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition( anim.fade_in,R.anim.out_from_right); 
			}
		});
		comment_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				comment=content.getText().toString();
				//������ݿ�
                commenter_id = share.getString("USER_ID", "");
                user_id = ((AppTrans)getApplication()).getShareId();
                app_name = ((AppTrans)getApplication()).getAppName();
                comment();
				
				finish();

			}
		});
	}
    @Override
    public void onResume(){
        commenter_id = share.getString("USER_ID", "");
        user_id = ((AppTrans)getApplication()).getShareId();
        app_name = ((AppTrans)getApplication()).getAppName();
        super.onResume();
    }
    @Override
    public void onStart(){
        commenter_id = share.getString("USER_ID", "");
        user_id = ((AppTrans)getApplication()).getShareId();
        app_name = ((AppTrans)getApplication()).getAppName();
        super.onStart();
    }
	private void initView(){
		back_btn=(Button)findViewById(R.id.comment_return_btn);
		comment_btn=(Button)findViewById(R.id.comment_btn);
		content=(EditText)findViewById(R.id.comment);
	}

    public void comment(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {

                makecomment();
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
    public void makecomment(){
        JSONObject jsonObject;
        URL url=null;
        String appname="";
        String comment_="";
        String res="";
        System.out.println(user_id+commenter_id+app_name+comment);

        try {
            appname= URLEncoder.encode(app_name, "UTF-8");
            comment_ = URLEncoder.encode(comment,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            url= new URL(SERVER + "/make_comment.php?user_id="+user_id+"&app_name="+appname+"&comment="+comment_+"&commenter_id="+commenter_id);
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
        //if(res.equals("success"))
        //Toast.makeText(CommentActivity.this,getResources().getString(R.string.cmt), Toast.LENGTH_LONG).show;
    }

}
