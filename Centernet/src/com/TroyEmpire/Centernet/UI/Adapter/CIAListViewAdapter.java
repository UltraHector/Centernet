package com.TroyEmpire.Centernet.UI.Adapter;

import java.util.List;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Entity.CIAListViewItem;
import com.TroyEmpire.Centernet.UI.ViewHolder.CIAListViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CIAListViewAdapter extends BaseAdapter {

	private List<CIAListViewItem> ciaListViewItem;
	private Context context;
	private LayoutInflater inflater;

	public void setCiaListViewItem(List<CIAListViewItem> ciaListViewItem) {
		this.ciaListViewItem = ciaListViewItem;
	}

	public CIAListViewAdapter(List<CIAListViewItem> ciaListViewItem,
			Context context) {
		this.ciaListViewItem = ciaListViewItem;
		this.context = context;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ciaListViewItem.size();
	}

	@Override
	public Object getItem(int position) {
		return ciaListViewItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		CIAListViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(R.layout.activity_cia_app_list_item, null);
			holder = new CIAListViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (CIAListViewHolder) convertView.getTag();
		}
		CIAListViewItem app = (CIAListViewItem) getItem(position);
		holder.ciaIcon.setImageDrawable(app.getApplcon());
		holder.ciaAppName.setText(app.getLauncherActivityName());
		holder.ciaVersion.setText(app.getAppVersion());
		holder.ciaAppSelectImage.setImageResource(app.getSelectStateIconId());
		// 对应getView 的convertView参数
		return view;
	}
}
