package com.TroyEmpire.Centernet.UI.Adapter;

import java.util.List;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Entity.CenternetScanListViewItem;
import com.TroyEmpire.Centernet.UI.ViewHolder.CenternetScanListViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//实现绑定scanListView需要的Adapter对象
public class CenternetScanListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<CenternetScanListViewItem> centernetScanListViewItem;

	public void setCenternetScanListViewItem(
			List<CenternetScanListViewItem> centernetScanListViewItem) {
		this.centernetScanListViewItem = centernetScanListViewItem;
	}

	public CenternetScanListViewAdapter(Context context,
			List<CenternetScanListViewItem> centernetScanListViewItem) {
		this.context = context;
		this.centernetScanListViewItem = centernetScanListViewItem;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return centernetScanListViewItem.size();
	}

	@Override
	public Object getItem(int position) {
		return centernetScanListViewItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		CenternetScanListViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(R.layout.activity_centernet_scan_listview,
					null);
			holder = new CenternetScanListViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (CenternetScanListViewHolder) convertView.getTag();
		}
		CenternetScanListViewItem item = (CenternetScanListViewItem) getItem(position);
		holder.scanImage.setImageDrawable(item.getScanImage());
		holder.scanTitle.setText(item.getScanTitle());
		return view;
	}

}
