package com.constants;

import java.util.HashMap;

import com.vo.FileVO;

public class SingletonRecords {

	public static SingletonRecords sr = null;

	public HashMap<String, FileVO> mastermap = null;
	public HashMap<String, FileVO> downloadmap = null;
	
	
	private SingletonRecords() {
		
		mastermap = new HashMap<String,FileVO>();
		downloadmap = new HashMap<String,FileVO>();

	}

	public static SingletonRecords getInstance() {
		if (sr == null) {
			sr = new SingletonRecords();
			return sr;
		} else {
			return sr;
		}
	}
}
