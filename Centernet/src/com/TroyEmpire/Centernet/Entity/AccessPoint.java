/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.TroyEmpire.Centernet.Entity;

import com.TroyEmpire.Centernet.Constant.PskType;

import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class AccessPoint {

	private String ssid;
	private String bssid;
	private int security;
	private int networkId;
	private boolean wpsAvailable = false;
	private boolean ignored;
	private boolean alreadyConfigured;

	private String ccuPortalPacktVersions;

	private PskType pskType = PskType.UNKNOWN;

	private int mRssi;
	private WifiInfo mInfo;
	private DetailedState mState;

	
	
	public int getLevel() {
		if (mRssi == Integer.MAX_VALUE) {
			return -1;
		}
		return WifiManager.calculateSignalLevel(mRssi, 5);
	}
	public void setLevel(int level){
		this.mRssi = level;
	}

	public WifiInfo getInfo() {
		return mInfo;
	}

	public void setInfo(WifiInfo mInfo) {
		this.mInfo = mInfo;
	}

	public DetailedState getState() {
		return mState;
	}

	public void setState(DetailedState mState) {
		this.mState = mState;
	}

	/* getter and setter */
	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public boolean isWpsAvailable() {
		return wpsAvailable;
	}

	public void setWpsAvailable(boolean wpsAvailable) {
		this.wpsAvailable = wpsAvailable;
	}

	public PskType getPskType() {
		return pskType;
	}

	public void setPskType(PskType pskType) {
		this.pskType = pskType;
	}
	public boolean isIgnored() {
		return ignored;
	}
	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}
	public boolean isAlreadyConfigured() {
		return alreadyConfigured;
	}
	public void setAlreadyConfigured(boolean alreadyConfigured) {
		this.alreadyConfigured = alreadyConfigured;
	}
	public String getCcuPortalPacktVersions() {
		return ccuPortalPacktVersions;
	}
	public void setCcuPortalPacktVersions(String ccuPortalPacktVersions) {
		this.ccuPortalPacktVersions = ccuPortalPacktVersions;
	}
}
