package com.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import com.Com_InterFace;
import com.constants.SingletonRecords;
import com.constants.StringConstants;
import com.server.GetInfo;
import com.vo.TransferVo;
import com.vo.Vo;

public class Client implements Runnable {

	public static int message_id = 0;

	public void getFileData(String Filename, Vo valueObj) {
		try {
			System.out.println("you are in the get file data");
			System.out.println(valueObj.toString());

			Registry registry = LocateRegistry.getRegistry(
					valueObj.getPeerServer_IP(), valueObj.getPeerServer_port());

			Com_InterFace comp = (Com_InterFace) registry.lookup(valueObj
					.getIndexName());

			System.out.println("got input from the server");
			TransferVo tvo = comp.obtain(Filename);

			byte[] bucket = tvo.getSendFile();// setting the byte array

			// setting the file download hash table
			SingletonRecords slr = SingletonRecords.getInstance();
			slr.downloadmap.put(Filename, tvo.getFvo());

			System.out.println("got ");
			// reading from the file and writing it to the downloaded area
			BufferedOutputStream bot = new BufferedOutputStream(
					new FileOutputStream(new File(StringConstants.DownloadDir
							+ "/" + Filename)));
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

	@Override
	public void run() {

		Scanner scan = new Scanner(System.in);

		// you are using this while loop till all the vaues are set

		while (!(StringConstants.ip != null
				&& StringConstants.ip_config != null
				&& StringConstants.MasterDir != null
				&& StringConstants.servername != null
				&& StringConstants.DownloadDir != null
				&& StringConstants.port != 0 && StringConstants.initialLoad != false)) {
			// this will keep going till all the parameter are set
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (true) {
			try {
				System.out.println("Client:welcome");
				System.out
						.println("Client:enter the file that you want to search$");
				String filename = scan.next();

				GetInfo gfo = new GetInfo();
				ArrayList<Vo> getdata = gfo.query(StringConstants.ip
						+ message_id, 0, filename);

				if (getdata.size() <= 0) {
					System.out.println("no file found");

				} else {
					System.out.println("file found");
					int i = 0;
					for (Vo url : getdata) {
						System.out.println((i + 1) + ">"
								+ url.getPeerServer_IP() + ""
								+ url.getPeerServer_port());
						i++;
					}

					// ------------------code to obtain the file
					System.out.println("do you want to download the file? y/n");

					String input = scan.next();
					if (input.matches("y")) {

						System.out
								.println("from which server you want to get file from ?");

						int inputnumber = scan.nextInt();
						if ((inputnumber <= getdata.size())
								&& (inputnumber > 0)) {
							Vo getVO = getdata.get(inputnumber - 1);
							System.out
									.println("querying the server for the file");
							// writing to the file
							getFileData(filename, getVO);

						}
					}

				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
