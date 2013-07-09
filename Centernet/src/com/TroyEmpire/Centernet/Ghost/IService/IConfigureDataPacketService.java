package com.TroyEmpire.Centernet.Ghost.IService;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

import com.TroyEmpire.Centernet.Entity.AccessPoint;

public interface IConfigureDataPacketService {
	/**
	 * Load the UDP probe package
	 * @param accessPoint current attached to the AP
	 * @param tcpPort the tcp socket number which wait for the tcp response
	 * @throws UnknownHostException 
	 */
	DatagramPacket configureDataPacket(AccessPoint accessPoint, int tcpPort)
			throws UnknownHostException;
}
