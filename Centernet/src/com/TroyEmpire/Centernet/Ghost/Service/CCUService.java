package com.TroyEmpire.Centernet.Ghost.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Entity.CenternetCentralUnit;
import com.TroyEmpire.Centernet.Entity.TCPProbeResponsePacket;
import com.TroyEmpire.Centernet.Ghost.Admin.AndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.IAdmin.IAndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.IService.ICCUService;
import com.TroyEmpire.Centernet.Util.IOUtil;
import com.TroyEmpire.Centernet.DB.CenternetCentralUnitDBManager;

public class CCUService implements ICCUService {

	private final String TAG = "com.TroyEmpire.Centernet.Ghost.Service.CenternetCentralUnitService";

	private CenternetCentralUnitDBManager ccuDbManager;
	private IAndroidWifiAdmin androidWifiAdmin;
	private Context context;

	public CCUService(Context context) {
		this.context = context;
		this.androidWifiAdmin = new AndroidWifiAdmin(this.context);
		this.ccuDbManager = new CenternetCentralUnitDBManager(context);
		iniRootStorage();
	}

	/*
	 * save the response packet into the database save the response body into
	 * the file system
	 */
	@Override
	public void saveNewPacketIntoDataBase(TCPProbeResponsePacket probeResponse,
			AccessPoint accessPoint) {
		CenternetCentralUnit ccu = loadCCU(probeResponse, accessPoint);
		if (probeResponse.getHead().isFirstContact()) {
			// first time to connect to this ccu
			ccuDbManager.save(ccu);
			saveTheBodyLoadZipIntoFileSystem(probeResponse);
		} else if (probeResponse.getHead().isUpdated()) {
			// new body arrived
			ccuDbManager.updateCCU(ccu);
			saveTheBodyLoadZipIntoFileSystem(probeResponse);
		}
	}

	private void saveTheBodyLoadZipIntoFileSystem(
			TCPProbeResponsePacket probeResponse) {
		/**
		 * the path pattern for the proberesponse body is root +
		 * "/body_serverid.zip" Note a consistent must be with the server: the
		 * file pattern for unzipped file should be in the folder with a path
		 * pattern: root + / Body_serverid /~
		 */
		String zipedFilePath = ConfigConstant.CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT
				+ "/Body_" + probeResponse.getHead().getCcuId() + ".zip";
		try {
			// save the zipped file
			File zipedBody = new File(zipedFilePath);
			if (!zipedBody.exists()) {
				zipedBody.createNewFile();
			}
			IOUtils.write(probeResponse.getBody().getBodyLoad(), new FileOutputStream(
					zipedBody));
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public void unZipPortalPacket(int ccuId) {

		String zipedFilePath = ConfigConstant.CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT
				+ "/Body_" + ccuId + ".zip";
		File zipedBody = new File(zipedFilePath);
		if (!zipedBody.exists())
			return;

		// unzip the body
		String unzippedFileFolderPath = ConfigConstant.CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT
				+ "/Body_" + ccuId;
		// create storage structures
		File unzippedFileFolder = new File(unzippedFileFolderPath);
		if (!unzippedFileFolder.exists())
			unzippedFileFolder.mkdirs();
		IOUtil.deleteFolderContents(unzippedFileFolder);

		if (IOUtil.unzipFile(zipedFilePath, unzippedFileFolderPath) == true)
			zipedBody.delete();
		else Log.e(TAG,"Unzip Body Error");
	}

	private CenternetCentralUnit loadCCU(TCPProbeResponsePacket probeResponse,
			AccessPoint accessPoint) {
		CenternetCentralUnit ccu = new CenternetCentralUnit();
		ccu.setBSSID(accessPoint.getBssid());
		ccu.setPortalPacketVersion(probeResponse.getHead().getContentLastModified());
		ccu.setBodyType(probeResponse.getHead().getBodyType());
		ccu.setCcuId(probeResponse.getHead().getCcuId());
		ccu.setTitile(probeResponse.getHead().getTitile());
		return ccu;
	}

	@Override
	public String formatCCUPortalPacketVersionsByAP(String BSSID) {
		StringBuffer formattedAPServerVersions = new StringBuffer();
		List<CenternetCentralUnit> ccus = ccuDbManager.getCCUsByAP(BSSID);
		// ccuId=version;
		// the ccus' versions are seperated by &
		// if formattedAPServerVersions is null, simply return "";
		for (CenternetCentralUnit ccu : ccus) {
			formattedAPServerVersions.append(ccu.getCcuId() + "="
					+ ccu.getPortalPacketVersion() + "&");
		}
		if (formattedAPServerVersions.length() > 0) {
			formattedAPServerVersions.substring(0,
					formattedAPServerVersions.length() - 2);
		}
		return formattedAPServerVersions.toString();
	}

	@Override
	public List<CenternetCentralUnit> getHistoryDataCCUInRange() {
		List<ScanResult> wifiList = androidWifiAdmin.getWifiList();
		List<CenternetCentralUnit> ccus = new ArrayList<CenternetCentralUnit>();
		if (wifiList == null)
			return null;
		for (ScanResult wifi : wifiList) {
			for (CenternetCentralUnit ccu : ccuDbManager
					.getCCUsByAP(wifi.BSSID)) {
				ccu.setRangeAvailable(true);
			}
		}
		return ccus;
	}

	@Override
	public List<CenternetCentralUnit> getHistoryDataCCUOutRange() {
		List<CenternetCentralUnit> ccus = ccuDbManager.getAllCCUs();
		if (ccus == null) {
			return null;
		}
		if (getHistoryDataCCUInRange() != null)
			ccus.removeAll(getHistoryDataCCUInRange());
		if (ccus.size() == 0) {
			return null;
		}
		for (CenternetCentralUnit ccu : ccus) {
			ccu.setRangeAvailable(false);
		}
		return ccus;
	}

	@Override
	public String getIndexPathLocationByCCUId(int ccuId) {

		unZipPortalPacket(ccuId);

		return ConfigConstant.CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT
				+ "/Body_" + ccuId;
	}

	private boolean iniRootStorage() {
		File root = new File(
				ConfigConstant.CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT);
		if (!root.exists())
			try {
				return root.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}

	@Override
	public void finalize() {
		try {
			ccuDbManager.closeDB();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteHistorDataByCCUId(int ccuId) {
		ccuDbManager.deleteCCU(ccuId);

	}
}
