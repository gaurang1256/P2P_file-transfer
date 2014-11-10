package com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vo.FileVO;
import com.vo.TransferVo;
import com.vo.Vo;

public interface Com_InterFace extends Remote {

	public String testRMI() throws RemoteException;

	public ArrayList<Vo> query(String message_ID, int TTL, String file_name)
			throws RemoteException, IOException, NotBoundException;

	public TransferVo obtain(String Filename) throws RemoteException,
			FileNotFoundException;

	public void invalidation(String filenameIN, FileVO fvo)
			throws RemoteException, FileNotFoundException, IOException, NotBoundException;

}