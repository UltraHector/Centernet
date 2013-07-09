package com.TroyEmpire.Centernet.Entity;

import java.io.Serializable;
import com.TroyEmpire.Centernet.Constant.ProbeResponseBodyType;

public class TCPProbeResponsePacket implements Serializable {
	/**
	 * should be serializable
	 */
	private static final long serialVersionUID = 1L;

	private TCPProbeResponsePacketBody body;
	private TCPProbeResponsePacketHead head;
	

	public TCPProbeResponsePacket() {
		body = new TCPProbeResponsePacketBody();
		head = new TCPProbeResponsePacketHead();
	}


	public TCPProbeResponsePacketBody getBody() {
		return body;
	}


	public void setBody(TCPProbeResponsePacketBody body) {
		this.body = body;
	}


	public TCPProbeResponsePacketHead getHead() {
		return head;
	}


	public void setHead(TCPProbeResponsePacketHead head) {
		this.head = head;
	}

//	public TCPProbeResponsePacket(TCPProbeResponsePacket probeResponse) {
//		this.titile = probeResponse.getTitile();
//		this.setCcuId(probeResponse.getCcuId());
//		this.updated = probeResponse.isUpdated();
//		this.firstContact = probeResponse.firstContact;
//		this.bodyType = probeResponse.getBodyType();
//		this.contentLastModified = probeResponse.getContentLastModified();
//		if (probeResponse.getBodyLoad() != null) {
//			this.bodyLoad = probeResponse.getBodyLoad().clone();
//		}
//	}

}
