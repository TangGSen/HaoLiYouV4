package com.mozillaonline.providers.downloads;

public interface DownloadSelectListener {
	 public void onDownloadSelectionChanged(long downloadId, boolean isSelected);
     public boolean isDownloadSelected(long id);
}
