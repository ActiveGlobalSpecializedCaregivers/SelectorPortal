package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class DocumentFile {
	private String name;
	private String path;
	private Date lastModified;
	private String type;
	private String extension;
	private Long size;
	private String thumbnail;
	private String s3Key;

	public DocumentFile() {
		super();
	}

	public DocumentFile(String name, String path, Date lastModified, String type, String extension, Long size) {
		super();
		this.name = name;
		this.path = path;
		this.lastModified = lastModified;
		this.type = type;
		this.extension = extension;
		this.size = size;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "DocumentFile [name=" + name + ", path=" + path + ", lastModified=" + lastModified + ", type=" + type
				+ ", extension=" + extension + ", size=" + size + "]";
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getS3Key() {
		return s3Key;
	}

	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}

}
	
	