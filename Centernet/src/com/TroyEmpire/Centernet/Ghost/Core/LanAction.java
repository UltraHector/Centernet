package com.TroyEmpire.Centernet.Ghost.Core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacket;
import com.TroyEmpire.Centernet.Ghost.ICore.ILanAction;
import com.TroyEmpire.Centernet.Ghost.IService.ICCUService;
import com.TroyEmpire.Centernet.Ghost.IService.IConfigureDataPacketService;
import com.TroyEmpire.Centernet.Ghost.Service.CCUService;
import com.TroyEmpire.Centernet.Ghost.Service.ConfigureDataPacketService;
import com.TroyEmpire.Centernet.Util.IOUtil;

public class LanAction implements ILanAction {

	private final String TAG = "com.TroyEmpire.Centernet.Ghost.Core.LanAction";

	private DatagramSocket udpSocket;
	private ServerSocket tcpSocket;
	private DatagramPacket probePacket;
	private IConfigureDataPacketService configureDataPacketService;
	private ICCUService ccuService;
	private Timer timer;
	private Context context;

	public LanAction(Context context) {
		this.context = context;
		configureDataPacketService = new ConfigureDataPacketService();
		new CCUService(context);
		ccuService = new CCUService(context);

		try {
			// create a random port udp socket
			this.udpSocket = new DatagramSocket();
			// create a random port tcp socket
			this.tcpSocket = new ServerSocket(0);
		} catch (Exception e) {
			Log.e(TAG, "Open sockets error!");
		}
	}

	/**
	 * send the UPD broadcast packet to the ap's LAN close the udp socket when
	 * any exception happened
	 */
	private boolean sendUdpProbe(AccessPoint accessPoint) {
		// Configure the udp packet and set it to broadcast
		try {
			this.probePacket = configureDataPacketService.configureDataPacket(
					accessPoint, tcpSocket.getLocalPort());
			this.udpSocket.setBroadcast(true);
		} catch (Exception e1) {
			Log.e(TAG, "Configure udp packet error!");
			return false;
		}
		// Try time to wait the network is available
		byte count = 0;
		// Try the 255.255.255.255
		while (true) {
			try {
				udpSocket.send(this.probePacket);
				Log.i(TAG,
						">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
				// close the UDP socket after sending the probe
				return true;
			} catch (SocketException ex) {
				/*
				 * do not end the loop to another try if the network is not
				 * reachable
				 */
				try {
					Thread.sleep(ConfigConstant.TRY_SEND_PACKET_INTERVAL);
					if (++count >= ConfigConstant.TRY_SEND_PACKET_TIMES) {
						Log.e(TAG,
								"Network is broken, abort trying to send udp pacekt");
						return false;
					} else {
						continue;
					}
				} catch (InterruptedException e) {
					return false;
				}
			} catch (Exception e) {
				Log.e("Client Send Probe", e.toString());
				return false;
			}
		}
	}

	private void handleResponse(final AccessPoint accessPoint) {
		try {
			// try to receive the response from several servers
			while (true) {
				final Socket connectionSocket = tcpSocket.accept();
				// extract the object
				Thread child = new Thread() {
					@Override
					public void run() {
						Log.i(TAG, "Get a portal response from a CCU");
						// deal with the received data
						handleReceivedSocket(connectionSocket, accessPoint);
					}
				};
				// start a new thread to handle the response data, let the
				// socket receive other response
				child.start();
			}
		} catch (SocketException ex) {
			/* The socket is closed, perhaps by the timer */
			Log.i(TAG, "End while: Socket closed");
		} catch (Exception e) {
			/* TODO something unexpected happened */
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	private void handleReceivedSocket(Socket connectionSocket,
			AccessPoint accessPoint) {

		try {
			handleReceivedPacket(
					(TCPProbeResponsePacket) IOUtil.convertStreamToObject(connectionSocket
							.getInputStream()), accessPoint);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the TCP response packet into database and trigger new arrival packet
	 * action
	 */
	private void handleReceivedPacket(TCPProbeResponsePacket probeResponse,
			AccessPoint accessPoint) {
		try {
			// Save the received packet into database
			ccuService.saveNewPacketIntoDataBase(probeResponse, accessPoint);

			// broad cast a new packet has been received
			Intent intent = new Intent(
					ConfigConstant.BROADCAST_NEW_PROBE_RESPONE_ACTION);
			intent.putExtra(ConfigConstant.BROADCAST_NEW_PROBE_RESPONE_VALUE,
					probeResponse.getHead());
			context.sendBroadcast(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handShakeServer(AccessPoint accessPoint) {
		if (sendUdpProbe(accessPoint)) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					try {
						tcpSocket.close();
					} catch (IOException e) {
					}
					Log.i(TAG, "Close the socket when waiting time is up");
					return;
				}
			}, ConfigConstant.MAX_WAIT_RESPONSE_HANDSHAKE);
			// Wait for a response
			handleResponse(accessPoint);
			// Close the port!
			timer.cancel();
		}

		// close the socket
		try {
			if (!tcpSocket.isClosed())
				tcpSocket.close();
			if (!udpSocket.isClosed())
				udpSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
