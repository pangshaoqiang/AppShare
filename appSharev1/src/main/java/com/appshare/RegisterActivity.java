package com.appshare;

import android.R.anim;
import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends Activity {
	private EditText account_name, pw1, pw2, phone_num;
	private TextView accout_hint, pw1_hint, pw2_hint, phone_hint;
	private Button register_ok, register_cancel;
	private String account_name_value, pw1_value, pw2_value, phone_num_value;
    private String user_id;
    private String password;
    private String phone;
    private String IsExist;
    private String SERVER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		setWindow();
		initView();
		init();
	}

	private void setWindow() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ�?��
		Point outsize = new Point();
		LayoutParams p = getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
		d.getSize(outsize);
		p.height = (int) (outsize.y * 0.6); // �߶�����Ϊ��Ļ��0.8
		p.width = (int) (outsize.x * 0.9); // �������Ϊ��Ļ��0.8
		p.alpha = 1.0f; // ���ñ���͸����
		p.dimAmount = 0.0f; // ���úڰ���

		getWindow().setAttributes(p); // ������Ч
		getWindow().setGravity(Gravity.CENTER); // ���ÿ��Ҷ���
	}

	private void initView() {
		account_name = (EditText) findViewById(R.id.account_value);
		pw1 = (EditText) findViewById(R.id.password_value1);
		pw2 = (EditText) findViewById(R.id.password_value2);
		accout_hint = (TextView) findViewById(R.id.account_hint);
		pw1_hint = (TextView) findViewById(R.id.password1_hint);
		pw2_hint = (TextView) findViewById(R.id.password2_hint);
		phone_hint = (TextView) findViewById(R.id.phone_hint);
		phone_num = (EditText) findViewById(R.id.phone_value);
		register_ok = (Button) findViewById(R.id.register_ok);
		register_cancel = (Button) findViewById(R.id.register_cancel);
		account_name.setFocusable(true);
		account_name.setFocusableInTouchMode(true);
	}

	public void init() {

		account_name.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				accout_hint.setText("");
				if (temp.length() > 0) {
					if (temp.length() < 3) {
						accout_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue1));
						accout_hint.setTextColor(getResources().getColor(
								R.color.red));
					}
					if (temp.length() > 20) {
						accout_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue2));
						accout_hint.setTextColor(getResources().getColor(
                                R.color.red));
					}
					if (temp.length() >= 3 && temp.length() <= 20) {
                        user_id = temp.toString();
                        checkRegisterName();
						/*if (checkRegisterName(temp)) {
							accout_hint.setText("�û��ʺſ���");
							accout_hint.setTextColor(getResources().getColor(
									R.color.green));
						} else {
							accout_hint.setText("���û��Ѵ���");
							accout_hint.setTextColor(getResources().getColor(
									R.color.red));
						}*/

					}
				}
			}
		});
		pw1.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
                password = temp.toString();
				pw1_hint.setText("");
				if (temp.length() > 0) {
					if (temp.length() < 3) {
						pw1_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue5));
						pw1_hint.setTextColor(getResources().getColor(
								R.color.red));
					}
					if (temp.length() > 20) {
						pw1_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue6));
						pw1_hint.setTextColor(getResources().getColor(
								R.color.red));
					}
					if (temp.length() >= 3 && temp.length() <= 20) {
						pw1_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue7));
						pw1_hint.setTextColor(getResources().getColor(
								R.color.green));
					}
				}
			}
		});
		pw2.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String str1;
				String str2;
				str1 = pw1.getText().toString();
				str2 = pw2.getText().toString();
				if (str1.equals(str2)) {
					pw2_hint.setText("");
					if (temp.length() >= 3 && temp.length() <= 20) {
						pw2_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue8));
						pw2_hint.setTextColor(getResources().getColor(
								R.color.green));
						register_ok.setEnabled(true);
					}
				} else {
					pw2_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue9));
					pw2_hint.setTextColor(getResources().getColor(R.color.red));
					register_ok.setEnabled(false);// ���벻ͬʱ��֤���ܵ��
				}
			}
		});
		register_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //������ݿ�
				/*finish();
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG)
						.show();*/
                register();
			}
		});
		register_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(anim.fade_in, anim.fade_out);
			}
		});
	}
    private void checkRegisterName() {
        SERVER = (String) this.getResources().getString(R.string.server_ip);
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                URL url = null;
                try {
                    try {
                        url = new URL(SERVER+ "/user_exist.php?uid=" + user_id);
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
                    IsExist=jsonObject.getString("user_exist");
                    //System.out.println(IsExist);
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
                if(result.equals("NO")){
                    accout_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue3));
                    accout_hint.setTextColor(getResources().getColor(
                            R.color.green));
                } else {
                    accout_hint.setText((String) RegisterActivity.this.getResources().getString(R.string.LAcue4));
                    accout_hint.setTextColor(getResources().getColor(
                            R.color.red));
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

    public void register(){
        SERVER = (String) this.getResources().getString(R.string.server_ip);
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                phone = phone_num.getText().toString();
                URL url = null;
                try {
                    try {
                        url = new URL(SERVER+ "/register.php?uid=" + user_id + "&password=" + password + "&phone=" + phone);
                        System.out.println(url.toString());
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
                    IsExist=jsonObject.getString("res");
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
                if(result.equals("success")) {
                    finish();
                    Toast.makeText(RegisterActivity.this, (String) RegisterActivity.this.getResources().getString(R.string.LAcue10), Toast.LENGTH_LONG)
                            .show();
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


}
