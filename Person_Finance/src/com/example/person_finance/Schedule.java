package com.example.person_finance;

public class Schedule {
	private int _id;
	private String _Date;
	private String _Cat;
	private String _SubCat;
	private String _Description;
	
	public Schedule() {
	
	}
	
	public Schedule(String Date,String _Cat,String _SubCat,String _Description){
		this._Date=Date;
		this._Cat = _Cat;
		this._SubCat = _SubCat;
		this._Description = _Description;
		
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	public void set_Date(String _Date) {
		this._Date = _Date;
	}
	public void set_Cat(String _Cat) {
		this._Cat = _Cat;
	}
	public void set_SubCat(String _SubCat) {
		this._SubCat = _SubCat;
	}
	public void set_Description(String _Description) {
		this._Description = _Description;
	}
	public int get_id() {
		return _id;
	}
	public String get_Date() {
		return _Date;
	}
	public String get_Cat() {
		return _Cat;
	}
	public String get_SubCat() {
		return _SubCat;
	}
	public String get_Description() {
		return _Description;
	}
	
}
