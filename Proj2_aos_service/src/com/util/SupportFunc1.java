package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import com.Com_InterFace;
import com.constants.StringConstants;
import com.vo.FileVO;
import com.vo.Vo;

public class SupportFunc1 {
	private String message_ID = null;
	private int TTL = 0;
	private String file_name = null;

	// you are using  the constuctor  and getting the values from the server
	public SupportFunc1(String message_ID, int TTL, String file_name) {
		this.message_ID = message_ID;
		this.TTL = TTL;
		this.file_name = file_name;

	}
public SupportFunc1() {
}
	// this is to check if the file exist
	public boolean checkFileLocaly(String file_name) {
		boolean foundFile = false;
		File file = new File(StringConstants.MasterDir);
		File[] subFiles = file.listFiles();

		for (File file2 : subFiles) {
			if (file2.getName().matches(file_name) == true) {
				foundFile = true;
			}
		}
		return foundFile;
	}

	public boolean check_configFile() throws IOException {

		boolean present = false;
		File temp = new File(StringConstants.ip_config);

		if (temp.exists()) {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(temp));
			if (br.readLine() == null) {
				/*
				 * System.out.println("No errors, and file empty"); // here you
				 * can put the return statement
				 */
				System.out.println("config file empty");
				present = false;
			} else {
				present = true;
			}
		} else {
			System.out.println("config file not set");
			present = false;
		}

		return present;
	}

	public ArrayList<Vo> getPeerList() throws FileNotFoundException {
		ArrayList<Vo> peerList = new ArrayList<Vo>();

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(new File(StringConstants.ip_config));
		while (scan.hasNext()) {
			String[] temp = scan.next().split(":");
			peerList.add(new Vo(temp[0], temp[1], Integer.parseInt(temp[2])));

		}
		return peerList;

	}

	public ArrayList<Vo> checkPeers(String indexServerName, String IP, int port)
			throws NotBoundException, IOException {
		Registry registry = LocateRegistry.getRegistry(IP, port);
		Com_InterFace comp = (Com_InterFace) registry.lookup(indexServerName);
		try {
			return comp.query(message_ID, TTL, file_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// used to call the invalidation purpose
	public void checkPeersFiles(String indexServerName, String IP, int port,String filename,FileVO fvo)
			throws NotBoundException, IOException {
		Registry registry = LocateRegistry.getRegistry(IP, port);
		Com_InterFace comp = (Com_InterFace) registry.lookup(indexServerName);
		try {
			 comp.invalidation(filename, fvo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
