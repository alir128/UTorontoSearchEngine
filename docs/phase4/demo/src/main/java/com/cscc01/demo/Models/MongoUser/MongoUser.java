package com.cscc01.demo.Models.MongoUser;

import java.util.Collection;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class MongoUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5987974161567631890L;
	private final String FirstName;
	private final String LastName;
	private final String fullName;
	private final String type;
	private final Boolean enabled;
	private final String id;
	private final String username;
	private final String email;
	private final Set<String> groups;
	private Set<String> files;
	
	public MongoUser(String username, String password, com.cscc01.demo.Models.SchemaBeans.User user, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, String firstName, String fullName, String lastName, String type, String id) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.FirstName = firstName;
		this.LastName = lastName;
		this.fullName = fullName;
		this.type = type;
		this.enabled=enabled;
		this.id = id;
		this.username=user.getUsername();
		this.email=user.getEmail();
		this.groups=user.getGroupIds();
		this.files=user.getFileIds();
	}

	public String getFirstName() {
		return FirstName;
	}

	public String getLastName() {
		return LastName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getType() {
		return type;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getId() {
		return id;
	}
	
	public String getUsername () {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public Set<String> getFiles() {
		return files;
	}
		
	
}
