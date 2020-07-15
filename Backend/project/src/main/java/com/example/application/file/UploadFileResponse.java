package com.example.application.file;

/*
 * This class is used for uploading the file response with file name, the download URI, filetype, and size
 */
public class UploadFileResponse {
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private long size;

	/*
	 * set file name, URI, file type, size
	 */
	public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	/*
	 * returns file name
	 */
	public String getFileName() {
		return fileName;
	}

	/*
	 *sets filename
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * returns file URI
	 */
	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	/*
	 * sets URI
	 */
	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	/*
	 * returns fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/*
	 * sets filetype
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/*
	 * return size of file
	 */
	public long getSize() {
		return size;
	}

	/*
	 * sets size of file
	 */
	public void setSize(long size) {
		this.size = size;
	}

}
