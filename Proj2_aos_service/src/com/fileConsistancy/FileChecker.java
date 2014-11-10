package com.fileConsistancy;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashMap;

import com.constants.SingletonRecords;
import com.constants.StringConstants;
import com.server.GetInfo;
import com.vo.FileVO;

public class FileChecker implements Runnable {

	// need to initally need to set the hashtable
	@Override
	public void run() {
		// this is for a push based consistancy
		// i need to fill the hash masters hashtable
		// initial loop to know check if all the contents are there
		while (!(StringConstants.ip != null
				&& StringConstants.ip_config != null
				&& StringConstants.MasterDir != null
				&& StringConstants.servername != null
				&& StringConstants.DownloadDir != null && StringConstants.port != 0)) {
			// this will keep going till all the parameter are set
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// initial file upload
		File masterFile = new File(StringConstants.MasterDir);
		File[] listFiles = masterFile.listFiles();
		SingletonRecords slr = SingletonRecords.getInstance();
		
		for (File file : listFiles) {
			slr.mastermap.put(file.getName(), new FileVO(file.lastModified(),
					StringConstants.servername, StringConstants.ip,
					StringConstants.port));
		}

		HashMap<String, FileVO> tempVOMaster = new HashMap<String, FileVO>(
				slr.mastermap);
		StringConstants.initialLoad = true;

		// setting the master file
		while (true) {
			// checking only the modification only not if the file is deleted
			listFiles = masterFile.listFiles();
			for (File file : listFiles) {

				if (tempVOMaster.get(file.getName()).getModifiedDate() != slr.mastermap
						.get(file.getName()).getModifiedDate()) {
					slr.mastermap.get(file.getName()).setModifiedDate(
							tempVOMaster.get(file.getName()).getModifiedDate());
					slr.mastermap.get(file.getName()).setVersion(
							tempVOMaster.get(file.getName()).getVersion() + 1);
					tempVOMaster = new HashMap<String, FileVO>(slr.mastermap);
					
					try {
						new GetInfo().invalidation(file.getName(),
								tempVOMaster.get(file));
					} catch (IOException | NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
