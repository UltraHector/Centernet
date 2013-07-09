package com.TroyEmpire.Centernet.Ghost.Core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Ghost.Admin.AndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.IAdmin.IAndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.ICore.ICenternetScanner;
import com.TroyEmpire.Centernet.Ghost.ICore.ILanAction;
import com.TroyEmpire.Centernet.Ghost.IService.ICCUService;
import com.TroyEmpire.Centernet.Ghost.IService.IAccessPointService;
import com.TroyEmpire.Centernet.Ghost.Service.CCUService;
import com.TroyEmpire.Centernet.Ghost.Service.AccessPointService;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.widget.ProgressBar;

public class CenternetScannner implements ICenternetScanner {

	private static final String TAG = "com.TroyEmpire.Centernet.Ghost.Core.CenternetScannner";

	private Context context;
	private IAndroidWifiAdmin wifiAdmin;
	private Timer timer;
	private boolean flagConneted;
	private boolean flagAbortTry;
	private ILanAction lanAction;
	private ICCUService ccuService;
	private IAccessPointService apService;
	private AccessPoint accessPoint;

	public CenternetScannner(Context context) {
		this.context = context;
		wifiAdmin = new AndroidWifiAdmin(this.context);
		lanAction = new LanAction(context);
		accessPoint = new AccessPoint();
		apService = new AccessPointService(context);
		ccuService = new CCUService(context);
	}

	@Override
	public void scanConfiguredNetwork(ProgressBar bar) {
		wifiAdmin.startScan();
		List<ScanResult> scanResults = wifiAdmin.getWifiList();
		Log.i(TAG, "共扫描的Wifi：  " + scanResults.size() + " 个");
		Set<String> ssidSet = new HashSet<String>();
		for (int i = 0; i < scanResults.size(); i++) {
			ScanResult result = scanResults.get(i);
			/* Already visited a LAN with the same SSID */
			if (!ssidSet.add(result.SSID)) {
				Log.i("Already Visited ", result.SSID);
				continue;
			}
			if (apService.checkWhetherWifiIgnored(result.BSSID)) {
				continue;
			}
			/* Already in the LAN */
			else if (wifiAdmin.getSSID().equals(result.SSID)) {
				Log.i("Already In Lan: ", result.SSID);
				/* Do some job if already in the LAN */
				jobAfterJoinLan();
				continue;
			}
			/* Try Enter A new LAN */
			else {
				Log.i("Try Enter A new LAN ", result.SSID);
				dealWithALan(result, null);
			}
			// TODO
			Log.i(TAG, "index i=" + i);
			bar.setProgress(((i + 1) * 100) / scanResults.size());

		}
		
	}

	@Override
	public void scanSpecifiedConfiguredNetwork(List<String> BSSID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobAfterJoinLan() {
		ActionInALanThread handShake = new ActionInALanThread();
		Thread lanAction = new Thread(handShake);
		lanAction.start();
		try {
			// TODO
			/*
			 * now the job is blocking the main thread, which is really bad but
			 * we currently have no choice, one network card can only have on ip
			 * address
			 */
			lanAction.join();
		} catch (InterruptedException e) {
			Log.i("Thread Error", "A lan action has not finished");
		}

	}

	/**
	 * Deal with a lan
	 */
	private void dealWithALan(final ScanResult result, String key) {
		if (wifiAdmin.connetWifiByScanResult(result) == true) {
			timer = new Timer(true);
			flagConneted = false;
			flagAbortTry = false;
			timer.schedule(new TimerTask() {
				public void run() {
					timer.cancel();
					flagAbortTry = true;
					Log.i("Connect Error ", "Is configured but cannot connect");
				}
			}, ConfigConstant.MAX_WIFI_TRY_TIME);
			timer.schedule(new TimerTask() {
				public void run() {
					if (wifiAdmin.getBSSID() == null
							|| !wifiAdmin.getBSSID().equals(result.BSSID)) {
						// try to update the BSSID to see whether the
						// sys catch the new network
						wifiAdmin.refreshConnectWifiInto();
					} else {
						timer.cancel();
						flagConneted = true;
					}
				}
			}, 0, ConfigConstant.TRY_WIFI_INTERVAL);
			while (!flagConneted && !flagAbortTry) {
				/*
				 * Block the thread Wait if the system have not pick up a new
				 * LAN
				 */
			}
			/* Do some job after join the LAN */
			if (flagConneted) {
				// attached to the accesspoint
				accessPoint.setBssid(result.BSSID);
				accessPoint
						.setCcuPortalPacktVersions(ccuService
								.formatCCUPortalPacketVersionsByAP(wifiAdmin
										.getBSSID()));
				jobAfterJoinLan();
			}
		} else {
			Log.i("Scan Protected Wifi", " Not configured and has password");
		}
	}

	/* Create another thread not block the main thread */
	private class ActionInALanThread implements Runnable {
		@Override
		public void run() {
			lanAction.handShakeServer(accessPoint);
		}
	}

}
