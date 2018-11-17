package com.CSCC01.Project.Backend.Model.MongoUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.CSCC01.Project.Backend.Model.SchemaBeans.User;
import com.CSCC01.Project.Backend.Model.SchemaBeans.Group;

public class MongoUserDetails implements UserDetails {
	User user;
	
	public MongoUserDetails(User user){
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		for (Group group : user.getGroups()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(group.name());
			authList.add(auth);
		}
		
		return authList;
	}

	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
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
		return true;
	}

}
