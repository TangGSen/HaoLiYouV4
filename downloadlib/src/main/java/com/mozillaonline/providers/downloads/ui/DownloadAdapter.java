/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mozillaonline.providers.downloads.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.DownloadSelectListener;
import com.sen.downloadlib.R;

import java.text.DateFormat;
import java.util.List;

/**
 * List adapter for Cursors returned by {@link DownloadManager}.
 */
public class DownloadAdapter extends CursorAdapter {
	private Context mContext;
	private Cursor mCursor;
	private DownloadSelectListener mDownloadSelectionListener;
	private Resources mResources;
	private DateFormat mDateFormat;
	private DateFormat mTimeFormat;

	final private int mTitleColumnId;
	final private int mStatusColumnId;
	final private int mReasonColumnId;
	final private int mTotalBytesColumnId;
	final private int mCurrentBytesColumnId;
	final private int mMediaTypeColumnId;
	final private int mDateColumnId;
	final private int mIdColumnId;

	public DownloadAdapter(Context context, Cursor cursor,
			DownloadSelectListener selectionListener) {
		super(context, cursor);
		mContext = context;
		mCursor = cursor;
		mResources = mContext.getResources();
		mDownloadSelectionListener = selectionListener;
		mDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

		mIdColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
		mTitleColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
		mStatusColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
		mReasonColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);
		mTotalBytesColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
		mCurrentBytesColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
		mMediaTypeColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIA_TYPE);
		mDateColumnId = cursor
				.getColumnIndexOrThrow(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP);
	}

	public View newView() {
		
		DownloadItem view = (DownloadItem) LayoutInflater.from(mContext).inflate(R.layout.download_list_item, null);
		view.setSelectListener(mDownloadSelectionListener);
		return view;
	}

	public void bindView(View convertView) {
		if (!(convertView instanceof DownloadItem)) {
			return;
		}

		long downloadId = mCursor.getLong(mIdColumnId);
		((DownloadItem) convertView).setDownloadId(downloadId);

		// Retrieve the icon for this download
		retrieveAndSetIcon(convertView);

		String title = mCursor.getString(mTitleColumnId);
		long totalBytes = mCursor.getLong(mTotalBytesColumnId);
		long currentBytes = mCursor.getLong(mCurrentBytesColumnId);
		int status = mCursor.getInt(mStatusColumnId);

		if (title.length() == 0) {
			title = mResources.getString(R.string.missing_title);
		}
		//sen淇敼鐨�		
		//String[] titleAndLeid = title.split("_");
		
		setTextForView(convertView, R.id.download_title, title);
		
		int progress = getProgressValue(totalBytes, currentBytes);

		boolean indeterminate = status == DownloadManager.STATUS_PENDING;
		ProgressBar progressBar = (ProgressBar) convertView
				.findViewById(R.id.download_progress);
		progressBar.setIndeterminate(indeterminate);
		if (!indeterminate) {
			progressBar.setProgress(progress);
			setTextForView(convertView, R.id.progress_text, progress + "%");
		}
		if (status==DownloadManager.STATUS_RUNNING) {
			setDownloadIcon(convertView, R.id.download_icon, R.drawable.start_download);
		}else {
			setDownloadIcon(convertView, R.id.download_icon, R.drawable.stop_download);
		}
		if (status == DownloadManager.STATUS_FAILED
				|| status == DownloadManager.STATUS_SUCCESSFUL) {
			progressBar.setVisibility(View.GONE);
		} else {
			progressBar.setVisibility(View.VISIBLE);
		}

		setTextForView(convertView, R.id.size_text, getSizeText(totalBytes));
		setTextForView(convertView, R.id.status_text,
				mResources.getString(getStatusStringId(status)));

		AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView
				.findViewById(R.id.download_checkbox);
		checkBox.setChecked(mDownloadSelectionListener
				.isDownloadSelected(downloadId));
	}

	public int getProgressValue(long totalBytes, long currentBytes) {
		if (totalBytes == -1) {
			return 0;
		}
		return (int) (currentBytes * 100 / totalBytes);
	}

	private String getSizeText(long totalBytes) {
		String sizeText = "";
		if (totalBytes >= 0) {
			sizeText = Formatter.formatFileSize(mContext, totalBytes);
		}
		return sizeText;
	}

	private int getStatusStringId(int status) {
		switch (status) {
		case DownloadManager.STATUS_FAILED:
			
			return R.string.download_error;

		case DownloadManager.STATUS_SUCCESSFUL:
			return R.string.download_success;

		case DownloadManager.STATUS_PENDING:
		case DownloadManager.STATUS_RUNNING:
			return R.string.download_running;

		case DownloadManager.STATUS_PAUSED:
			if (mCursor.getInt(mReasonColumnId) == DownloadManager.PAUSED_QUEUED_FOR_WIFI) {
				return R.string.download_queued;
			} else {
				return R.string.download_paused;
			}
		}
		throw new IllegalStateException("Unknown status: "
				+ mCursor.getInt(mStatusColumnId));
	}

	private void retrieveAndSetIcon(View convertView) {
		String mediaType = mCursor.getString(mMediaTypeColumnId);
		AppCompatImageView iconView = (AppCompatImageView) convertView
				.findViewById(R.id.download_icon);
		iconView.setVisibility(View.INVISIBLE);

		if (mediaType == null) {
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromParts("file", "", null), mediaType);
		PackageManager pm = mContext.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() == 0) {
			// no icon found for this mediatype. use "unknown" icon
			iconView.setImageResource(R.drawable.stop_download);
		} else {
			iconView.setImageResource(R.drawable.start_download);
		}
		iconView.setVisibility(View.VISIBLE);
	}

	private void setTextForView(View parent, int textViewId, String text) {
		AppCompatTextView view = (AppCompatTextView) parent.findViewById(textViewId);
		view.setText(text);
	}
	
	private void setDownloadIcon(View parent, int iconId, int resId){
		AppCompatImageView view = (AppCompatImageView) parent.findViewById(iconId);
		view.setImageResource(resId);
	}

	// CursorAdapter overrides

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return newView();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		bindView(view);
	}
}
