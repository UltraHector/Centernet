package com.TroyEmpire.Centernet.UI.ViewHolder;

import com.TroyEmpire.Centernet.UI.Activity.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiInfoListViewHolder {
	public TextView centernetScanTitle;
	public ImageView centernetScanSelectedState;

	public WifiInfoListViewHolder(View view) {
		centernetScanTitle = (TextView) view
				.findViewById(R.id.wifi_history_title);
		centernetScanSelectedState = (ImageView) view
				.findViewById(R.id.wifi_history_selectedStatus);
	}
}
