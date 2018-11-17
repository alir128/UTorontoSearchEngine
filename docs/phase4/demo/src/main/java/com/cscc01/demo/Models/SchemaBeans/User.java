package com.cscc01.demo.Models.SchemaBeans;


import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
public class User {

	@Id
    private String id;

	@Indexed(unique = true)
    private String email;
    private String password;
    private String fullname;

    @Indexed(unique = true)
    private String username;
    private String FirstName;
    private String LastName;

    private String type; // instructor or student

    private Set<String> groupIds = new HashSet<>();

    private Set<String> fileIds = new HashSet<>();

	private boolean enabled;
	@DBRef
	private Set<Role> roles;

	public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(Set<String> groupIds) {
		this.groupIds = groupIds;
	}

	public Set<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(Set<String> fileIds) {
		this.fileIds = fileIds;
	}
}
