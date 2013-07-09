package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.Constant.BroadCastReceiverAciton;
import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetInstallAppService;
import com.TroyEmpire.Centernet.Ghost.Service.CenternetInstallAppService;
import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Adapter.FunctionSelectGridViewAdapter;
import com.TroyEmpire.Centernet.UI.Entity.FunctionSelectGridViewItem;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class FunctionSelectActivity extends FragmentActivity implements
		OnItemClickListener {

	private String TAG = "com.TroyEmpire.Centernet.View.Activity.FunctionSelectActivity";
	// 保存主要功能的名称
	private List<String> mainFuncName;
	// 保存主要功能的Image
	private List<Drawable> mainFuncLogo;
	// GridView Adapter
	private FunctionSelectGridViewAdapter girdViewAdapter;
	// GridView 上要显示的内容
	private List<FunctionSelectGridViewItem> gridViewItem;
	private List<FunctionSelectGridViewItem> gridViewStaticItem;
	private List<FunctionSelectGridViewItem> gridViewDynamicItem;
	// 用于展示内容的View对象
	private GridView gridView;
	// 处理本程序安装apk的Service
	private ICenternetInstallAppService CIAppService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectfun);
		// 获得初始数据用于列表显示
		prepareInitData();
		// 获得GridView的View元素
		setView();
		// 初始化与GridView绑定的Adpater对象
		setAdapter();
		// 绑定单击GridView的item时的Listener
		setListener();

	}

	private void setListener() {
		Log.i(TAG, "setListener()");
		gridView.setOnItemClickListener(this);
	}

	private void setAdapter() {
		Log.i(TAG, "setAdapter()");
		girdViewAdapter = new FunctionSelectGridViewAdapter(this, gridViewItem);
		gridView.setAdapter(girdViewAdapter);
	}

	private void setView() {
		Log.i(TAG, "setView()");
		// 获取GridView对象
		gridView = (GridView) findViewById(R.id.main_func_gridview);
		// 获取TextView对象 设置标题的内容
		TextView titleTextView = (TextView) findViewById(R.id.main_title_text);
		titleTextView.setText(R.string.all_function);
	}

	private void prepareInitData() {
		Log.i(TAG, "prepareInitData()");
		CIAppService = new CenternetInstallAppService(this);
		mainFuncName = new ArrayList<String>();
		mainFuncLogo = new ArrayList<Drawable>();
		gridViewItem = new ArrayList<FunctionSelectGridViewItem>();
		gridViewStaticItem = new ArrayList<FunctionSelectGridViewItem>();
		gridViewDynamicItem = new ArrayList<FunctionSelectGridViewItem>();
		gridViewItem.clear();
		gridViewStaticItem.clear();
		gridViewDynamicItem.clear();
		// 下面加载程序固定的功能
		prepareStaticData();
		// 下面加载由本程序安装的apk的索引
		prepareDynamicData();
		gridViewItem.addAll(gridViewStaticItem);
		gridViewItem.addAll(gridViewDynamicItem);
	}

	private void prepareStaticData() {
		Log.i(TAG, "prepareStaticData");
		// 下面加载程序固定的功能
		FunctionSelectGridViewItem GVItemInfo;
		// 准备资源的
		Resources resource = this.getResources();
		// 准备要固定要显示的funcName
		mainFuncName.add(resource.getString(R.string.function_serach));
		mainFuncName.add(resource.getString(R.string.function_about));
		mainFuncName.add(resource.getString(R.string.function_history));
		mainFuncName.add(resource.getString(R.string.function_app_management));
		// mainFuncName.add(resource.getString(R.string.function_gps_location));
		// 准备要固定要显示的funcIcon
		mainFuncLogo.add(resource.getDrawable(R.drawable.function_search));
		mainFuncLogo.add(resource.getDrawable(R.drawable.function_about));
		mainFuncLogo.add(resource.getDrawable(R.drawable.function_history));
		mainFuncLogo.add(resource.getDrawable(R.drawable.function_app_manage));
		// mainFuncLogo.add(resource.getDrawable(R.drawable.function_gps_location));
		for (int i = 0; i < mainFuncName.size(); i++) {
			GVItemInfo = new FunctionSelectGridViewItem();
			GVItemInfo.setFuncIcon(mainFuncLogo.get(i));
			GVItemInfo.setFuncName(mainFuncName.get(i));
			gridViewStaticItem.add(GVItemInfo);
		}
	}

	private void prepareDynamicData() {
		Log.i(TAG, "prepareDynamicData");
		gridViewDynamicItem.clear();
		FunctionSelectGridViewItem GVItemInfo;
		// 下面加载由本程序安装的apk的索引
		List<CenternetInstalledApp> appInstalledByCenternet = CIAppService
				.getAllAppInstalledByCenternet();
		Log.i(TAG, " prepareDynamicData appInstalledByCenternet.size()"
				+ appInstalledByCenternet.size());
		for (int i = 0; i < appInstalledByCenternet.size(); i++) {
			GVItemInfo = new FunctionSelectGridViewItem();
			GVItemInfo.setFuncName(appInstalledByCenternet.get(i)
					.getLauncherActivityName());
			Drawable appIcon = CIAppService.getAppDrawable(
					appInstalledByCenternet.get(i).getPackageName(),
					appInstalledByCenternet.get(i).getAppName());
			// 可能由于手动卸载了相应的程序，需要把数据库中相应的appInfo删除
			if (null == appIcon) {
				// 由于这里的操作可能会影响数据库中存放的总记录数
				// 所以下面获得numsOfAdditionalFunc 时要重新访问数据库
				// 不能直接使用appInstalledByCenternet.size()
				Log.i(TAG, "appIcon is null");
				CIAppService.deleteAppInfoByAppName(appInstalledByCenternet
						.get(i).getAppName());
				Intent intent = new Intent();
				intent.setAction(BroadCastReceiverAciton.BroadCast_After_Remove_App_InfoInDB);
				this.sendBroadcast(intent);
			} else {
				Log.i(TAG, "appIcon is not null");
				Log.i(TAG, "Appname======="
						+ appInstalledByCenternet.get(i)
								.getLauncherActivityName());
				Log.i(TAG, "version======="
						+ appInstalledByCenternet.get(i).getAppVersion());
				GVItemInfo.setFuncIcon(appIcon);
				// 这里可以直接存储包名 等到跳转时在new intent
				GVItemInfo.setPackageName(appInstalledByCenternet.get(i)
						.getPackageName());
				gridViewDynamicItem.add(GVItemInfo);
			}
		}
		Log.i(TAG, "gridViewItemLength==" + gridViewItem.size());
	}

	// 动态更新界面，不重新加载固定要显示的内容，提高程序的运行效率
	private void updateDynamicDate() {
		Log.i(TAG, "updateDynamicDate");
		prepareDynamicData();
		gridViewItem.clear();
		gridViewItem.addAll(gridViewStaticItem);
		gridViewItem.addAll(gridViewDynamicItem);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		// 注册广播 注意在填加了新的action之后 registerReceiver时对应 filter的action也要加上
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastReceiverAciton.BroadCast_Update_FunctionSelect);
		filter.addAction(BroadCastReceiverAciton.BroadCast_Remove_App);
		filter.addAction(BroadCastReceiverAciton.BroadCast_Replace_App);
		this.registerReceiver(FunctionSelectBroadcastReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
		// 注册广播，用于更新界面
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		// 注销广播 只要要放在onDestroy方法中
		this.unregisterReceiver(FunctionSelectBroadcastReceiver);
	}

	// 注册gridView的setOnItemClickListener监听器实现onItemClick事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			startActivity(new Intent(getApplication(), TabHostActivity.class));
			break;
		case 1:
			startActivity(new Intent(getApplication(), AboutActivity.class));
			break;
		case 2:
			startActivity(new Intent(getApplication(),
					WifiInfoHistoryActivity.class));
			break;
		case 3:
			startActivity(new Intent(getApplication(),
					CIAManagementListActivity.class));
			break;
//		case 4:
//			startActivity(new Intent(getApplication(),
//					GPSLocationActivity.class));
//			break;
		default:
			// 单击打开由本程序安装的APK
			PackageManager pm = this.getPackageManager();
			startActivity(pm.getLaunchIntentForPackage(gridViewItem.get(
					position).getPackageName()));

		}
	}

	private void updateView() {
		Log.i(TAG, "updateView");
		// 更新数据，因为数据库更新
		updateDynamicDate();
		// 重要 如果不设置新的gridViewItem 调用notifyDataSetChanged不能达到更新界面的效果
		girdViewAdapter.setGridViewItem(gridViewItem);
		girdViewAdapter.notifyDataSetChanged();
	}

	private BroadcastReceiver FunctionSelectBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					BroadCastReceiverAciton.BroadCast_Update_FunctionSelect)) {
				Log.i(TAG, "update view because of install new app");
				updateView();
			}
			if (intent.getAction().equals(
					BroadCastReceiverAciton.BroadCast_Remove_App)) {
				if (null != CIAppService.getCIAInDbByPackageName(intent
						.getStringExtra("packageName"))) {
					Log.i(TAG, "update view because of remove app");
					updateView();
				}
			}
			if (intent.getAction().equals(
					BroadCastReceiverAciton.BroadCast_Replace_App)) {
				String packageName = intent.getStringExtra("packageName");
				Log.i(TAG, "replace package name " + packageName);
				CenternetInstalledApp cia = CIAppService
						.getAppInfoInSystemByPackageName(packageName);
				// 如果replace的app是由本程序安装的更新版本
				// 只需要更新DB中的version字段
				if (cia != null) {
					String newAppVersion = cia.getAppVersion();
					String oldAppVersion = CIAppService
							.getAppVersionInDBByPackageName(packageName);
					// oldAppVersion为空代表DB中没有对应packageName的app
					// 因此不用更新数据库中的版本信息
					// 如果replace对应的app在本地数据库没有存档的话，把其存进本地数据库
					// 但是由于android系统在replace app时会首先调用 remove 和add
					// 而本程序的已经在这两个action中把新加入的app信息保存到现有数据库中，所以在这里就不用额外的操作的
					if (!"".equals(oldAppVersion)
							&& !oldAppVersion.equals(newAppVersion)) {
						Log.i(TAG, "update the version of app in the db");
						CIAppService.updateAppVersionInDbByPackageName(
								packageName, newAppVersion);
						// 同步app列表中的数据
						context.sendBroadcast(new Intent(
								BroadCastReceiverAciton.BroadCast_Update_CIAManagement_After_Replace));
						Toast.makeText(
								getApplication(),
								cia.getLauncherActivityName() + " 更新后的版本为 "
										+ newAppVersion, Toast.LENGTH_LONG)
								.show();
					} else if (!"".equals(oldAppVersion)
							&& oldAppVersion.equals(newAppVersion)) {
						Toast.makeText(
								getApplication(),
								cia.getLauncherActivityName() + " 更新后的版本为 "
										+ newAppVersion + " 和原来的版本相同",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	};
}
