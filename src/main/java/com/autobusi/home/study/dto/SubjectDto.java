
package com.autobusi.home.study.dto;

import java.util.Date;

import com.autobusi.home.study.models.Subject;
import com.autobusi.home.study.util.SubjectCategory;

public class SubjectDto {
	private long id;
	private String name;
	private String detail;
	private SubjectCategory category;
	private Date lastChangedDate;
	private byte[] imageBytes;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SubjectCategory getCategory() {
		return category;
	}
	public void setCategory(SubjectCategory category) {
		this.category = category;
	}
	public Date getLastChangedDate() {
		return lastChangedDate;
	}
	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}
	public byte[] getImageBytes() {
		return imageBytes;
	}
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public void createFromSubject(Subject sub){
		if(sub == null){
			return;
		}
		this.setId(sub.getId());
		this.setName(sub.getTitle());
		this.setDetail(sub.getDetail());
		this.setCategory(sub.getCategory());
		this.setLastChangedDate(sub.getModifiedAt());
	}
}
