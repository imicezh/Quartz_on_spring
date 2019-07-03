package com.neo.scheduler2.web.dict;

public enum Status {

	SUCCESS("100","success"),FAIL("200","fail"),ERROR("500","error");
	
	private String  code;
	private String  msg;
	
	private Status(String code,String msg){
		this.code  = code;
		this.msg   = msg;
	}
	
	public String code(){
		return code;
	}
	public String msg(){
		return msg;
	}
	@Override
	public String toString() {
		return this.code;
	}
}
