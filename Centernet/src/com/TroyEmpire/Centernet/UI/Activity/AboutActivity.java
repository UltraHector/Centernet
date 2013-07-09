package com.TroyEmpire.Centernet.UI.Activity;

import com.TroyEmpire.Centernet.UI.Activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// 设置标题栏的名称
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.function_about);
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
