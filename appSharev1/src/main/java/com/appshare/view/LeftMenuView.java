package com.appshare.view;

import com.appshare.LoginActivity;
import com.appshare.MyFavActivity;
import com.appshare.MyShareActivity;
import com.appshare.R;
import com.appshare.ShareActivity;
import com.appshare.base.AppTrans;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LeftMenuView {
	private final Activity activity;
	SlidingMenu localSlidingMenu;
	private ImageView head;
	private TextView nickname, sex, sign ,user_id;
	private Button fav_btn, hs_btn, offline_btn;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private String headfile;
    private String user_name;
    private String sex_;
    private String user_id_;
    private String sign_;
    private String phone;

	public LeftMenuView(Activity activity) {
		this.activity = activity;
        share = activity.getSharedPreferences("userInfo", activity.MODE_PRIVATE);
        editor=share.edit();
        headfile = share.getString("HEAD", "");
        user_name = share.getString("USER_NAME", "");
        sign_ = share.getString("SIGN", "null");
        if(share.getString("SEX", "null").equals("male"))
            sex_=activity.getResources().getString(R.string.male);
        if(share.getString("SEX", "null").equals("female"))
            sex_=activity.getResources().getString(R.string.female);
        phone = share.getString("PHONE", "");
        user_id_ = share.getString("USER_ID","");
	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);// �������һ��˵�
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ����Ҫʹ�˵�������������Ļ�ķ�Χ
		// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//������������ȡ�����˵�����Ľ��㣬������ע�͵�
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// ������ӰͼƬ�Ŀ��
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// ������ӰͼƬ
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu����ʱ��ҳ����ʾ��ʣ����
		localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu����ʱ�Ľ���̶�
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);// ʹSlidingMenu������Activity�ұ�
		// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//����SlidingMenu�˵��Ŀ��
		localSlidingMenu.setMenu(R.layout.left_drawer_fragment);// ����menu�Ĳ����ļ�
		// localSlidingMenu.toggle();//��̬�ж��Զ��رջ���SlidingMenu
		// localSlidingMenu.setSecondaryMenu(R.layout.left_drawer_activity);
		// localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		localSlidingMenu
				.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {
                        //SetTheHead();
                        headfile = share.getString("HEAD", "");
                        user_name = share.getString("USER_NAME", "");
                        sign_ = share.getString("SIGN", "null");
                        if(share.getString("SEX", "null").equals("male"))
                            sex_=activity.getResources().getString(R.string.male);
                        if(share.getString("SEX", "null").equals("female"))
                            sex_=activity.getResources().getString(R.string.female);
                        phone = share.getString("PHONE", "");
                        user_id_ = share.getString("USER_ID","");
                        Bitmap tmp = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + headfile );
                        Drawable drawable = new BitmapDrawable(null,tmp);
                        System.out.println(headfile+"1");
                        //if(drawable!=null)
                        head.setImageDrawable(drawable);
					}
				});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// TODO Auto-generated method stub

			}
		});
		initView();
		initListener(activity);

		return localSlidingMenu;
	}

	private void initView() {
		head = (ImageView) localSlidingMenu.findViewById(R.id.left_menu_head);
        user_id = (TextView) localSlidingMenu.findViewById(R.id.user_name_text);
		nickname = (TextView) localSlidingMenu
				.findViewById(R.id.nick_name_text);
		sex = (TextView) localSlidingMenu.findViewById(R.id.sex_text);
		sign = (TextView) localSlidingMenu.findViewById(R.id.sign_text);
		offline_btn = (Button) localSlidingMenu.findViewById(R.id.offline_btn);
		fav_btn = (Button) localSlidingMenu.findViewById(R.id.favorite_btn);
		hs_btn = (Button) localSlidingMenu.findViewById(R.id.history_share_btn);
        share = activity.getSharedPreferences("userInfo", activity.MODE_PRIVATE);
        editor=share.edit();
        headfile = share.getString("HEAD", "");
        user_name = share.getString("USER_NAME", "");
        sign_ = share.getString("SIGN", "null");
        if(share.getString("SEX", "null").equals("male"))
            sex_=activity.getResources().getString(R.string.male);
        if(share.getString("SEX", "null").equals("female"))
            sex_=activity.getResources().getString(R.string.female);
        phone = share.getString("PHONE", "");
        user_id_ = share.getString("USER_ID","");
        nickname.setText(user_name);
        sex.setText(sex_);
        sign.setText(sign_);
        user_id.setText(user_id_);
	}
    private void resumeView() {
        headfile = share.getString("HEAD", "");
        user_name = share.getString("USER_NAME", "");
        sign_ = share.getString("SIGN", "null");
        if(share.getString("SEX", "null").equals("male"))
            sex_=activity.getResources().getString(R.string.male);
        if(share.getString("SEX", "null").equals("female"))
            sex_=activity.getResources().getString(R.string.female);
        phone = share.getString("PHONE", "");
        user_id_ = share.getString("USER_ID","");
        nickname.setText(user_name);
        sex.setText(sex_);
        sign.setText(sign_);
        user_id.setText(user_id_);
    }

	private void initListener(final Activity activity) {

		head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		nickname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder NickName = new AlertDialog.Builder(activity);
				final EditText input = new EditText(activity);
				NickName.setTitle(activity.getResources().getString(R.string.newname))
						.setView(input)
						.setPositiveButton(activity.getResources().getString(R.string.Sure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String nickName = input.getText()
												.toString();
										nickname.setText(nickName);
                                        UpdateInfo();
										Toast.makeText(activity, activity.getResources().getString(R.string.updatesucc),
												Toast.LENGTH_LONG).show();
									}
								}).show();
			}
		});
		sex.setOnClickListener(new OnClickListener() {
			CharSequence sexValue = null;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder NickName = new AlertDialog.Builder(activity);
				RadioGroup sex = new RadioGroup(activity);
				final RadioButton male = new RadioButton(activity);
				male.setText(activity.getResources().getString(R.string.male));
				final RadioButton female = new RadioButton(activity);
				female.setText(activity.getResources().getString(R.string.female));
				sex.addView(male);
				sex.addView(female);
				sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == male.getId()) {
							// ��mRadio1�����ݴ���mTextView1
							sexValue = male.getText();
						} else if (checkedId == female.getId()) {
							// ��mRadio2�����ݴ���mTextView1
							sexValue = female.getText();
						}
					}
				});
				NickName.setTitle(activity.getResources().getString(R.string.sexset))
						.setView(sex)
						.setPositiveButton(activity.getResources().getString(R.string.Sure),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										LeftMenuView.this.sex.setText(sexValue);
                                        UpdateInfo();
										Toast.makeText(activity, activity.getResources().getString(R.string.updatesucc),
												Toast.LENGTH_LONG).show();
									}
								})
						.setNegativeButton(activity.getResources().getString(R.string.Cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();

			}
		});
		sign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder NickName = new AlertDialog.Builder(activity);
				final EditText input = new EditText(activity);
				NickName.setTitle(activity.getResources().getString(R.string.newsign))
						.setView(input)
						.setPositiveButton(activity.getResources().getString(R.string.Sure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String signValue = input.getText()
												.toString();
										sign.setText(signValue);
                                        UpdateInfo();
										Toast.makeText(activity, activity.getResources().getString(R.string.updatesucc),
												Toast.LENGTH_LONG).show();
									}
								}).show();
			}
		});
		offline_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.zoomin,
						R.anim.zoomout);
                activity.finish();
			}
		});
		fav_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, MyFavActivity.class);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.in_from_right,
						R.anim.out_from_left);
			}
		});
		hs_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, MyShareActivity.class);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.in_from_right,
						R.anim.out_from_left);
			}
		});
	}

    public void UpdateInfo(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
        String SERVER = activity.getResources().getString(R.string.server_ip);
        String new_user_name = nickname.getText().toString();
        String new_sex;
        new_sex = "male";
        if(sex.getText().toString().equals(activity.getResources().getString(R.string.male)))
            new_sex = "male";
        else if(sex.getText().toString().equals(activity.getResources().getString(R.string.female)))
            new_sex = "female";
        String new_sign = sign.getText().toString();
        editor.putString("SEX",new_sex);
        editor.putString("USER_NAME",new_user_name);
        editor.putString("SIGN",new_sign);
        editor.commit();
        try {
            new_user_name = URLEncoder.encode(new_user_name,"UTF-8");
            new_sign = URLEncoder.encode(new_sign,"UTF-8");
            URL url= new URL((String) SERVER + "/update_info.php?uid="+user_id_+"&name="+new_user_name+"&sex="+new_sex+"&sign="+new_sign);
            URLConnection connection = url.openConnection();
            long total = connection.getContentLength();
            InputStream iStream = connection.getInputStream();
            System.out.println(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }       return null;
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
}
