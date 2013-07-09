package com.TroyEmpire.Centernet.UI.ViewHolder;


import com.TroyEmpire.Centernet.UI.Activity.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FunctionSelectGridViewHolder {
	public ImageView funcIamge;
	public TextView funcName;

	public FunctionSelectGridViewHolder(View view) {
		funcIamge = (ImageView) view.findViewById(R.id.function_image_item);
		funcName = (TextView) view.findViewById(R.id.function_name);
	}
}
