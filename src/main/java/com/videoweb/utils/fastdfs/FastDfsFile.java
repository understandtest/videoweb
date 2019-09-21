package com.videoweb.utils.fastdfs;

import org.csource.common.NameValuePair;


public class FastDfsFile {
	private String groupName;
	private String fileName;
	private NameValuePair[] nameValuePairs;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public NameValuePair[] getNameValuePairs() {
		return nameValuePairs;
	}

	public void setNameValuePairs(NameValuePair[] nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}
}
