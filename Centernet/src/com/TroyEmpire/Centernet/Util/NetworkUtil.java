package com.TroyEmpire.Centernet.Util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {

	private static final Pattern IPV4_PATTERN = Pattern
			.compile("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

	/**
	 * Return the local ip address in a format: 127.0.0.1
	 */
	public static String getAndroidIP() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return extracSingleIpV4addressFromString(inetAddress
								.getHostAddress());
					}
				}
			}
		} catch (SocketException ex) {
			return null;
		}
		return null;
	}

	/**
	 * @param text
	 *            contains a ipv4 address with in it
	 * @return ip address
	 */
	public static String extracSingleIpV4addressFromString(String text) {
		Matcher m = IPV4_PATTERN.matcher(text);
		while (m.find()) {
			String ip = m.group(0);
			/* only extract the first match */
			return ip;
		}
		return null;
	}

}
