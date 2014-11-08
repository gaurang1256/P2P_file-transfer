package com.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.Com_InterFace;
import com.constants.StringConstants;

public class Serve implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("testing1:you are there in the serve");
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
		try {
			Com_InterFace helloObj1 = new GetInfo();

			/*
			 * HelloInterface stub = (HelloInterface) UnicastRemoteObject
			 * .exportObject(helloObj1,5439);
			 */
			Registry registry = LocateRegistry
					.createRegistry(StringConstants.port);
			registry.rebind(StringConstants.servername, helloObj1);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
