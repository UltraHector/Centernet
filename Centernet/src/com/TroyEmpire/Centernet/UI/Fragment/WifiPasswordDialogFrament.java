package com.TroyEmpire.Centernet.UI.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.TroyEmpire.Centernet.UI.Activity.R;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Entity.AccessPoint;

@SuppressLint("ValidFragment")
// API = 8 and above
public class WifiPasswordDialogFrament extends DialogFragment {

	private Activity activity;
	private AccessPoint ap;
	private View dialogView;
	private SharedPreferences wifiPasswordInfo;

	@SuppressLint("ValidFragment")
	public WifiPasswordDialogFrament(Activity activity, AccessPoint ap) {
		this.activity = activity;
		this.ap = ap;
		this.wifiPasswordInfo = activity.getSharedPreferences(
				ConfigConstant.WIFI_PASSWORD_SHARED_REFEREMCE,
				Context.MODE_PRIVATE);
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialogView = inflater.inflate(R.layout.dialog_wifi_password, null);
		builder.setView(dialogView)
				.setTitle(ap.getSsid())
				// click the confirm button and call the function
				.setPositiveButton(R.string.confirm,
						new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Do nothing , we will override it
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								WifiPasswordDialogFrament.this.getDialog()
										.cancel();
							}
						});

		// ----------------------------------------------------------
		// Override the onclick() of positive button API 8 and above
		// ----------------------------------------------------------
		final AlertDialog dialog_ready = builder.create();
		dialog_ready.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button b = dialog_ready
						.getButton(DialogInterface.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO what ?
					}
				});
			}
		});
		return dialog_ready;
	}

	private void saveUserInfo() {
		EditText wifiPassword = (EditText) (dialogView
				.findViewById(R.id.wifi_password));
		CheckBox saveInfo = (CheckBox) (dialogView
				.findViewById(R.id.wifi_save_password_checkbox));
		// Save the Jwc input by the user into the system
		if (saveInfo.isChecked()) {
			SharedPreferences.Editor editor = wifiPasswordInfo.edit();
			editor.putString(ap.getBssid(), wifiPassword.getText().toString());
			editor.commit(); // save the data
		}
	}
}
