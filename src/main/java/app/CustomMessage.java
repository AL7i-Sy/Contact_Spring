package app;

import java.io.Serializable;



public class CustomMessage implements Serializable{

	

	String firstname;

	String lastname;

	String phone;

	String email;

	String address;

	String source;
	
	
	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	@Override
	public String toString() {

		return  "Name: " + this.firstname + " " + this.lastname + "," + 
		"Phone: "+ this.phone + "," + 
				"Email: " + this.email + "," + 
		"Address: " + this.address +","+
				"Source:" + this.source;

		
	}

}
