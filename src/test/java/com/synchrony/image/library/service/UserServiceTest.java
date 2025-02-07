package com.synchrony.image.library.service;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchrony.image.library.domain.User;
import com.synchrony.image.library.domain.UserImage;
import com.synchrony.image.library.imgur.response.domain.Data;
import com.synchrony.image.library.imgur.response.domain.ImgurImageUploadResponse;
import com.synchrony.image.library.kinesis.KinesisProducer;
import com.synchrony.image.library.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	
	@MockitoBean
	private UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@MockitoBean
	ImgurService imgurService;
	
	@MockitoBean
	KinesisProducer kinesisProducer;
	
	@Test
	public void addUserTest() {
		User user = createTestUser();
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User receivedUser = userService.addUser(user);
		Assert.notNull(receivedUser, "User successfully added");
	}
	
	@Test
	public void getUserTest() {
		User user = createTestUser();
		Mockito.when(userRepository.findById(Long.getLong("1"))).thenReturn(Optional.of(user));
		User receivedUser = userService.getUser(Long.getLong("1"));
		Assert.notNull(receivedUser, "User successfully received");
	}
	
	@Test
	public void findUserByUserNameTest() {
		User user = createTestUser();
		Mockito.when(userRepository.findByUserName("userName")).thenReturn(user);
		User receivedUser = userService.findUserByUserName("userName");
		Assert.notNull(receivedUser, "User successfully received");
	}
	
	@Test
	public void addImageTest() throws IOException {
		User user = createTestUser();
		MultipartFile multipartFile = createMultiPartFile();
		ImgurImageUploadResponse imgurResponse = createImgurImageUploadResponse();
		Mockito.when(imgurService.uploadImage(any(String.class),  any(MultipartFile.class))).thenReturn(imgurResponse);
		Mockito.when(kinesisProducer.sendData(any(String.class), any(String.class))).thenReturn(createSamplePutRecordResult());
		boolean hasUploadedImage = userService.addImage(user, multipartFile, "someImageName");
		Assert.isTrue(hasUploadedImage, "has successfully uploaded Image");
		
	}
	
	@Test
	public void deleteImageTest() {
		Mockito.doNothing().when(imgurService).deleteImage(any(String.class));
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
	
	private MultipartFile createMultiPartFile() {
		byte[] content = "Hello World".getBytes();
		String fileName = "test.txt";
		String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;
		MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, contentType, content);
		return multipartFile;
	}
	
	private ImgurImageUploadResponse createImgurImageUploadResponse() {
		ImgurImageUploadResponse response = new ImgurImageUploadResponse();
		Data data = new Data();
		data.setDeletehash("deleteHash");
		response.setData(data);
		return response;
	}
	
	private PutRecordResult createSamplePutRecordResult() throws StreamReadException, DatabindException, IOException {
		/*
		String jsonString = "{\"sdkResponseMetadata\":{\"requestId\":\"fc0ed4af-519f-54ce-9c2e-99d5d777a107\"},\"sdkHttpMetadata\":{\"httpHeaders\":{\"connection\":\"keep-alive\",\"Content-Length\":\"104\",\"Content-Type\":\"application/x-amz-cbor-1.1\",\"Date\":\"Fri, 07 Feb 2025 18:04:42 GMT\",\"x-amz-id-2\":\"Db4neYzshunQvsMjQSsgLXQSGNROQKI2HjaMSEKyMuweGJKnZoPIvqCV2tMUA/bNSVprOynPdryUYWuWaAWKPQhQKzK4LpIA\",\"x-amzn-RequestId\":\"fc0ed4af-519f-54ce-9c2e-99d5d777a107\"},\"httpStatusCode\":200,\"allHttpHeaders\":{\"x-amzn-RequestId\":[\"fc0ed4af-519f-54ce-9c2e-99d5d777a107\"],\"connection\":[\"keep-alive\"],\"Content-Length\":[\"104\"],\"x-amz-id-2\":[\"Db4neYzshunQvsMjQSsgLXQSGNROQKI2HjaMSEKyMuweGJKnZoPIvqCV2tMUA/bNSVprOynPdryUYWuWaAWKPQhQKzK4LpIA\"],\"Date\":[\"Fri, 07 Feb 2025 18:04:42 GMT\"],\"Content-Type\":[\"application/x-amz-cbor-1.1\"]}},\"shardId\":\"shardId-000000000000\",\"sequenceNumber\":\"49660335912114062766816373852417149354372969880440799234\",\"encryptionType\":null}\r\n"
				+ "";
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString.getBytes(), PutRecordResult.class);
		*/
		return new PutRecordResult();
		
	}
}
