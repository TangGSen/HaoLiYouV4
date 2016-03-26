package com.sen.haoliyou.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {

	/**
	 * 检测网络连接是否可用
	 */
	public static boolean isNetworkConnected(Activity context) {
		if (null != context) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (null != info) {
				Log.e("提示", "网络连接状态为：[" + info.isAvailable() + "]");
				return info.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检测wifi是否已经连接
	 */
	public static boolean isWifiConnected(Context context) {
		if (null != context) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (null != info) {
				return info.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类别
	 */
	public static int getConnectedType(Context context) {
		if (null != context) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (null != info && info.isAvailable()) {
				return info.getType();
			}
		}
		return -1;
	}

}
