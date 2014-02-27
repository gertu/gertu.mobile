package com.gertu.mobile.models;

public class User {
	
	private String firstName;
	private String lastName;
	private String _id=null;
	private String email;
    private String token;
    private String image;


    public User() {
		
	}
	
	public User(String firstName, String lastName, String _id, String email, String token, String image) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this._id = _id;
		this.email = email;
        this.token = token;
        this.image = image;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
	public boolean isId(){
    	if(this._id != null)
    		return true;
    	else
    		return false;
    }
}
