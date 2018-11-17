package com.cscc01.demo.Models.MongoUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cscc01.demo.Models.SchemaBeans.User;
import com.cscc01.demo.Models.SchemaBeans.Role;

@SuppressWarnings("serial")
public class MongoUserDetails implements UserDetails {
	private String username;
	private String password;
	private String fullName;
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean enabled;
	
	/**
	 * 
	 */
	

	public MongoUserDetails(User user){
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.authorities = translate(user.getRoles());
		this.enabled = user.isEnabled();
	}

	
	private Collection<? extends GrantedAuthority> translate(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            String name = role.getRole().toUpperCase();
            //Make sure that all roles start with "ROLE_"
            if (!name.startsWith("ROLE_"))
                name = "ROLE_" + name;
            authorities.add(new SimpleGrantedAuthority(name));
        }
        return authorities;
    }
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
	
	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	public String getFullName() {
		return fullName;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
