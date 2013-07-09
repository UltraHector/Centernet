package com.TroyEmpire.Centernet.Entity;

import java.io.Serializable;

import com.TroyEmpire.Centernet.Constant.ProbeResponseBodyType;

public class TCPProbeResponsePacketHead implements Serializable{
	private String titile;
	private int ccuId;
	private boolean updated;
	private boolean firstContact;
	private ProbeResponseBodyType bodyType;
	private String contentLastModified;// time in milli
	
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

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isFirstContact() {
		return firstContact;
	}

	public void setFirstCotact(boolean firstContact) {
		this.firstContact = firstContact;
	}

	public int getCcuId() {
		return ccuId;
	}

	public void setCcuId(int ccuId) {
		this.ccuId = ccuId;
	}

	public String getContentLastModified() {
		return contentLastModified;
	}

	public void setContentLastModified(String contentLastModified) {
		this.contentLastModified = contentLastModified;
	}
}
