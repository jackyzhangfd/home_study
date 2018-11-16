package com.autobusi.home.study.util;

public enum SubjectCategory {
	//Don't change the value's order, in DB the value is represented by int
	YUWEN("yuwen"), // 0
	SHUXUE("shuxue"), // 1
	YINGYU("yingyu"), // 2
	ZONGHE("zonghe"); //3
	
	private String category;
	
	private SubjectCategory(String cate){
		this.category = cate;
	}
	
	@Override
    public String toString() {
        return this.category;
    }
}
