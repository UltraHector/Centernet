package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.UI.Activity.R;
import java.util.ArrayList;
import java.util.List;

import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Ghost.IService.IAccessPointService;
import com.TroyEmpire.Centernet.Ghost.Service.AccessPointService;
import com.TroyEmpire.Centernet.UI.Fragment.WifiPasswordDialogFrament;
import com.TroyEmpire.Centernet.Util.Checker;
import com.TroyEmpire.Centernet.Util.Checker.Resource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class TabHostActivity extends FragmentActivity implements
		OnClickListener {
	// 下面声明要用到的变量
	// 用于LOG
	private final String TAG = "com.TroyEmpire.Centernet.View.Activity.TabHostActivity";
	// 表示TabHostActivity的上下文
	private Context mContex = this;
	// TabHostActivity用到的选项卡对象
	private TabHost tabHost;
	// 获得数据需要的Service对象
	private IAccessPointService apService;
	// 保存扫描到的结果
	List<AccessPoint> wifiWithCode, wifiWithOutCode;
	// 保存有密码和没有密码的listview
	private ListView wifiInfoWithCode, wifiInfoWithOutCode;
	// 对应实现相应功能的button
	private Button tab1RefreshWifi, tab2RefreshWifi, tab1ScanCenternet,
			tab2ScanCenternet;
	// 与相应listview关联的Adapter对象，把数据和VIEW绑定在一起
	private WifiWithCodelistViewAdapter wifiInfoWithCodeAdpater;
	private WifiWithOutCodelistViewAdapter wifiInfoWithOutCodeAdpater;

	// 更新tab中listview的模式，共3种
	private static final int upadteModeTab1 = 1;
	private static final int upadteModeTab2 = 2;
	private static final int upadteModeTabAll = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabhost);
		// 获得初始数据用于列表显示
		prepareInitDate();
		// 获得TabHostActivity的View元素
		setView();
		// 初始化与listView绑定的Adpater对象
		setAdapter();
		// 绑定单击listView的item时的Listener
		setListener();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		// TabHostActivity每次重新获得交点时更新ListView的显示内容
		checkWifiStateForListView(upadteModeTabAll);

	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}

	private void setView() {
		Log.i(TAG, "setView()");
		// 获取TabHost对象
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		// 初始化TabHost组件
		tabHost.setup();
		LayoutInflater inflater = LayoutInflater.from(this);
		// /这里用到了inflate的第二个参数
		inflater.inflate(R.layout.activity_tabhost_tab1,
				tabHost.getTabContentView());
		inflater.inflate(R.layout.activity_tabhost_tab2,
				tabHost.getTabContentView());
		// 添加第一个标签页
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(
						getTabWidget(getResources().getString(
								R.string.tab1_name)))
				.setContent(R.id.linearlayout_tab1));
		// 添加第二个标签页
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(
						getTabWidget(getResources().getString(
								R.string.tab2_name)))
				.setContent(R.id.linearlayout_tab2));
		// 布局每个tab里面的扫描按钮
		tab1RefreshWifi = (Button) findViewById(R.id.tab1_refresh_button);
		tab2RefreshWifi = (Button) findViewById(R.id.tab2_refresh_button);
		tab1ScanCenternet = (Button) findViewById(R.id.tab1_scancenternet_button);
		tab2ScanCenternet = (Button) findViewById(R.id.tab2_scancenternet_button);
		// 获得用于在tab中显示内容的列表
		wifiInfoWithCode = (ListView) findViewById(R.id.tab1_listview);
		wifiInfoWithOutCode = (ListView) findViewById(R.id.tab2_listview);
		// 设置标题栏的名称
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.scan_wifi_info);
	}

	// 初始化数据
	private void prepareInitDate() {
		Log.i(TAG, "prepareInitDate()");
		// 初始化相应的对象
		apService = new AccessPointService(this);
		wifiWithCode = new ArrayList<AccessPoint>();
		wifiWithOutCode = new ArrayList<AccessPoint>();
	}

	private void setAdapter() {
		Log.i(TAG, "setAdapter()");
		// 初始化与listView绑定的Adpater对象
		wifiInfoWithCodeAdpater = new WifiWithCodelistViewAdapter(this);
		wifiInfoWithOutCodeAdpater = new WifiWithOutCodelistViewAdapter(this);
		// 绑定显示listView数据的Adpater
		wifiInfoWithCode.setAdapter(wifiInfoWithCodeAdpater);
		wifiInfoWithOutCode.setAdapter(wifiInfoWithOutCodeAdpater);
	}

	private void setListener() {
		Log.i(TAG, "setListener()");
		// 绑定单击listView的item时的Listener
		wifiInfoWithCode.setOnItemClickListener(listOnItemClickListener);
		wifiInfoWithOutCode.setOnItemClickListener(listOnItemClickListener);
		// 绑定相应按钮的Listener
		tab1RefreshWifi.setOnClickListener(this);
		tab2RefreshWifi.setOnClickListener(this);
		tab1ScanCenternet.setOnClickListener(this);
		tab2ScanCenternet.setOnClickListener(this);
	}

	// 绑定tab1_listview item中的View对象
	public class WifiWithCodelistViewHolder {
		public ImageView wifi_intensity;
		public Button ignore;
		public Button config;
		public TextView wifi_ssid;
		public TextView wifi_state;

		public WifiWithCodelistViewHolder(View view) {
			wifi_intensity = (ImageView) view
					.findViewById(R.id.tab1_wifi_intensity);
			wifi_ssid = (TextView) view.findViewById(R.id.tab1_wifi_ssid);
			wifi_state = (TextView) view.findViewById(R.id.tab1_wifi_state);
			ignore = (Button) view.findViewById(R.id.tab1_ignore_button);
			config = (Button) view.findViewById(R.id.tab1_config_button);
		}
	}

	// 实现绑定tab1的listview需要的Adapter对象
	protected class WifiWithCodelistViewAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public WifiWithCodelistViewAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return wifiWithCode.size();
		}

		@Override
		public Object getItem(int position) {
			return wifiWithCode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			WifiWithCodelistViewHolder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				view = inflater.inflate(
						R.layout.activity_tabhost_tab1_listview, null);
				// 初始化相应的View对象
				holder = new WifiWithCodelistViewHolder(view);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (WifiWithCodelistViewHolder) convertView.getTag();
			}
			final AccessPoint ap = (AccessPoint) wifiWithCode.get(position);

			// 设置状态图片
			holder.wifi_intensity.setBackgroundResource(getSignalIntensify(ap
					.getLevel()));
			// 显示ssid
			holder.wifi_ssid.setText(ap.getSsid());
			// 设置忽略按钮要显示的内容
			if (ap.isIgnored()) {
				holder.ignore.setText(getResources().getString(
						R.string.text_recover));
			} else {
				holder.ignore.setText(getResources().getString(
						R.string.text_ignore));
			}
			// 设置wifi状态
			if (ap.isAlreadyConfigured()) {
				holder.wifi_state.setText(getResources().getString(
						R.string.text_save));
			} else {
				holder.wifi_state.setText(getResources().getString(
						R.string.text_not_save));
			}
			// 单击相应按钮改变其要显示的内容
			holder.ignore.setOnClickListener(new OnClickListener() {
				// 充当临时变量的作用
				AccessPoint apCurrent = wifiWithCode.get(position);

				@Override
				public void onClick(View v) {
					if (apCurrent.isIgnored()) {
						// 在数据库中保存相应的AccessPoint的Ignored字段
						apService.setAccessPointScanRecover(apCurrent
								.getBssid());
					} else {
						// 在数据库中保存相应的AccessPoint的Ignored字段
						apService.setAccessPointScanIgnored(apCurrent
								.getBssid());	
					}
					// 为了同步数据库中的数据，而不去在数据库读取数据，需要更新与listview绑定在一起的list对象
					apCurrent.setIgnored(!apCurrent.isIgnored());
					// 更新显示的内容
					updateView(upadteModeTab1);
				}
			});
			// 单击显示一个对话框对相应的wifi进行配置
			holder.config.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					WifiPasswordDialogFrament wifipwdDialog = new WifiPasswordDialogFrament(
							TabHostActivity.this, ap);
					wifipwdDialog.show(getSupportFragmentManager(),
							"config_wifiwithcode");
					// TODO do something when press the cofiguration button
				}
			});
			return view;
		}
	}

	// 绑定tab2_listview item中的View对象
	public class WifiWithOutCodelistViewHolder {
		public ImageView wifi_intensity;
		public Button ignore;
		public TextView wifi_ssid;

		public WifiWithOutCodelistViewHolder(View view) {
			// 初始化相应的View对象
			wifi_intensity = (ImageView) view
					.findViewById(R.id.tab2_wifi_intensity);
			wifi_ssid = (TextView) view.findViewById(R.id.tab2_wifi_ssid);
			ignore = (Button) view.findViewById(R.id.tab2_ignore_button);
		}

	}

	// 实现绑定tab2的listview需要的Adapter对象
	protected class WifiWithOutCodelistViewAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public WifiWithOutCodelistViewAdapter(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return wifiWithOutCode.size();
		}

		@Override
		public Object getItem(int position) {
			return wifiWithOutCode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			WifiWithOutCodelistViewHolder holder = null;
			View view = null;
			if (convertView == null || convertView.getTag() == null) {
				view = inflater.inflate(
						R.layout.activity_tabhost_tab2_listview, null);
				holder = new WifiWithOutCodelistViewHolder(view);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (WifiWithOutCodelistViewHolder) convertView.getTag();
			}
			AccessPoint ap = wifiWithOutCode.get(position);
			// 设置状态图片
			holder.wifi_intensity.setBackgroundResource(getSignalIntensify(ap
					.getLevel()));
			// 显示ssid
			holder.wifi_ssid.setText(ap.getSsid());
			// 设置忽略按钮要显示的内容
			if (ap.isIgnored()) {
				holder.ignore.setText(getResources().getString(
						R.string.text_recover));
			} else {
				holder.ignore.setText(getResources().getString(
						R.string.text_ignore));
			}
			// 单击相应按钮改变其要显示的内容
			holder.ignore.setOnClickListener(new OnClickListener() {
				AccessPoint apCurrent = wifiWithOutCode.get(position);

				@Override
				public void onClick(View v) {
					if (apCurrent.isIgnored()) {
						// 在数据库中保存相应的AccessPoint的Ignored字段
						apService.setAccessPointScanRecover(apCurrent
								.getBssid());
					} else {
						// 在数据库中保存相应的AccessPoint的Ignored字段
						apService.setAccessPointScanIgnored(apCurrent
								.getBssid());
					}
					// 为了同步数据库中的数据，而不去在数据库读取数据，需要更新与listview绑定在一起的list对象
					apCurrent.setIgnored(!apCurrent.isIgnored());
					// 更新显示的内容
					updateView(upadteModeTab2);
				}
			});
			return view;
		}
	}

	private OnItemClickListener listOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			switch (parent.getId()) {
			case R.id.tab1_listview:
				// TODO
				break;
			case R.id.tab2_listview:
				// TODO
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab1_refresh_button:
			// 获得要在列表中显示的数据
			checkWifiStateForListView(upadteModeTab1);
			break;
		case R.id.tab2_refresh_button:
			// 获得要在列表中显示的数据
			checkWifiStateForListView(upadteModeTab2);
			break;
		// 跳转到CenternetScanActivity
		case R.id.tab1_scancenternet_button:
			startActivity(new Intent(getApplicationContext(),
					CenternetScanActivity.class));
			break;
		// 跳转到CenternetScanActivity
		case R.id.tab2_scancenternet_button:
			startActivity(new Intent(getApplicationContext(),
					CenternetScanActivity.class));
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private View getTabWidget(String tabName) {
		// 这里设置tab标题栏的样式
		RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(
				mContex).inflate(R.layout.activity_tabwidgetitem, null);
		TextView tx = (TextView) linearLayout
				.findViewById(R.id.tab_widget_item);
		tx.setText(tabName);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		tx.setWidth(width / 2);
		// 设置默认的文字大小defaultSize，然后获取屏幕密度，根据密度改变文字大小
		int defaultSize = 10;
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		int testsize = (int) (defaultSize * dm.density);
		tx.setTextSize(testsize);
		tx.setGravity(Gravity.CENTER_HORIZONTAL);
		return linearLayout;
	}

	private int getSignalIntensify(int level) {
		int imageid = 4;
		switch (level) {
		case 0:
			imageid = R.drawable.wifi_intensity_1;
			break;
		case 1:
			imageid = R.drawable.wifi_intensity_2;
			break;
		case 2:
			imageid = R.drawable.wifi_intensity_3;
			break;
		case 3:
			imageid = R.drawable.wifi_intensity_4;
			break;
		case 4:
			imageid = R.drawable.wifi_intensity_5;
			break;
		}
		return imageid;
	}

	private void updateView(int updateMode) {
		switch (updateMode) {
		case upadteModeTab1:
			wifiInfoWithCodeAdpater.notifyDataSetChanged();
			break;
		case upadteModeTab2:
			wifiInfoWithOutCodeAdpater.notifyDataSetChanged();
			break;
		case upadteModeTabAll:
			wifiInfoWithCodeAdpater.notifyDataSetChanged();
			wifiInfoWithOutCodeAdpater.notifyDataSetChanged();
			break;
		}
	}

	private void checkWifiStateForListView(int scan_mode) {
		// 获得要在列表中显示的数据
		// 而且每次获得显示数据时都要检查设备WIFI是否打开
		switch (scan_mode) {
		case upadteModeTab1:
			new Checker(this).pass(new Checker.Pass() {
				@Override
				public void pass() {
					wifiWithCode = apService.getOrderedEncryptedWifiList();
					updateView(upadteModeTab1);
				}
			}).check(Resource.NETWORK);
			break;
		case upadteModeTab2:
			new Checker(this).pass(new Checker.Pass() {
				@Override
				public void pass() {
					wifiWithOutCode = apService.getOrderedOpenWifiList();
					updateView(upadteModeTab2);

				}
			}).check(Resource.NETWORK);
			break;
		case upadteModeTabAll:
			new Checker(this).pass(new Checker.Pass() {
				@Override
				public void pass() {
					wifiWithOutCode = apService.getOrderedOpenWifiList();
					wifiWithCode = apService.getOrderedEncryptedWifiList();
					updateView(upadteModeTabAll);
				}
			}).check(Resource.NETWORK);
			break;
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
}
