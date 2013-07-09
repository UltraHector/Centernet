package com.TroyEmpire.Centernet.UI.BroadcastReceiver;

import com.TroyEmpire.Centernet.Constant.BroadCastReceiverAciton;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CenternetInstalledAppBroadCast extends BroadcastReceiver {

	private final static String TAG = "com.TroyEmpire.Centernet.UI.BroadcastReceiver.CenternetInstalledAppBR";

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = "";
		if (null != intent) {
			packageName = intent.getDataString();
			packageName = packageName.replace("package:", "");
		}
		if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
			Log.i(TAG, "added");
			Intent addIntent = new Intent(
					BroadCastReceiverAciton.BroadCast_Install_App);
			addIntent.putExtra("packageName", packageName);
			Log.i(TAG, "packageName==" + packageName);
			context.sendBroadcast(addIntent);

		} else if ("android.intent.action.PACKAGE_REMOVED".equals(intent
				.getAction())) {
			Log.i(TAG, "remove");
			Intent removeIntent = new Intent(
					BroadCastReceiverAciton.BroadCast_Remove_App);
			removeIntent.putExtra("packageName", packageName);
			context.sendBroadcast(removeIntent);

		} else if ("android.intent.action.PACKAGE_REPLACED".equals(intent
				.getAction())) {
			Log.i(TAG, "replace");
			Intent replaceIntent = new Intent(
					BroadCastReceiverAciton.BroadCast_Replace_App);
			replaceIntent.putExtra("packageName", packageName);
			context.sendBroadcast(replaceIntent);
		}
	}
}
