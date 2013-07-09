package com.TroyEmpire.Centernet.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

public class LoadingActivity extends Activity {

	private boolean _touched = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		Handler x = new Handler();
		x.postDelayed(new splashhandler(), 1500);
	}

	class splashhandler implements Runnable {
		public void run() {
			if (!_touched) {
				startActivity(new Intent(getApplication(),
						FunctionSelectActivity.class));
				finish();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_touched = true;
			startActivity(new Intent(getApplication(),
					FunctionSelectActivity.class));
			finish();
		}
		return true;
	}
}
