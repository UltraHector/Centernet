package com.TroyEmpire.Centernet.Ghost.ICore;

import com.TroyEmpire.Centernet.Entity.AccessPoint;

public interface ILanAction {
	/**
	 * handshake with servers if there is some
	 * @param accesspoint currently connect to the AP
	 */
	public void handShakeServer(AccessPoint accessPoint);
}
