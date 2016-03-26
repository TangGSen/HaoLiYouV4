package com.sen.haoliyou.tools;

import android.content.Context;
import android.widget.TextView;

import com.sen.haoliyou.R;
import com.sen.haoliyou.widget.CustomerDialog;


public class DialogUtils {
	private static CustomerDialog progressdialog;

	public static void showDialog(Context context, String text) {
		try {

			progressdialog = new CustomerDialog(context,130,110, R.layout.widget_dialog_cunstomer, R.style.Theme_dialog);
			progressdialog.setCanceledOnTouchOutside(false);
			TextView mMessage = (TextView) progressdialog.findViewById(R.id.message);
			mMessage.setText(text);
			progressdialog.show();

		} catch (Exception e) {
		}
	}

	public static void closeDialog() {
		try {
			if (progressdialog != null && progressdialog.isShowing()) {
				progressdialog.dismiss();
			}

		} catch (Exception e) {
		}
	}


	public static boolean isShowDiaog(){
		if (progressdialog != null && progressdialog.isShowing()) {
			return true;
		}
		return false;
	}

}
