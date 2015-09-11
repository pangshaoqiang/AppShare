package com.appshare.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.appshare.LoginActivity;
import com.appshare.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LeftMenuFragment extends Fragment {
	private ImageView thehead;
	private TextView nickname,sex,sign;
	private Button fav_btn,hs_btn,offline_btn;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private String head;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        share = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        head = share.getString("HEAD", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.left_drawer_fragment, container, false);
		initView(rootview);
        System.out.println(head+"2");
		offline_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);

//				Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG)
//				.show();
			}
		});
		
		return rootview;
	}
	
	private void initView(View view){
        thehead = (ImageView) getActivity().findViewById(R.id.left_menu_head);
		SetTheHead();
        offline_btn=(Button)view.findViewById(R.id.offline_btn);
	}
	
	private void initListener(){
		offline_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}
    public void SetTheHead(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                Bitmap tmp = BitmapFactory.decodeFile("/sdcard/AppShare/head/" + head);
                Drawable drawable = new BitmapDrawable(null,tmp);
                System.out.println(head+"1");
                if(drawable!=null)
                thehead.setImageDrawable(drawable);
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

}
