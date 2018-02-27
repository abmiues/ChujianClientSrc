package com.abmiues.chujian.pojo;

public class Seller {
private String sellerid;
private String name;
private String tel;
private String permitid;
private int comment;
private int exp;
private int state;
private String pwd;
public String getPermitid() {
	return permitid;
}
public void setPermitid(String permitid) {
	this.permitid = permitid;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
public String getSellerid() {
	return sellerid;
}
public void setSellerid(String sellerid) {
	this.sellerid = sellerid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getTel() {
	return tel;
}
public void setTel(String tel) {
	this.tel = tel;
}

public int getComment() {
	return comment;
}
public void setComment(int comment) {
	this.comment = comment;
}
public int getExp() {
	return exp;
}
public void setExp(int exp) {
	this.exp = exp;
}
public int getState() {
	return state;
}
public void setState(int state) {
	this.state = state;
}
}
