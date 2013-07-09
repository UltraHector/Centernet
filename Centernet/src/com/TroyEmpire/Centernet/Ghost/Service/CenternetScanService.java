package com.TroyEmpire.Centernet.Ghost.Service;

import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacket;
import com.TroyEmpire.Centernet.Ghost.Core.CenternetScannner;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetScanService;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class CenternetScanService extends Service implements
		ICenternetScanService {

	private final static String LOG_TAG = "com.TroyEmpire.Centernet.Ghost.Service.CenternetScanService";

	private IBinder binder = new CenternetScanBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class CenternetScanBinder extends Binder {
		public CenternetScanService getService() {
			return CenternetScanService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(LOG_TAG, "CenternetScanService Service Destroyed.");
	}

	@Override
	public void startCenternetScan(final ProgressBar bar) {
		// start a centernet scan
		AsyncTask<Void, TCPProbeResponsePacket, Void> task = new AsyncTask<Void, TCPProbeResponsePacket, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				new CenternetScannner(CenternetScanService.this)
						.scanConfiguredNetwork(bar);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				bar.setVisibility(View.GONE);
			}

			@Override
			protected void onPreExecute() {
				Log.i(LOG_TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(TCPProbeResponsePacket... values) {
				super.onProgressUpdate(values);

			}
		};
		task.execute();
	}
}
