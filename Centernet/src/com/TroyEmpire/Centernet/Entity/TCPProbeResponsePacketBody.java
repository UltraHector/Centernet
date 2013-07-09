package com.TroyEmpire.Centernet.Entity;

import java.io.Serializable;

public class TCPProbeResponsePacketBody implements Serializable{
	
	private byte[] bodyLoad;
	
	public byte[] getBodyLoad() {
		return bodyLoad;
	}

	public void setBodyLoad(byte[] bodyLoad) {
		this.bodyLoad = bodyLoad;
	}
}
