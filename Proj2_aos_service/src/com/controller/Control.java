package com.controller;

import java.util.Scanner;

import com.client.Client;
import com.constants.StringConstants;
import com.fileConsistancy.FileChecker;
import com.server.Serve;

public class Control {

	public static void main(String[] args) {
		System.out.println("controller:seting up");
		Scanner scan = new Scanner(System.in);
		boolean check = true;
		// ---------------

		while (check) {
			System.out.println("controller:enter  you master directory");
			StringConstants.MasterDir = scan.next();

			System.out.println("controller:enter  you download directory");
			StringConstants.DownloadDir = scan.next();
			
			System.out.println("controller:enter your host adddress");
			StringConstants.ip = scan.next();

			System.out.println("controller:enter you port number");
			StringConstants.port = scan.nextInt();

			System.out.println("controller:enter the config file path");
			StringConstants.ip_config = scan.next();

			System.out.println("controller:enter servername");
			StringConstants.servername = scan.next();

			if (StringConstants.ip != null && StringConstants.ip_config != null
					&& StringConstants.MasterDir != null
					&& StringConstants.servername != null
				    && StringConstants.DownloadDir != null
					&& StringConstants.port != 0) {
				check = false;

			}
			else
			{
				System.out.println("in complete input");
			}
		}
		new Thread(new FileChecker()).start();
		new Thread(new Client()).start();
		new Thread(new Serve()).start();
		

	}
}
