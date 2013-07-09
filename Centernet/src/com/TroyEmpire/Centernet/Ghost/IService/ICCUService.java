package com.TroyEmpire.Centernet.Ghost.IService;

import java.util.List;

import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Entity.CenternetCentralUnit;
import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacket;

public interface ICCUService {
	/**
	 * format all CCU portal packet versions within a AP into a formatted string
	 * 
	 * @param BSSID
	 *            the AP's BSSID
	 * @return a formatted string, example:
	 *         "CCU_1_Id=versionNumber&CCU_2_ID= versionNumber" the version
	 *         number can be the lastmodified time of the packet
	 */
	public String formatCCUPortalPacketVersionsByAP(String BSSID);

	/**
	 * save a new packet send from a ccu which attached to an accesspoint
	 * 
	 * @param accesspoint
	 *            the AP which is attached to by the ccu
	 * @param probeRequest
	 *            the response packet sent from the ccu
	 */
	void saveNewPacketIntoDataBase(TCPProbeResponsePacket probeRequest,
			AccessPoint accessPoint);

	/**
	 * get the history data for the ccus which are attached to the APs which are
	 * currently in range
	 */
	public List<CenternetCentralUnit> getHistoryDataCCUInRange();

	/**
	 * get the history data for the ccus which are attached to the APs which are
	 * currently out of range
	 */
	List<CenternetCentralUnit> getHistoryDataCCUOutRange();

	/**
	 * @param ccuId
	 *            the ccuId for the request ccu, which always be a hashcode of
	 *            the ccu's mac
	 * @return the path of the folder directly contains the index for the
	 *         specific serverId.
	 */
	
	String getIndexPathLocationByCCUId(int ccuId);
	
	
	/**
	 * @param ccuId the ccuid whose portalpacket will be deleted
	 * the 
	 */
	void deleteHistorDataByCCUId(int ccuId);
	

	/**
	 * we seperate the unzip work away from scanning wifi this method will be
	 * called only onece when user click the portal packet title
	 */
	void unZipPortalPacket(int ccuId);

	/**
	 * do somethind like close the database when necessary
	 */
	public void finalize();
}
