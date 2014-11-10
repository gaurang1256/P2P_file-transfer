package com.vo;

import java.io.Serializable;

public class Vo implements Serializable {
	private String indexName= null;
	private String peerServer_IP= null;
	private int peerServer_port=0;
	
	@Override
	public String toString() {
		return "Vo [indexName=" + indexName + ", peerServer_IP="
				+ peerServer_IP + ", peerServer_port=" + peerServer_port + "]";
	}
	
	public Vo(String indexName, String peerServer_IP, int peerServer_port) {
		super();
		this.indexName = indexName;
		this.peerServer_IP = peerServer_IP;
		this.peerServer_port = peerServer_port;
	}
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getPeerServer_IP() {
		return peerServer_IP;
	}
	public void setPeerServer_IP(String peerServer_IP) {
		this.peerServer_IP = peerServer_IP;
	}
	public int getPeerServer_port() {
		return peerServer_port;
	}
	public void setPeerServer_port(int peerServer_port) {
		this.peerServer_port = peerServer_port;
	} 
	
	

}
