package com.TroyEmpire.Centernet.UI.Entity;

import android.graphics.drawable.Drawable;

public class CenternetScanListViewItem {
	private String scanTitle;
	private Drawable scanImage;

	public CenternetScanListViewItem() {
	}

	public String getScanTitle() {
		return scanTitle;
	}

	public void setScanTitle(String scanTitle) {
		this.scanTitle = scanTitle;
	}

	public Drawable getScanImage() {
		return scanImage;
	}

	public void setScanImage(Drawable scanImage) {
		this.scanImage = scanImage;
	}
}
