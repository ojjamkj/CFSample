package com.gtone.cfrestapi.vo;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class RemoteCheckInVO {
	
	private String paramString;
	
	private List<CommonsMultipartFile[]> fileList;
	private List<CommonsMultipartFile> fileList1;
	private CommonsMultipartFile[] fileList2;

	public String getParamString() {
		return paramString;
	}

	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

	public List<CommonsMultipartFile[]> getFileList() {
		return fileList;
	}

	public void setFileList(List<CommonsMultipartFile[]> fileList) {
		this.fileList = fileList;
	}

	public List<CommonsMultipartFile> getFileList1() {
		return fileList1;
	}

	public void setFileList1(List<CommonsMultipartFile> fileList1) {
		this.fileList1 = fileList1;
	}

	public CommonsMultipartFile[] getFileList2() {
		return fileList2;
	}

	public void setFileList2(CommonsMultipartFile[] fileList2) {
		this.fileList2 = fileList2;
	}
}
