package com.TroyEmpire.Centernet.UI.Adapter;

import java.util.List;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Entity.WifiInfoListViewItem;
import com.TroyEmpire.Centernet.UI.ViewHolder.WifiInfoListViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class WifiInfoListViewAdapter extends BaseAdapter {

	private final static String TAG = "com.TroyEmpire.Centernet.UI.Adapter.WifiInfoListViewAdapter";

	private List<WifiInfoListViewItem> wifiInfoListViewItem;
	private Context context;
	private LayoutInflater inflater;

	public WifiInfoListViewAdapter(Context context,
			List<WifiInfoListViewItem> centernetCentralUnit) {
		this.setWifiInfoListViewItem(centernetCentralUnit);
		this.context = context;
		// this.inflater = (LayoutInflater) this.context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.inflater = LayoutInflater.from(this.context);
		Log.i(TAG, "WifiInfoListViewAdapter Constructor");
	}

	public void setWifiInfoListViewItem(
			List<WifiInfoListViewItem> wifiInfoListViewItem) {
		this.wifiInfoListViewItem = wifiInfoListViewItem;
	}

	@Override
	public int getCount() {
		return wifiInfoListViewItem.size();
	}

	@Override
	public Object getItem(int pos) {
		return wifiInfoListViewItem.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		WifiInfoListViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(
					R.layout.activity_wifiinfo_history_listview, null);
			holder = new WifiInfoListViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (WifiInfoListViewHolder) view.getTag();
		}
		WifiInfoListViewItem listViewItem = (WifiInfoListViewItem) getItem(position);
		holder.centernetScanTitle.setText(listViewItem.getWifiHistoryTitle());
		if (R.drawable.unchecked == listViewItem.getSelectedImageResid()) {
			holder.centernetScanSelectedState
					.setBackgroundResource(R.drawable.unchecked);
		} else {
			holder.centernetScanSelectedState
					.setBackgroundResource(R.drawable.checked);
		}
		holder.centernetScanSelectedState
				.setOnClickListener(new OnClickListener() {
					WifiInfoListViewItem item = wifiInfoListViewItem
							.get(position);

					@Override
					public void onClick(View v) {
						if (item.getSelectedImageResid() == R.drawable.unchecked) {
							item.setSelectedImageResid(R.drawable.checked);
						} else {
							item.setSelectedImageResid(R.drawable.unchecked);
						}
						updateView();
					}
				});
		return view;
	}

	private void updateView() {
		this.notifyDataSetChanged();
	}
}
