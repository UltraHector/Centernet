package com.TroyEmpire.Centernet.UI.Entity;

import android.graphics.drawable.Drawable;

public class FunctionSelectGridViewItem {
	private String funcName;
	private Drawable funcIcon;
	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public FunctionSelectGridViewItem() {
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public Drawable getFuncIcon() {
		return funcIcon;
	}

	public void setFuncIcon(Drawable funcIcon) {
		this.funcIcon = funcIcon;
	}
}
