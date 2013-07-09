package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.Entity.LocationPosition;
import com.TroyEmpire.Centernet.Ghost.IService.IGpsService;
import com.TroyEmpire.Centernet.Ghost.Service.GpsService;
import com.TroyEmpire.Centernet.Util.Checker;
import com.TroyEmpire.Centernet.Util.Checker.Resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GPSLocationActivity extends Activity implements OnClickListener {

	private static final String TAG = "com.TroyEmpire.Centernet.UI.Activity.GPSLocationActivity";
	private IGpsService gpsService;
	private LocationManager locationManager;
	private TextView latitude;
	private TextView longtituede;
	private Button refreshButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_gps_location);
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.gpsService = new GpsService(locationManager);
		latitude = (TextView) findViewById(R.id.latitudeTv);
		longtituede = (TextView) findViewById(R.id.longtitudeTv);
		refreshButton = (Button) findViewById(R.id.refreshLocation);
		refreshButton.setOnClickListener(this);
		TextView title = (TextView) this.findViewById(R.id.title_text);
		title.setText("Location Service");
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates((LocationListener) gpsService);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Checker(this).pass(new Checker.Pass() {
			@Override
			public void pass() {
				locationManager.requestLocationUpdates(
						gpsService.getProvider(), 400, 1,
						(LocationListener) gpsService);
				Log.i(TAG, "check");
			}
		}).check(Resource.GPS, Resource.NETWORK);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refreshLocation:
			LocationPosition location = gpsService.getLocationPosition();
			latitude.setText(String.valueOf(location.getLatitude()));
			longtituede.setText(String.valueOf(location.getLongitude()));
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
