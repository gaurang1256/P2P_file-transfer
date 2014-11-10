package com.vo;

import java.io.Serializable;

public class FileVO implements Serializable{
	private long modifiedDate= 0;
	private boolean invalidate= false;
	private int version =1;
	private String indexer= null;
	
	private String hostServer_IP= null;
	private int hostServer_port=0;
	
	public long getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public boolean isInvalidate() {
		return invalidate;
	}
	public void setInvalidate(boolean invalidate) {
		this.invalidate = invalidate;
	}
	public String getHostServer_IP() {
		return hostServer_IP;
	}
	public void setHostServer_IP(String hostServer_IP) {
		this.hostServer_IP = hostServer_IP;
	}
	public int getHostServer_port() {
		return hostServer_port;
	}
	public void setHostServer_port(int hostServer_port) {
		this.hostServer_port = hostServer_port;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getIndexer() {
		return indexer;
	}
	public void setIndexer(String indexer) {
		this.indexer = indexer;
	}
		public FileVO(long modifiedDate, boolean invalidate, int version, String indexer) {
		super();
		this.modifiedDate = modifiedDate;
		this.invalidate = invalidate;
		this.version = version;
		this.indexer = indexer;
	}
	
	public FileVO(long modifiedDate, int version, String indexer) {
		super();
		this.modifiedDate = modifiedDate;
		this.version = version;
		this.indexer = indexer;
	}
	
	public FileVO(long modifiedDate, String indexer,String hostServer_IP,int hostServer_port) {
		super();
		this.modifiedDate = modifiedDate;
		this.indexer = indexer;
		this.hostServer_IP=hostServer_IP;
		this.hostServer_port=hostServer_port;
	}
	

}
