package com.cscc01.demo.TestMongoUser;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cscc01.demo.Models.MongoUser.MongoUserDetails;
import com.cscc01.demo.Models.SchemaBeans.Role;
import com.cscc01.demo.Models.SchemaBeans.User;

import junit.framework.TestCase;

public class TestMongoUserDetails extends TestCase {
	
	private MongoUserDetails mgu;
	
	public TestMongoUserDetails() {
		Set<Role> roles = new HashSet<>();
		Role role1 = new Role();
		role1.setRole("some role");
		Role role2 = new Role();
		role2.setRole("another role");
		roles.add(role1);
		roles.add(role2);
		
		User user = new User();
		user.setUsername("username");
		user.setPassword("password");
		user.setRoles(roles);
		user.setEnabled(true);
		
		mgu = new MongoUserDetails(user);
	}
	
	@Test
	public void test_constructor_setProperly() {
		assertEquals(mgu.getUsername(), "username");
		assertEquals(mgu.getPassword(), "password");
		assertEquals(mgu.isEnabled(), true);
	}
	
	@Test
	public void test_translate_nonEmptySet() {
		assertEquals(mgu.getAuthorities().size(), 2);
	}
}
