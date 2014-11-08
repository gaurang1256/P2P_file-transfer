package com.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import com.constants.StringConstants;
import com.server.GetInfo;
import com.vo.Vo;

public class Client implements Runnable {

	public static int message_id = 0;

	@Override
	public void run() {

		Scanner scan = new Scanner(System.in);

		// you are using this while loop till all the vaues are set

		while (!(StringConstants.ip != null
				&& StringConstants.ip_config != null
				&& StringConstants.localDir != null
				&& StringConstants.servername != null && StringConstants.port != 0)) {
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
							gfo.getFileData(filename, getVO);

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
