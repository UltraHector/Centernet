package com.TroyEmpire.Centernet.UI.Adapter;

import java.util.List;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Entity.FunctionSelectGridViewItem;
import com.TroyEmpire.Centernet.UI.ViewHolder.FunctionSelectGridViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FunctionSelectGridViewAdapter extends BaseAdapter {

	private List<FunctionSelectGridViewItem> gridViewItem;
	private Context context;
	private LayoutInflater inflater;

	public FunctionSelectGridViewAdapter(Context context,
			List<FunctionSelectGridViewItem> gridViewItem) {
		this.context = context;
		this.gridViewItem = gridViewItem;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setGridViewItem(List<FunctionSelectGridViewItem> gridViewItem) {
		this.gridViewItem = gridViewItem;
	}

	@Override
	public int getCount() {
		return gridViewItem.size();
	}

	@Override
	public Object getItem(int pos) {
		return gridViewItem.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		FunctionSelectGridViewHolder holder = null;
		if (null == convertView || null == convertView.getTag()) {
			view = inflater.inflate(R.layout.activity_mainfunction_gridview,
					null);
			holder = new FunctionSelectGridViewHolder(view);
			view.setTag(holder);

		} else {
			view = convertView;
			holder = (FunctionSelectGridViewHolder) view.getTag();
		}
		FunctionSelectGridViewItem info = (FunctionSelectGridViewItem) getItem(position);
		holder.funcIamge.setImageDrawable(info.getFuncIcon());
		holder.funcName.setText(info.getFuncName());
		return view;
	}
}
