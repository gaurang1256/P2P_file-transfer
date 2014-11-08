package com.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import com.Com_InterFace;
import com.constants.StringConstants;
import com.vo.Vo;

public class GetInfo extends UnicastRemoteObject implements Serializable,
		Com_InterFace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message_ID = null;
	private int TTL = 0;
	private String file_name = null;

	public GetInfo() throws RemoteException {
		super();
	}

	@Override
	public String testRMI() throws RemoteException {

		return "this is ip" + StringConstants.ip + " and your port is "
				+ StringConstants.port;
	}

	@Override
	public ArrayList<Vo> query(String message_ID, int TTL, String file_name)
			throws IOException, NotBoundException, RemoteException {

		this.message_ID = message_ID;
		this.TTL = TTL;
		this.file_name = file_name;

		ArrayList<Vo> returnUrl = new ArrayList<Vo>();
		try {

			if (TTL <= StringConstants.TTL) {
				// checking locally
				System.out.println("checking localy");
				if (checkFileLocaly(file_name) == true) {

					returnUrl.add(new Vo(StringConstants.servername,
							StringConstants.ip, StringConstants.port));

					TTL++;// i need to incriment the time to live
				}
				if (check_configFile() == true) {
					System.out.println("checking with the peers");
					ArrayList<Vo> tempList = getPeerList();
					for (Vo vo : tempList) {

						ArrayList<Vo> tempgetBack = checkPeers(
								vo.getIndexName(), vo.getPeerServer_IP(),
								vo.getPeerServer_port());

						if (tempgetBack.isEmpty() != true) {
							returnUrl.addAll(tempgetBack);
						}
					}

				}
			}
		} catch (MalformedURLException e) {
			System.err
					.println("arvind: you have made an error while making the url");
			e.printStackTrace();
		}

		return returnUrl;
	}

	// this is to check if the file exist
	public boolean checkFileLocaly(String file_name) {
		boolean foundFile = false;
		File file = new File(StringConstants.localDir);
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

	@Override
	public byte[] obtain(String Filename) throws RemoteException,
			FileNotFoundException {
		System.out.println("inside the obtain file");
		// FileVo fvo = new FileVo();
		byte[] bucket = new byte[1024];
		
		InputStream inStream = new BufferedInputStream(new FileInputStream(new File(
				StringConstants.localDir + "/" + Filename)));
		int bytesRead = 0;
		while (bytesRead >-1) {
			try {
				bytesRead = inStream.read(bucket);

			} catch (IOException e) {
				System.err.println("error while you are transfering the file");
				e.printStackTrace();
			}
		}
		try {
			inStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bucket;

	}

	public void getFileData(String Filename, Vo valueObj) {
		try {
			System.out.println("you are in the get file data");
			System.out.println(valueObj.toString());

			Registry registry = LocateRegistry.getRegistry(
					valueObj.getPeerServer_IP(), valueObj.getPeerServer_port());

			Com_InterFace comp = (Com_InterFace) registry.lookup(valueObj
					.getIndexName());

			System.out.println("got input from the server");

			byte[] bucket = comp.obtain(Filename);
			System.out.println("got ");
			/*
			 * if (fvoGet == null) System.out.println("there is no input");
			 */
		//	byte[] bucket = fvoGet;

			BufferedOutputStream bot = new BufferedOutputStream(new FileOutputStream(
					new File(StringConstants.localDir + "/" + Filename)));
			System.out.println("bucket length" + bucket.length);
			
			bot.write(bucket, 0, bucket.length);// assuming 10 mb of file at the
			// max
			bot.flush();
			bot.close();
			System.out.println("file transfered");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
