package com.synchrony.image.library.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchrony.image.library.domain.User;
import com.synchrony.image.library.domain.UserImage;
import com.synchrony.image.library.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ImageControllerTest {
	
	private static final String END_POINT_PATH = "/image/user";

	@Autowired
	private MockMvc mockMvc;
	
	
	@MockitoBean
	private UserService userService;
		
	@Autowired
	ObjectMapper objectMapper;

	
	@Test
	public void addUserTest() throws Exception {
		
		User user = createTestUser();
		Mockito.when(userService.addUser(any(User.class))).thenReturn(user);	
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "Password");
		headers.add("description", "some file uploaded");
		
		String requestBody = objectMapper.writeValueAsString(user);
		mockMvc.perform(post(END_POINT_PATH)
				.headers(headers)
				.requestAttr(requestBody, requestBody)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void addUserTest_WithInternalServerError() throws Exception {
		
		User user = createTestUser();
		Mockito.when(userService.addUser(any(User.class))).thenReturn(null);	
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "Password");
		headers.add("description", "some file uploaded");
		
		String requestBody = objectMapper.writeValueAsString(user);
		mockMvc.perform(post(END_POINT_PATH)
				.headers(headers)
				.requestAttr(requestBody, requestBody)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody))
				.andExpect(status().isInternalServerError())
				.andDo(print());
	}
	
	@Test
	public void getUserTest() throws Exception  {
		User user = createTestUser();
		Mockito.when(userService.getUser(any(Long.class))).thenReturn(user);
		mockMvc.perform(get(END_POINT_PATH + "/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.firstName").value("SomeFirstName"))
				.andDo(print());	
	}
	
	@Test
	public void getUserTest_UserNotFound() throws Exception  {
		User user = createTestUser();
		Mockito.when(userService.getUser(any(Long.class))).thenReturn(null);
		mockMvc.perform(get(END_POINT_PATH + "/1"))
				.andExpect(status().isNotFound())
				.andDo(print());	
	}
	
	@Test
	public void getUserByUserNameTest() throws Exception  {
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		mockMvc.perform(get(END_POINT_PATH + "/username/username"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.firstName").value("SomeFirstName"))
				.andDo(print());	
	}
	
	@Test
	public void getUserByUserNameTest_UserNotFound() throws Exception  {
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(null);
		mockMvc.perform(get(END_POINT_PATH + "/username/username"))
				.andExpect(status().isNotFound())
				.andDo(print());	
	}
	
	@Test
	public void uploadImageTest() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "somePassword");
		headers.add("description", "some file uploaded");
		
        byte[] content = "Hello World".getBytes();
        String fileName = "test.txt";
        String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

        MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);
        
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		Mockito.when(userService.addImage(any(User.class), any(MultipartFile.class), any(String.class))).thenReturn(true);
		
		mockMvc.perform(multipart(END_POINT_PATH+"/image")			
				.file(multipartFile).headers(headers))
				.andExpect(status().isAccepted())
				.andDo(print());		
		
	}
	
	@Test
	public void uploadImageTest_throwsIOException() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "somePassword");
		headers.add("description", "some file uploaded");
		
        byte[] content = "Hello World".getBytes();
        String fileName = "test.txt";
        String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

        MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);
        
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		Mockito.when(userService.addImage(any(User.class), any(MultipartFile.class), any(String.class))).thenThrow(IOException.class);
		
		mockMvc.perform(multipart(END_POINT_PATH+"/image")			
				.file(multipartFile).headers(headers))
				.andExpect(status().isInternalServerError())
				.andDo(print());		
		
	}	
	
	@Test
	public void uploadImageTest_badRequest() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		headers.add("UserName", "UserName");
		headers.add("Password", "Password");
		headers.add("description", "some file uploaded");
		
        byte[] content = "Hello World".getBytes();
        String fileName = "test.txt";
        String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

        MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);
        
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		Mockito.when(userService.addImage(any(User.class), any(MultipartFile.class), any(String.class))).thenReturn(true);
		
		mockMvc.perform(multipart(END_POINT_PATH+"/image")			
				.file(multipartFile).headers(headers))
				.andExpect(status().isBadRequest())
				.andDo(print());		
		
	}
	
	@Test
	public void uploadImageTest_badRequest_UserCouldNotBeFound() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "Password");
		headers.add("description", "some file uploaded");
		
        byte[] content = "Hello World".getBytes();
        String fileName = "test.txt";
        String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

        MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);
        
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(null);
		Mockito.when(userService.addImage(any(User.class), any(MultipartFile.class), any(String.class))).thenReturn(true);
		
		mockMvc.perform(multipart(END_POINT_PATH+"/image")			
				.file(multipartFile).headers(headers))
				.andExpect(status().isBadRequest())
				.andDo(print());		
		
	}
	
	@Test
	public void uploadImageTest_badRequest_InvalidPassword() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("ImageName", "ImageName");
		headers.add("UserName", "UserName");
		headers.add("Password", "Password");
		headers.add("description", "some file uploaded");
		
        byte[] content = "Hello World".getBytes();
        String fileName = "test.txt";
        String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

        MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);
        
		User user = createTestUser();
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		Mockito.when(userService.addImage(any(User.class), any(MultipartFile.class), any(String.class))).thenReturn(true);
		
		mockMvc.perform(multipart(END_POINT_PATH+"/image")			
				.file(multipartFile).headers(headers))
				.andExpect(status().isBadRequest())
				.andDo(print());		
		
	}
	
	private User createTestUser() {
		User user = new User();
		user.setFirstName("SomeFirstName");
		user.setLastName("someLastName");
		user.setUserName("someUserName");
		user.setPassword("somePassword");
		List<UserImage> userImages = new ArrayList<>();
		UserImage userImage = new UserImage();
		userImage.setImageName("someImageName");
		userImage.setImageHash("ImageHash");		
		userImages.add(userImage);
		user.setImages(userImages);
		return user;
	}
}
