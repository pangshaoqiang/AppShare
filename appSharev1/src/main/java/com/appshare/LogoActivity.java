package com.appshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class LogoActivity extends Activity {
	private ProgressBar progressBar;
	private Button backButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logo);

		progressBar = (ProgressBar) findViewById(R.id.pgBar);
		backButton = (Button) findViewById(R.id.btn_back);

		progressBar.setMax(100);
		progressBar.setProgress(0);	
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Handler handler=new Handler();
		Runnable run=new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(LogoActivity.this,
						MainActivity.class);
				LogoActivity.this.startActivity(intent);
                finish();
			}

		};
		handler.postDelayed(run, 5000);
        //finish();

	}
}
