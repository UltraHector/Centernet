package com.TroyEmpire.Centernet.Entity;

import java.io.Serializable;

import com.TroyEmpire.Centernet.Constant.OSType;

public class UdpProbeRequestPacket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OSType OSType;
	private int osVersion;
	private int tcpPort;
	/*
	 * portalPacketsVersion a string represents the versions of the cached
	 * portalpacket in the machine, one AP can have several CCUs attached one
	 * one CCU correspondents a portalpacket version; 
	 */
	private String portalPacketsVersion;

	public OSType getOSType() {
		return OSType;
	}

	public UdpProbeRequestPacket() {

	}

	public UdpProbeRequestPacket(UdpProbeRequestPacket packet) {
		this.OSType = packet.OSType;
		this.osVersion = packet.getOsVersion();
		this.tcpPort = packet.getTcpPort();
		this.portalPacketsVersion = packet.portalPacketsVersion;
	}

	public void setOSType(OSType oSType) {
		OSType = oSType;
	}

	public int getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(int osVersion) {
		this.osVersion = osVersion;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getPortalPacketsVersion() {
		return portalPacketsVersion;
	}

	public void setPortalPacketsVersion(String portalPacketsVersion) {
		this.portalPacketsVersion = portalPacketsVersion;
	}
}
