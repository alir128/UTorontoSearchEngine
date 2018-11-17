package com.cscc01.demo.TestMongoUser;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.SchemaBeans.User;

import junit.framework.TestCase;

public class TestMongoUser extends TestCase {
	
	private MongoUser mu;
	
	public TestMongoUser() {
		User user = new User();
		user.setUsername("username");
		user.setEmail("email");
		String username = "username";
		String password = "password";
		boolean enabled = false;
		boolean isAccountExpired = false;
		boolean isCredentialsExpired = false;
		boolean isAccountLocked = false;
		String firstName = "firstName";
		String fullName = "fullName";
		String lastName = "lastName";
		String type = "type";
		String id = "id";
		Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
		mu = new MongoUser(username, password, user, enabled, isAccountExpired, isCredentialsExpired, 
				isAccountLocked, authorities, firstName, fullName, lastName, type, id);
	}
	
	@Test
	public void test_setValuesFromConstructor() {
		assertEquals(mu.isEnabled(), false);
		assertEquals(mu.getFirstName(), "firstName");
		assertEquals(mu.getLastName(), "lastName");
		assertEquals(mu.getPassword(), "password");
		assertEquals(mu.getId(), "id");
		assertEquals(mu.getType(), "type");
	}
}
