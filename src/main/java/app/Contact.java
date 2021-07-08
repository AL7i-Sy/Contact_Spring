package app;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class Contact implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@NotNull
	String firstname;

	@NotNull
	String lastname;

	String phone;

	String email;

	String address;
	
	String source;

	boolean isapproved =false;
	@ManyToOne
	@JoinColumn(name = "userid")
	User user;

	@Version
	Long version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public boolean isIsapproved() {
		return isapproved;
	}

	public void setIsapproved(boolean isapproved) {
		this.isapproved = isapproved;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString()
	{
		return String.join(",", 
				"Name: "+this.firstname +" " +this.lastname,
				"Phone: "+this.phone,
				"Email: "+this.email,
				"Address: "+this.address
				);
	}



}
