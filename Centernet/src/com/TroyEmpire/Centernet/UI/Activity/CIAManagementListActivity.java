package com.TroyEmpire.Centernet.UI.Activity;

import java.util.ArrayList;
import java.util.List;

import com.TroyEmpire.Centernet.Constant.BroadCastReceiverAciton;
import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetInstallAppService;
import com.TroyEmpire.Centernet.Ghost.Service.CenternetInstallAppService;
import com.TroyEmpire.Centernet.UI.Adapter.CIAListViewAdapter;
import com.TroyEmpire.Centernet.UI.Entity.CIAListViewItem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CIAManagementListActivity extends Activity implements
		OnItemClickListener, OnClickListener {

	private static final String TAG = "com.TroyEmpire.Centernet.UI.Activity.CIAInstallListActivity";
	private ListView ciaInstalledListView;
	private Button unstallAppButton;
	// 处理本程序安装apk的Service
	private ICenternetInstallAppService CIAppService;
	private List<CenternetInstalledApp> ciaAppList;
	private List<CIAListViewItem> ciaListViewItem;
	private CIAListViewAdapter ciaListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cia_app_list);
		// 初始化本程序用到的变量 主要用于不用重复创建对象的变量
		initVariables();
		// 获得初始数据用于列表显示
		prepareInitData();
		// 获得ListView的View元素
		setView();
		// 初始化与ListView绑定的Adpater对象
		setAdapter();
		// 绑定单击ListView的item时的Listener
		setListener();
	}

	// 主要用于不用重复创建对象的变量
	private void initVariables() {
		CIAppService = new CenternetInstallAppService(this);
		ciaListViewItem = new ArrayList<CIAListViewItem>();
	}

	private void setListener() {
		Log.i(TAG, "setListener");
		ciaInstalledListView.setOnItemClickListener(this);
		unstallAppButton.setOnClickListener(this);
	}

	private void setAdapter() {
		Log.i(TAG, "setAdapter");
		ciaListViewAdapter = new CIAListViewAdapter(ciaListViewItem, this);
		ciaInstalledListView.setAdapter(ciaListViewAdapter);
	}

	private void setView() {
		Log.i(TAG, "setView");
		// 更改标题栏名称
		((TextView) findViewById(R.id.title_text))
				.setText(R.string.centernet_installed_app_management);
		ciaInstalledListView = (ListView) findViewById(R.id.cia_app_listview);
		unstallAppButton = (Button) findViewById(R.id.cia_app_button);
	}

	private void updateViewByDbDataChanged(List<CIAListViewItem> ciaListViewItem) {
		prepareInitData();
		ciaListViewAdapter.setCiaListViewItem(ciaListViewItem);
		ciaListViewAdapter.notifyDataSetChanged();
	}

	private void updateView(List<CIAListViewItem> ciaListViewItem) {
		ciaListViewAdapter.setCiaListViewItem(ciaListViewItem);
		ciaListViewAdapter.notifyDataSetChanged();
	}

	private void prepareInitData() {
		Log.i(TAG, "prepareInitData");
		ciaAppList = CIAppService.getAllAppInstalledByCenternet();
		ciaListViewItem.clear();
		for (int i = 0; i < ciaAppList.size(); i++) {
			CIAListViewItem item = new CIAListViewItem();
			item.setAppName(ciaAppList.get(i).getAppName());
			item.setAppVersion(ciaAppList.get(i).getAppVersion());
			item.setLauncherActivityName(ciaAppList.get(i)
					.getLauncherActivityName());
			item.setPackageName(ciaAppList.get(i).getPackageName());
			item.setApplcon(CIAppService.getAppDrawable(ciaAppList.get(i)
					.getPackageName(), ciaAppList.get(i).getAppName()));
			item.setSelectStateIconId(R.drawable.unchecked);
			ciaListViewItem.add(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (ciaListViewItem.get(pos).getSelectStateIconId() == R.drawable.unchecked) {
			ciaListViewItem.get(pos).setSelectStateIconId(R.drawable.checked);
		} else {
			ciaListViewItem.get(pos).setSelectStateIconId(R.drawable.unchecked);
		}
		updateView(ciaListViewItem);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cia_app_button:
			uninstallApp();
			break;
		}
	}

	private void uninstallApp() {
		boolean isSelect = false;
		// 单选一次只能卸载一个app
		for (int i = 0; i < ciaListViewItem.size(); i++) {
			if (R.drawable.unchecked != ciaListViewItem.get(i)
					.getSelectStateIconId()) {
				isSelect = true;
				Uri packageURI = Uri.parse("package:"
						+ ciaListViewItem.get(i).getPackageName());
				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
						packageURI);
				startActivity(uninstallIntent);
				break;
			}
		}
		if (isSelect == false) {
			Toast.makeText(getApplicationContext(), "没有选定任何app",
					Toast.LENGTH_SHORT).show();
		}
	}

	private BroadcastReceiver updateViewAfterRemoveAppInfoInDB = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent
					.getAction()
					.equals(BroadCastReceiverAciton.BroadCast_After_Remove_App_InfoInDB)) {
				updateViewByDbDataChanged(ciaListViewItem);
			}
			if (intent
					.getAction()
					.equals(BroadCastReceiverAciton.BroadCast_Update_CIAManagement_After_Replace)) {
				updateViewByDbDataChanged(ciaListViewItem);
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(updateViewAfterRemoveAppInfoInDB);
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastReceiverAciton.BroadCast_After_Remove_App_InfoInDB);
		filter.addAction(BroadCastReceiverAciton.BroadCast_Update_CIAManagement_After_Replace);
		this.registerReceiver(updateViewAfterRemoveAppInfoInDB, filter);
		super.onResume();
	}
}
