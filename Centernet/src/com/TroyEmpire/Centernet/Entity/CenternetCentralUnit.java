package com.TroyEmpire.Centernet.Entity;

import com.TroyEmpire.Centernet.Constant.ProbeResponseBodyType;


/**
 * A entity save servers for a specific AP, a AP can have multiple ccus
 * attached
 */
public class CenternetCentralUnit {
	
    private long   _id;
	
    private String BSSID;
    
	/** the server mathine's MAC address's hashcode
	 * should be unique within in a LAN 
	 * in case two machines use the same id
	 */
    private int ccuId;
    
    private boolean rangeAvailable;
	
    private String portalPacketVersion;
    
    // derived from the proberesponsepacket
    private String titile;
	private ProbeResponseBodyType bodyType;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getBSSID() {
		return BSSID;
	}

	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}


	public ProbeResponseBodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(ProbeResponseBodyType bodyType) {
		this.bodyType = bodyType;
	}

	public boolean isRangeAvailable() {
		return rangeAvailable;
	}

	public void setRangeAvailable(boolean rangeAvailable) {
		this.rangeAvailable = rangeAvailable;
	}

	public int getCcuId() {
		return ccuId;
	}

	public void setCcuId(int ccuId) {
		this.ccuId = ccuId;
	}

	public String getPortalPacketVersion() {
		return portalPacketVersion;
	}

	public void setPortalPacketVersion(String portalPacketVersion) {
		this.portalPacketVersion = portalPacketVersion;
	}
}
