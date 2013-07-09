package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.UI.Activity.R;
import com.TroyEmpire.Centernet.UI.Adapter.CenternetScanListViewAdapter;
import com.TroyEmpire.Centernet.UI.Entity.CenternetScanListViewItem;

import java.util.ArrayList;
import java.util.List;
import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacket;
import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacketHead;
import com.TroyEmpire.Centernet.Ghost.IService.ICCUService;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetScanService;
import com.TroyEmpire.Centernet.Ghost.Service.CCUService;
import com.TroyEmpire.Centernet.Ghost.Service.CenternetScanService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CenternetScanActivity extends Activity implements
		OnItemClickListener {
	private String TAG = "com.TroyEmpire.Centernet.View.Activity.CenternetScanActivity";

	// text view private TextView newPacketView;
	private ListView scanListView;

	//
	private ProgressBar scanProgressBar;

	// define the service for this activity
	private ICCUService ccuService;
	private ICenternetScanService centernetScanService = null;

	// define adapter
	private CenternetScanListViewAdapter scanListViewAdapter;

	// define the list carry date send from server
	private List<TCPProbeResponsePacketHead> udpProbeResponsePacketHead;

	// 保存要在listview中显示的内容
	private List<CenternetScanListViewItem> centernetScanListViewItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centernet_scan);
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
		// 绑定服务
		centernetScanActivityBindService();

	}

	private void centernetScanActivityBindService() {
		// 注册intent，用于启动相应的服务
		Intent bindIntent = new Intent(CenternetScanActivity.this,
				CenternetScanService.class);
		// 绑定服务
		bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void setListener() {
		// 绑定单击ListView对象的Item时使用的Listener
		scanListView.setOnItemClickListener(this);
	}

	private void setAdapter() {
		// 设置ListView对象的adapter对象
		scanListViewAdapter = new CenternetScanListViewAdapter(this,
				centernetScanListViewItem);
		// 绑定ListView对象的adapter对象
		scanListView.setAdapter(scanListViewAdapter);
	}

	private void setView() {
		// 获得显示数据的ListView对象
		scanListView = (ListView) findViewById(R.id.centernet_scan_listview);
		// 注册上下文菜单
		registerForContextMenu(scanListView);
		// 更改标题栏名称
		((TextView) findViewById(R.id.title_text))
				.setText(R.string.centernet_webview);

		scanProgressBar = (ProgressBar) findViewById(R.id.centernet_scan_progressbar);
	}

	private void prepareInitData() {
		centernetScanListViewItem = new ArrayList<CenternetScanListViewItem>();
	}

	private void initVariables() {
		ccuService = new CCUService(this);
		// 初始化数据
		udpProbeResponsePacketHead = new ArrayList<TCPProbeResponsePacketHead>();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Activity退出，解除服务绑定
		unbindService(serviceConnection);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Activity失去焦点时，注销广播
		unregisterReceiver(newPacketActionReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Activity获得焦点时，注册接受广播
		IntentFilter centernetScanActionFilter = new IntentFilter(
				ConfigConstant.BROADCAST_NEW_PROBE_RESPONE_ACTION);
		registerReceiver(newPacketActionReceiver, centernetScanActionFilter);
	}

	// 选中上下文菜单选项时触发的事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	// 长按ListView时，构造上下文菜单显示的内容
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("123");
		menu.add(0, 0, 0, "1234");
		menu.add(0, 1, 0, "1234");
	}

	// 定义广播接受处理事件
	private BroadcastReceiver newPacketActionReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			TCPProbeResponsePacketHead packet = (TCPProbeResponsePacketHead) intent
					.getSerializableExtra(ConfigConstant.BROADCAST_NEW_PROBE_RESPONE_VALUE);
			if (packet != null) {
				udpProbeResponsePacketHead.add(packet);			
				updateListView();
			}
		}
	};

	// 定义serviceConnection绑定相应的Service对象，用与返回相应的Service对象的实例，通过实例调用相应的方法
	private ServiceConnection serviceConnection = new ServiceConnection() {
		// 与服务连接时
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			centernetScanService = ((CenternetScanService.CenternetScanBinder) service)
					.getService();
			if (centernetScanService != null)
				centernetScanService.startCenternetScan(scanProgressBar);
		}

		// 与服务断开时
		@Override
		public void onServiceDisconnected(ComponentName className) {
			centernetScanService = null;
		}
	};

	// 当接收到广播发回来的信息时，更新scanListView的内容
	private void updateListView() {
		centernetScanListViewItem.clear();
		for (int i = 0; i < udpProbeResponsePacketHead.size(); i++) {
			CenternetScanListViewItem item = new CenternetScanListViewItem();
			item.setScanImage(this.getResources().getDrawable(
					R.drawable.ic_launcher));
			item.setScanTitle(udpProbeResponsePacketHead.get(i).getTitile());
			centernetScanListViewItem.add(item);
		}
		scanListViewAdapter
				.setCenternetScanListViewItem(centernetScanListViewItem);
		scanListViewAdapter.notifyDataSetChanged();
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, FunctionSelectActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		finish();
	}

	// 单击scanListView_item时激发的方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (parent.getId() == R.id.centernet_scan_listview) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					CenternetWebViewActivity.class);
			intent.putExtra("url", ccuService
					.getIndexPathLocationByCCUId(udpProbeResponsePacketHead
							.get(pos).getCcuId()));
			startActivity(intent);
		}
	}
}
