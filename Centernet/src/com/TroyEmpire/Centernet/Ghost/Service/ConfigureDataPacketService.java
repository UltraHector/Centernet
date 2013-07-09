package com.TroyEmpire.Centernet.Ghost.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Constant.OSType;
import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Entity.UdpProbeRequestPacket;
import com.TroyEmpire.Centernet.Ghost.IService.IConfigureDataPacketService;
import com.TroyEmpire.Centernet.Util.IOUtil;

public class ConfigureDataPacketService implements IConfigureDataPacketService {

	@Override
	public DatagramPacket configureDataPacket(AccessPoint accessPoint, int tcpPort)
			throws UnknownHostException {
		DatagramPacket sendPacket = null;

		// Construct the packet
		UdpProbeRequestPacket packet = new UdpProbeRequestPacket();
		int androidVersion = android.os.Build.VERSION.SDK_INT;
		packet.setOsVersion(androidVersion);
		packet.setOSType(OSType.ANDROID);
		packet.setPortalPacketsVersion(accessPoint.getCcuPortalPacktVersions());
		packet.setTcpPort(tcpPort);

		try {
			// Serialize to a byte array
			byte[] serializedMessage = IOUtil.convertObjectToBytes(packet);

			// send the data
			sendPacket = new DatagramPacket(serializedMessage,
					serializedMessage.length,
					InetAddress.getByName("255.255.255.255"),
					ConfigConstant.PROBE_SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendPacket;
	}

}
