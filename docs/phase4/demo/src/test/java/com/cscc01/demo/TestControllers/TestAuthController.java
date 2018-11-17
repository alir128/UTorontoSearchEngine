package com.cscc01.demo.TestControllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import com.cscc01.demo.Controllers.AuthController;
import com.cscc01.demo.Models.MongoUser.MongoUserDetailsService;
import com.cscc01.demo.Models.Repositories.GroupRepository;
import com.cscc01.demo.Models.Repositories.UserRepository;
import com.cscc01.demo.Models.SchemaBeans.User;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class TestAuthController extends TestCase {
	
	@Mock
	private MongoUserDetailsService mockUS;
	
	@Mock
	private BCryptPasswordEncoder mockCPE;
	
	@Mock
	private GroupRepository mockGR;
	
	@Mock
	private UserRepository mockUR;
	
	@InjectMocks
	private AuthController ac = new AuthController();
	
	@Test
	public void test_createNewUser_newUserDoesntExistInDatabase() {
		User mockUser = Mockito.mock(User.class);
		Mockito.when(mockUser.getEmail()).thenReturn("some email");
		Mockito.when(mockUS.findUserByEmail(Mockito.anyString())).thenReturn(null);
		BindingResult mockBR = Mockito.mock(BindingResult.class);
		
		ac.createNewUser(mockUser, mockBR);
		
		Mockito.verify(mockUS).saveUser(Mockito.any(User.class));
		Mockito.verify(mockUser, Mockito.times(2)).getId();
		Mockito.verify(mockUser, Mockito.times(2)).getEmail();
	}
	
	@Test
	public void test_recieveVerify_validUser() throws IOException {
		HttpServletResponse mockHSP = Mockito.mock(HttpServletResponse.class);
		User mockUser = Mockito.mock(User.class);
		Mockito.when(mockUS.findUserById(Mockito.anyString())).thenReturn(mockUser);
		ac.recieveVerify("some string", mockHSP);
		Mockito.verify(mockUR).save(Mockito.any(User.class));
	}
	
	@Test
	public void test_passwordResetPost_validUser() throws Exception {
		HttpServletResponse mockHSP = Mockito.mock(HttpServletResponse.class);
		User mockUser = Mockito.mock(User.class);
		mockUser.setEnabled(false);
		Mockito.when(mockUS.findUserById(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(mockCPE.encode(Mockito.any())).thenReturn("hi");
		
		ac.passwordresetpost(mockUser, "some string", null, mockHSP);
		
		Mockito.verify(mockUR).save(Mockito.any(User.class));
	}
	
	@Test
	public void test_resetPassword_withValidUser() throws Exception {
		User mockUser = Mockito.mock(User.class);
		HttpServletResponse mockHSP = Mockito.mock(HttpServletResponse.class);
		
		Mockito.when(mockUS.findUserByEmail(Mockito.any())).thenReturn(mockUser);
		
		ac.resetPassword(mockUser, mockHSP);
		assertEquals(mockUser.isEnabled(), false);
		Mockito.verify(mockUR).save(Mockito.any(User.class));
	}
}
