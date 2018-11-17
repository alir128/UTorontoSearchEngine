package com.cscc01.demo.TestMongoUser;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.MongoUser.MongoUserDetailsService;
import com.cscc01.demo.Models.Repositories.RoleRepository;
import com.cscc01.demo.Models.Repositories.UserRepository;
import com.cscc01.demo.Models.SchemaBeans.Role;
import com.cscc01.demo.Models.SchemaBeans.User;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class TestMongoUserDetailsService extends TestCase {
	
	@Mock
	private UserRepository mockUR;
	@Mock
	private RoleRepository mockRR;
	@Mock
	private BCryptPasswordEncoder mockCPE;
	@InjectMocks
	private MongoUserDetailsService mguds = new MongoUserDetailsService();
	
	@Test
	public void test_saveUser_nonNullUser() {
		Role role = new Role();
		Mockito.when(mockCPE.encode(Mockito.anyString())).thenReturn("password");
		Mockito.when(mockRR.findByRole(Mockito.anyString())).thenReturn(role);
		
		User user = new User();
		user.setPassword("password");
		
		mguds.saveUser(user);
		
		Mockito.verify(mockUR).save(Mockito.any(User.class));
		assertEquals(user.isEnabled(), false);
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void test_loadByUsername_userNotFoundInDatabase() {
		Mockito.when(mockUR.findByEmail(Mockito.anyString())).thenReturn(null);
		
		mguds.loadUserByUsername("some email");
	}
	
	@Test
	public void test_loadByUsername_validEmailInDatabase() {
		User user = new User();
		user.setUsername("username");
		user.setEmail("email");
		user.setUsername("username");
		user.setPassword("password");
		user.setEnabled(false);
		user.setFirstName("firstName");
		user.setFullname("fullName");
		user.setLastName("lastName");
		user.setType("type");
		user.setId("id");
		
		Role role = new Role();
		role.setRole("ADMIN");
		Set<Role> someSet = new HashSet<>();
		someSet.add(role);
		
		user.setRoles(someSet);
		
		Mockito.when(mockUR.findByEmail(Mockito.anyString())).thenReturn(user);
		
		assertNotNull(mguds.loadUserByUsername("some email"));
		assertEquals(mguds.loadUserByUsername("some email").isEnabled(), false);
		assertEquals(((MongoUser) mguds.loadUserByUsername("some email")).getFirstName(), "firstName");
		assertEquals(((MongoUser) mguds.loadUserByUsername("some email")).getLastName(), "lastName");
		assertEquals(mguds.loadUserByUsername("some email").getPassword(), "password");
		assertEquals(((MongoUser) mguds.loadUserByUsername("some email")).getId(), "id");
		assertEquals(((MongoUser) mguds.loadUserByUsername("some email")).getType(), "type");
	}
}
