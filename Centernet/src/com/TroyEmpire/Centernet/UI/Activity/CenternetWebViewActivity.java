package com.TroyEmpire.Centernet.UI.Activity;

import java.io.File;

import com.TroyEmpire.Centernet.Constant.BroadCastReceiverAciton;
import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetInstallAppService;
import com.TroyEmpire.Centernet.Ghost.Service.CenternetInstallAppService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class CenternetWebViewActivity extends Activity {
	private String TAG = "com.TroyEmpire.Centernet.View.Activity.CenternetWebViewActivity";
	// 用于显示html文件的WebView对象
	private WebView webview;
	// 对应文件的目录，可能包含文件的安装包
	private String portalPacketSrc;
	// apk安装包文件名
	private String apkFileName;
	// 处理本程序安装apk的Service
	private ICenternetInstallAppService CIAppService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		// 初始化Service
		CIAppService = new CenternetInstallAppService(this);
		setContentView(R.layout.activity_centernet_webview);
		webview = (WebView) findViewById(R.id.centernet_webview);
		((TextView) findViewById(R.id.title_text))
				.setText(R.string.centernet_webview);
		// 如果不使用下面的语句，将使用内置浏览器访问网页
		webview.setWebViewClient(new WebViewClient());
		portalPacketSrc = getIntent().getStringExtra("url");
		webview.addJavascriptInterface(new JSAboutApkHandler(this), "JSI");
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}
		});
		// 使webview组件具有放大和缩小网页的功能
		// webview.getSettings().setSupportZoom(true);
		// webview.getSettings().setBuiltInZoomControls(true);
		webview.loadUrl("file:///" + portalPacketSrc + "/index.html");
		webview.getSettings().setJavaScriptEnabled(true);
		WebSettings settings = webview.getSettings();
		settings.setPluginState(PluginState.ON);
		//settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	public class JSAboutApkHandler {
		Context context;

		public JSAboutApkHandler(Context context) {
			this.context = context;
		}

		@JavascriptInterface
		public void switchToAnotherActivity(String s) {
			// 如果安装包存在，获得文件名
			if (portalPacketSrc != null) {
				apkFileName = findApkFileName(portalPacketSrc);
			}
			Log.i(TAG, "onClick() apkFileName=" + apkFileName);
			Log.i(TAG, "onClick() portalPacketSrc=" + portalPacketSrc);
			if (apkFileName != null && apkFileName != "") {
				// 安装文件
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				Uri uriDate = Uri.fromFile(new File(portalPacketSrc + "/"
						+ apkFileName + ".apk"));
				installIntent.setDataAndType(uriDate,
						"application/vnd.android.package-archive");
				startActivity(installIntent);
			} else {
				Toast.makeText(getApplicationContext(), "file not exist",
						Toast.LENGTH_SHORT).show();
			}
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

	// 获得安装包文件名
	private String findApkFileName(String path) {
		File f = new File(path);
		String[] filelist = f.list();
		for (String filename : filelist) {
			if (filename.contains(".apk")) {
				return filename.substring(0, filename.indexOf('.'));
			}
		}
		return "";
	}

	private void updateFunctionSelectActivityView(String packageName) {
		// 获得对应安装文件信息的Entity
		CenternetInstalledApp installedApp = CIAppService
				.getCIAInSystemBypackageName(packageName);
		// 将上面的Entity保存到DB
		// 确保DB中只有一个确定名称app信息
		if (null == installedApp) {
			Log.i(TAG, "appinfo is null");
		}
		if (null != installedApp
				&& false == CIAppService.isAppExistInDbByName(installedApp
						.getAppName())) {
			CIAppService.saveCenternetInstallAppIntoDb(installedApp);
			// 通过广播通知FunctionSelectActivity更新界面
			Log.i(TAG, "save the app info into db");
		}
		this.sendBroadcast(new Intent(
				BroadCastReceiverAciton.BroadCast_Update_FunctionSelect));
	}

	private BroadcastReceiver ApkHandlerBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 接受安装apk后返回的广播
			String packageName = intent.getStringExtra("packageName");
			CenternetInstalledApp cia = null;
			if (null != packageName && !"".equals(packageName)) {
				cia = CIAppService.getAppInfoInSystemByPackageName(packageName);
			}
			//要求按照的文件名apkFileName要和launcher的名字相同
			if (intent.getAction().equals(
					BroadCastReceiverAciton.BroadCast_Install_App)) {
				if (null != cia
						&& false == CIAppService
								.isAppExistInDbByPackageName(packageName)
						&& apkFileName.equals(cia.getAppName())) {
					updateFunctionSelectActivityView(packageName);
					Log.i(TAG, "package name===="+packageName);
				}
			}
		}
	};

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		// 注册广播 注意在填加了新的action之后 registerReceiver时对应 filter的action也要加上
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastReceiverAciton.BroadCast_Install_App);
		this.registerReceiver(ApkHandlerBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() { 
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		this.unregisterReceiver(ApkHandlerBroadcastReceiver);
		finish();
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
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
	}

}
