package com.vo;

import java.io.Serializable;

public class TransferVo implements Serializable{
	// you are using this file  to transfer the data 
	private byte[] sendFile= null;
	private FileVO fvo= null;
	
	
	public byte[] getSendFile() {
		return sendFile;
	}
	public void setSendFile(byte[] sendFile) {
		this.sendFile = sendFile;
	}
	public FileVO getFvo() {
		return fvo;
	}
	public void setFvo(FileVO fvo) {
		this.fvo = fvo;
	}

}
