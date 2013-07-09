package com.TroyEmpire.Centernet.Ghost.ICore;

import java.util.List;

import android.widget.ProgressBar;

public interface ICenternetScanner {

	/**
	 * Scan ALl the congigured network
	 * if open network, just simply configure it
	 */
	void scanConfiguredNetwork(ProgressBar bar);

	void scanSpecifiedConfiguredNetwork(List<String> BSSID);

	/** 
	 * Do Jobs after join the LAN
	 */
	void jobAfterJoinLan();

}
