package com.sen.haoliyou.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.sen.haoliyou.R;


@SuppressLint("SetJavaScriptEnabled")
public class NewVersionActivity extends Activity {

	private WebView webView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_browser);

		Intent i = getIntent();
		String url = i.getStringExtra("newversion");
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);

	}

	// // 按下返回键,如果有历史记录,则退回上一级历史记录
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		}else {
			finish();
			overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
		}
	}

//	private void showDialogs() {
//		final CustomerDialog dialog = new CustomerDialog(NewVersionActivity.this, 250, 150, R.layout.customer_tip_update_dialog, R.style.Theme_dialog);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
//		AppCompatTextView txt_update_title = (AppCompatTextView) dialog.findViewById(R.id.txt_update_title);
//		AppCompatTextView txt_update_message = (AppCompatTextView) dialog.findViewById(R.id.txt_update_message);
//		txt_update_title.setText("退出提示");
//		txt_update_message.setText("确认退出");
//		AppCompatButton update = (AppCompatButton) dialog.findViewById(R.id.update);
//		update.setText("确定");
//		AppCompatButton dismiss_dialog = (AppCompatButton) dialog.findViewById(R.id.update_dismiss_dialog);
//		dismiss_dialog.setVisibility(View.GONE);
//		update.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				dialog.dismiss();
//				finish();
//				overridePendingTransition(android.R.anim.slide_in_left,
//						android.R.anim.slide_out_right);
//			}
//		});




}
