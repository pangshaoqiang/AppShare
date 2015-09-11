package com.appshare;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

public class LoginActivity extends Activity {

	private EditText accountName, password, name, pw, phone;
	private CheckBox rem_pw, auto_login;
    private ImageView thehead;
	private Button login, register;
	private String userNameValue, passwordValue;
	private SharedPreferences sp;
    private String SERVER;
    private String IsExist;
    private String Login_Fault;
    private String head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        Login_Fault = (String) this.getResources().getString(R.string.login_fault);
        IsExist = "NO";
        initView();
		//initView();
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		if (sp.getBoolean("ISCHECK", false)) {
			// ����Ĭ���Ǽ�¼����״̬
			rem_pw.setChecked(true);
			accountName.setText(sp.getString("USER_ID", ""));
			password.setText(sp.getString("PASSWORD", ""));
			// �ж��Զ���½��ѡ��״̬
			if (sp.getBoolean("AUTO_ISCHECK", false)) {
				// ����Ĭ�����Զ���¼״̬
				auto_login.setChecked(true);
				// ��ת����
				Intent intent = new Intent(LoginActivity.this,
						LogoActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				//((ApplicationTrans) getApplication()).setValue(sp.getString("USER_NAME", ""));

			}
		}
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userNameValue = accountName.getText().toString();
				passwordValue = password.getText().toString();
                checkLogin();

			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(anim.fade_in, anim.fade_out);
			}
		});
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {
					System.out.println("��ס������ѡ��");
					sp.edit().putBoolean("ISCHECK", true).commit();
				} else {

					System.out.println("��ס����û��ѡ��");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}

			}
		});

		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (auto_login.isChecked()) {
					System.out.println("�Զ���¼��ѡ��");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("�Զ���¼û��ѡ��");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
	}

	private void initView() {
		accountName = (EditText) findViewById(R.id.account_value);
		password = (EditText) findViewById(R.id.password_value);
		rem_pw = (CheckBox) findViewById(R.id.cb_password);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
        thehead = (ImageView) findViewById(R.id.head_icon);
	}
    public void checkLogin() {
        SERVER = (String) this.getResources().getString(R.string.server_ip);
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                URL url = null;
                try {
                    try {
                        url = new URL(SERVER + "/user_exist.php?uid=" + userNameValue + "&password=" + passwordValue);
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
                    IsExist = jsonObject.getString("user_exist");
                    System.out.println(IsExist);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return IsExist;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                Intent intent = new Intent(LoginActivity.this,
                        LogoActivity.class);
                //System.out.println(result + "1");
                if (IsExist.equals("YES")) {
                    //System.out.println("XX");
                    if (rem_pw.isChecked()) {
                        // ��ס�û������롢
                        Editor editor = sp.edit();
                        editor.putString("USER_ID", userNameValue);
                        editor.putString("PASSWORD", passwordValue);
                        editor.commit();
                    }
                }
                if (IsExist.equals("YES")) {
                    //System.out.println("SS");
                    Editor editor = sp.edit();
                    editor.putString("USER_ID", userNameValue);
                    editor.commit();
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, Login_Fault, Toast.LENGTH_SHORT).show();
                }
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

    public void GetMyInfo(String userid) {
        SERVER = (String) this.getResources().getString(R.string.server_ip);
        Integer res = 0;
        Boolean exist;
        URL url = null;
        JSONObject jsonObject;
        exist = false;
        try {
            try {
                url = new URL(SERVER + "/get_user_info.php?uid=" + userid);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            InputStream iStream = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(iStream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
                exist = true;
            }
            iStream.close();
            if (exist) {
                jsonObject = new JSONObject(builder.toString());
                head = jsonObject.getString("head");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetTheHead() {
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                GetMyInfo(accountName.getText().toString());
                Bitmap headimg = null;
                Drawable drawable;
                //if(headimg == null) DownTheHead(head);
                thehead = (ImageView) findViewById(R.id.head_icon);
                headimg = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                if (headimg != null) {
                    //System.out.println(head);
                    drawable = new BitmapDrawable(null, headimg);
                    //System.out.println("A");
                    thehead.setImageDrawable(drawable);
                }
                //else thehead.setImageResource(R.drawable.default_round_head);
                //System.out.println("ASY");

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

    public void DownTheHead(String head) {
        String SERVER = (String) this.getResources().getString(R.string.server_ip);
        ;
        String headurl = SERVER + "/head/" + head;
        OutputStream output = null;
        try {
            URL url = new URL(headurl);
            URLConnection connection = url.openConnection();
            String DownPath = "/sdcard/AppShare/head/" + head;
            File file = new File(DownPath);
            InputStream input = connection.getInputStream();
            file.createNewFile();
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (input.read(buffer) != -1) {
                output.write(buffer);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
