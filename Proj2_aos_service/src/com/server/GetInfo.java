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
import com.constants.SingletonRecords;
import com.constants.StringConstants;
import com.util.SupportFunc1;
import com.vo.FileVO;
import com.vo.TransferVo;
import com.vo.Vo;

public class GetInfo extends UnicastRemoteObject implements Serializable,
		Com_InterFace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

		// this is the function for the query
		SupportFunc1 sf1 = new SupportFunc1(message_ID, TTL, file_name);

		ArrayList<Vo> returnUrl = new ArrayList<Vo>();
		try {

			if (TTL <= StringConstants.TTL) {
				// checking locally
				System.out.println("checking localy");
				if (sf1.checkFileLocaly(file_name) == true) {

					returnUrl.add(new Vo(StringConstants.servername,
							StringConstants.ip, StringConstants.port));

					TTL++;// i need to incriment the time to live
				}
				if (sf1.check_configFile() == true) {
					System.out.println("checking with the peers");
					ArrayList<Vo> tempList = sf1.getPeerList();
					for (Vo vo : tempList) {

						ArrayList<Vo> tempgetBack = sf1.checkPeers(
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

	@Override
	public TransferVo obtain(String Filename) throws RemoteException,
			FileNotFoundException {
		System.out.println("inside the obtain file");

		File inputFile = new File(StringConstants.MasterDir + "/" + Filename);
		InputStream inStream = new BufferedInputStream(new FileInputStream(
				inputFile));
		byte[] bucket = new byte[(int) inputFile.length()];

		int bytesRead = 0;
		while (bytesRead > -1) {
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
		// you are getting the masters last modified date and adding version and
		// sending it to client where it wil down load
		// and save it in
		SingletonRecords slr = SingletonRecords.getInstance();

		FileVO fvo = slr.mastermap.get(Filename);
		TransferVo tvo = new TransferVo();
		tvo.setSendFile(bucket);
		tvo.setFvo(fvo);
		return tvo;

	}

	@Override
	public void invalidation(String filenameIN, FileVO fvo)
			throws FileNotFoundException, IOException, NotBoundException {
		
		SingletonRecords srs= SingletonRecords.getInstance();
		SupportFunc1 sf1 = new SupportFunc1();

		

			if (srs.downloadmap.size()!=0) {
				if(srs.downloadmap.containsKey(filenameIN)==true){
				if(srs.downloadmap.get(filenameIN).getModifiedDate()!=fvo.getModifiedDate())
					fvo.setInvalidate(true);
					srs.downloadmap.put(filenameIN, fvo);
				System.out.println("file"+filenameIN+"is invalidated");
							}
				}
		
				if (sf1.check_configFile() == true) {
					System.out.println("checking with the peers");
					ArrayList<Vo> tempList = sf1.getPeerList();
					for (Vo vo : tempList) {

						 sf1.checkPeersFiles(
								vo.getIndexName(), vo.getPeerServer_IP(),
								vo.getPeerServer_port(),filenameIN,fvo);
					}

				}

		

		
	}

	
}
