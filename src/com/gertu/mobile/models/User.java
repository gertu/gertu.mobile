package com.gertu.mobile.models;

public class User {
	
	private String firstName;
	private String lastName;
	private String _id=null;
	private String email;
	
	public User() {
		
	}
	
	public User(String firstName, String lastName, String _id, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this._id = _id;
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isId(){
    	if(this._id!= null)
    		return true;
    	else
    		return false;
    }
}
