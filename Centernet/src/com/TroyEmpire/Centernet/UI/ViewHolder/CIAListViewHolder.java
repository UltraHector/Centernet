package com.TroyEmpire.Centernet.UI.ViewHolder;

import com.TroyEmpire.Centernet.UI.Activity.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CIAListViewHolder {
	public ImageView ciaIcon;
	public TextView ciaAppName;
	public TextView ciaVersion;
	public ImageView ciaAppSelectImage;

	public CIAListViewHolder(View view) {
		ciaIcon = (ImageView) view.findViewById(R.id.ciaImg);
		ciaAppName = (TextView) view.findViewById(R.id.ciaAppName);
		ciaVersion = (TextView) view.findViewById(R.id.ciaVersion);
		ciaAppSelectImage = (ImageView) view
				.findViewById(R.id.cia_select_state);
	}
}
