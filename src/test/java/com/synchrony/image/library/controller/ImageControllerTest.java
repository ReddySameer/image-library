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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		Mockito.when(userService.getUser(any(Long.class))).thenReturn(user);
		mockMvc.perform(get(END_POINT_PATH + "/1")
				.headers(headers))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.firstName").value("SomeFirstName"))
				.andDo(print());	
	}
	
	@Test
	public void getUserTest_UserNotFound() throws Exception  {
		User user = createTestUser();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		Mockito.when(userService.getUser(any(Long.class))).thenReturn(null);
		mockMvc.perform(get(END_POINT_PATH + "/1")
				.headers(headers))
				.andExpect(status().isNotFound())
				.andDo(print());	
	}
	
	@Test
	public void getUserByUserNameTest() throws Exception  {
		User user = createTestUser();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(user);
		mockMvc.perform(get(END_POINT_PATH + "/username/username")
				.headers(headers))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.firstName").value("SomeFirstName"))
				.andDo(print());	
	}
	
	@Test
	public void getUserByUserNameTest_UserNotFound() throws Exception  {
		User user = createTestUser();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		Mockito.when(userService.findUserByUserName(any(String.class))).thenReturn(null);
		mockMvc.perform(get(END_POINT_PATH + "/username/username")
				.headers(headers))
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
		headers.add(HttpHeaders.AUTHORIZATION, "BEARER " + accessToken());
		
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
	
	private String accessToken() {

		return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im16N0ktbUxTQ181UlAyMV83QzJmbSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rbXZqcGFxYTJudmYwZHlxLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJvSjRsNjJ6Q2lheGxNV2ZFTFl3dVkwNjlmSmhONkpXVkBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9pbWFnZS1saWJyYXJ5IiwiaWF0IjoxNzM5MDQ1Nzc4LCJleHAiOjE3MzkxMzIxNzgsInNjb3BlIjoiZ2V0IGFkZC11c2VyLWltYWdlcyIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6Im9KNGw2MnpDaWF4bE1XZkVMWXd1WTA2OWZKaE42SldWIn0.Awr6W2tBXy_Jej9wsdUojhQwQKlMsNSvwdYsBBehZsc6N-ZiDpXbyVT7MCdKKVwnYOvGMn8QheG3-wnGL7QR-1wE87CMkuP28W04v76Fo0nhTSqkrEt2Q6m36MURWK5RTDLMsu4xphCusRTz6AS94MBzMLgAhFyvEsIn9664YXiWFQjNr2hpqOFSgbLECX_zaYiU-Bb5lfTIuPu03fiWQpw2zstdvc9Yi1ao94xxW-CkdkFsfl6BZ1q3cNmUNIJgNpd0RrdPDuWhDimMEbahA-lZBlicVLHl7SbuZuZoEt39EVJkzdMl8YxYPR9txtMa31iJm0brb5Fh1xoB-YR11w";
		//return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im16N0ktbUxTQ181UlAyMV83QzJmbSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rbXZqcGFxYTJudmYwZHlxLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJvSjRsNjJ6Q2lheGxNV2ZFTFl3dVkwNjlmSmhONkpXVkBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9pbWFnZS1saWJyYXJ5IiwiaWF0IjoxNzM5MDM0ODY0LCJleHAiOjE3MzkxMjEyNjQsInNjb3BlIjoiZ2V0IGFkZC11c2VyLWltYWdlcyIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6Im9KNGw2MnpDaWF4bE1XZkVMWXd1WTA2OWZKaE42SldWIn0.R0mev4aSjkGdPAxedpf7erKszsAlNlOEwxHsdDgtuNcB1o9Dtt_ZyDgbx4lIjfY4rfxXOvfPf6_XW2bLxDE5B8wQMKFAJJh83vTgS-W3k7_hu76-JvQSsXexOzkcwjZty1tu8Oqsrr56L8ghsMFyKJqqz6pV2yy4EWrT62Y4AByIxNqVnujNt8O39H0ioEW6aiI8a9vMgH_kyjIFvX-hRkC9-qK09_GA265b9QAe3tCjyqbRmycbZG-s9O4dRP94kOeO9KW2e0aqaRzIHMCObiE2uXFikxvltYWfr1z-aty4MbGDJcNCJSSAOMT2O2ps_9pbG1In7uXZQCn5rwtpVA";
		
		
	}
}
