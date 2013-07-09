package com.TroyEmpire.Centernet.UI.ViewHolder;

import com.TroyEmpire.Centernet.UI.Activity.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//绑定scanListView_item中的View对象
public class CenternetScanListViewHolder {
	public ImageView scanImage;
	public TextView scanTitle;

	public CenternetScanListViewHolder(View view) {
		scanImage = (ImageView) view.findViewById(R.id.centernet_scan_image);
		scanTitle = (TextView) view.findViewById(R.id.centernet_scan_title);
	}
}
