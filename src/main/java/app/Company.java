package app;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Company implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String name;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private Set<User> users;

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Company() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}