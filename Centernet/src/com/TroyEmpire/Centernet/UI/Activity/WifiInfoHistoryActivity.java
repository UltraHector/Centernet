package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Adapter.WifiInfoListViewAdapter;
import com.TroyEmpire.Centernet.UI.Entity.WifiInfoListViewItem;
import java.util.ArrayList;
import java.util.List;

import com.TroyEmpire.Centernet.Entity.CenternetCentralUnit;
import com.TroyEmpire.Centernet.Ghost.IService.ICCUService;
import com.TroyEmpire.Centernet.Ghost.Service.CCUService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WifiInfoHistoryActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener {

	private final static String TAG = "com.TroyEmpire.Centernet.View.Activity.WifiInfoHistoryActivity";
	// 保存在范围内，不再范围内，和用于在listview中显示的CenternetCentralUnit信息
	private List<CenternetCentralUnit> apServerInRange, apServerOutRange,
			displayList;
	// 获得CenternetCentralUnit信息的service
	private ICCUService ccuService;
	// 用于显示信息的ListView对象
	private ListView displayListView;
	// 与ListView对象绑定的Adapter对象
	private WifiInfoListViewAdapter wifiInfoListViewAdapter;
	// 保存用于在ListView中显示的内容
	private List<WifiInfoListViewItem> wifiInfoListViewItem;

	// 删除记录和选中所有记录的按钮
	private Button deleteHistoryRecord, selectAllRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		// 获得初始数据用于列表显示
		prepareIntiData();
		// 设置Activity的布局，获得ListView的View元素
		setView();
		// 初始化与ListView绑定的Adpater对象
		setAdapter();
		// 绑定单击ListView的item时的Listener
		setListener();
	}

	private void setListener() {
		Log.i(TAG, "setListener");
		if (0 != displayList.size()) {
			deleteHistoryRecord.setOnClickListener(this);
			selectAllRecord.setOnClickListener(this);
			displayListView.setOnItemClickListener(this);
		}

	}

	private void setAdapter() {
		Log.i(TAG, "setAdapter");
		if (0 != displayList.size()) {
			wifiInfoListViewItem = new ArrayList<WifiInfoListViewItem>();
			for (int i = 0; i < displayList.size(); i++) {
				WifiInfoListViewItem item = new WifiInfoListViewItem();
				item.setWifiHistoryTitle(displayList.get(i).getTitile());
				item.setSelectedImageResid(R.drawable.unchecked);
				wifiInfoListViewItem.add(item);
			}
			wifiInfoListViewAdapter = new WifiInfoListViewAdapter(this,
					wifiInfoListViewItem);
			displayListView.setAdapter(wifiInfoListViewAdapter);
		}
	}

	private void setView() {
		Log.i(TAG, "setView");
		if (0 == displayList.size()) {// 没有历史记录
			setContentView(R.layout.noneof_wifiinfohistory);
		} else {// 有历史记录
			setContentView(R.layout.have_wifiinfohistory);
			displayListView = (ListView) findViewById(R.id.wifihistory_scan_listview);
			// deleteHistoryRecord, selectAllRecord;
			deleteHistoryRecord = (Button) this
					.findViewById(R.id.wifihistory_deleteRecord_button);
			selectAllRecord = (Button) this
					.findViewById(R.id.wifihistory_selectAllRecord_button);
		}
		// 设置标题栏的名称
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.function_history);
	}

	private void prepareIntiData() {
		Log.i(TAG, "prepareIntiData");
		ccuService = new CCUService(this);
		displayList = new ArrayList<CenternetCentralUnit>();
		apServerInRange = ccuService.getHistoryDataCCUInRange();
		apServerOutRange = ccuService.getHistoryDataCCUOutRange();
		if (null != apServerInRange) {
			displayList.addAll(apServerInRange);
			Log.i(TAG, "recodr inrange numL:" + apServerInRange.size());
		}
		if (null != apServerOutRange) {
			displayList.addAll(apServerOutRange);
			Log.i(TAG, "recodr outrange numL:" + apServerOutRange.size());
		}
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, FunctionSelectActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (parent.getId() == R.id.wifihistory_scan_listview) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					CenternetWebViewActivity.class);
			intent.putExtra("url", ccuService
					.getIndexPathLocationByCCUId(displayList.get(pos)
							.getCcuId()));
			Log.i(TAG, "onItemClick");
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wifihistory_deleteRecord_button:
			// TODO Auto-generated method stub
			break;
		case R.id.wifihistory_selectAllRecord_button:
			// 更改所有的状态
			changeAllSelectStatue();
			break;
		default:
		}
	}

	private void changeAllSelectStatue() {
		// 有记录时才执行下面的操作
		if (null != wifiInfoListViewItem && 0 != wifiInfoListViewItem.size()) {
			// 如果selectAllRecord的内容为全选时，把内容改为反选，选中所有的状态
			if (selectAllRecord.getText().equals(
					this.getResources().getString(
							R.string.text_select_all_history_record))) {
				selectAllRecord
						.setText(R.string.text_select_convert_history_record);
				setAlltheSameStatue(R.drawable.checked);
			} // 如果selectAllRecord的内容为反选时，把内容改为全选，把所有的状态改为未选中
			else if (selectAllRecord.getText().equals(
					this.getResources().getString(
							R.string.text_select_convert_history_record))) {
				setAlltheSameStatue(R.drawable.unchecked);
				selectAllRecord
						.setText(R.string.text_select_all_history_record);
			}
		}
	}

	private void setAlltheSameStatue(int resid) {
		for (int i = 0; i < wifiInfoListViewItem.size(); i++) {
			wifiInfoListViewItem.get(i).setSelectedImageResid(resid);
		}
		wifiInfoListViewAdapter.setWifiInfoListViewItem(wifiInfoListViewItem);
		wifiInfoListViewAdapter.notifyDataSetChanged();
	}
}
