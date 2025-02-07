package com.synchrony.image.library.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchrony.image.library.imgur.response.domain.ImgurImageUploadResponse;

@SpringBootTest
public class ImgurServiceTest {

	@Autowired
	private ImgurService imgurService;

	final String url = "https://api.imgur.com/3/image";

	@Test
	void uploadImageTest() throws RestClientException, URISyntaxException {
		try {
			RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
			ResponseEntity<ImgurImageUploadResponse> responseEntity = ResponseEntity.ok(createResponseObject());

			byte[] content = "Hello World".getBytes();
			String fileName = "test.txt";
			String contentType = MediaType.MULTIPART_FORM_DATA_VALUE;

			MockMultipartFile multipartFile = new MockMultipartFile("image", fileName, contentType, content);

			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
			parts.add("image", new ByteArrayResource(multipartFile.getBytes()));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("type", "file");
			headers.add("title", "imageName");
			headers.add("description", "some file uploaded");

			Mockito.when(restTemplateMock.exchange(Mockito.eq(new URI(url)), Mockito.eq(HttpMethod.POST),
					any(HttpEntity.class), Mockito.eq(ImgurImageUploadResponse.class))).thenReturn(responseEntity);

			ImgurImageUploadResponse imgurImageUploadResponse = imgurService.uploadImage("somefileName", multipartFile);
			Assert.notNull(imgurImageUploadResponse, "method returned the imgurImageUploadResponse");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ImgurImageUploadResponse createResponseObject() throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(sampleResponse().getBytes(), ImgurImageUploadResponse.class);
	}

	private String sampleResponse() {
		return "{\r\n" + "    \"status\": 200,\r\n" + "    \"success\": true,\r\n" + "    \"data\": {\r\n"
				+ "        \"id\": \"U6ogptD\",\r\n" + "        \"deletehash\": \"XaijYQWc5PySldn\",\r\n"
				+ "        \"account_id\": null,\r\n" + "        \"account_url\": null,\r\n"
				+ "        \"ad_type\": null,\r\n" + "        \"ad_url\": null,\r\n"
				+ "        \"title\": \"Simple upload\",\r\n"
				+ "        \"description\": \"This is a simple image upload in Imgur\",\r\n"
				+ "        \"name\": \"\",\r\n" + "        \"type\": \"image/jpeg\",\r\n"
				+ "        \"width\": 409,\r\n" + "        \"height\": 442,\r\n" + "        \"size\": 34509,\r\n"
				+ "        \"views\": 0,\r\n" + "        \"section\": null,\r\n" + "        \"vote\": null,\r\n"
				+ "        \"bandwidth\": 0,\r\n" + "        \"animated\": false,\r\n"
				+ "        \"favorite\": false,\r\n" + "        \"in_gallery\": false,\r\n"
				+ "        \"in_most_viral\": false,\r\n" + "        \"has_sound\": false,\r\n"
				+ "        \"is_ad\": false,\r\n" + "        \"nsfw\": null,\r\n"
				+ "        \"link\": \"https://i.imgur.com/U6ogptD.jpeg\",\r\n" + "        \"tags\": [],\r\n"
				+ "        \"datetime\": 1738949069,\r\n" + "        \"mp4\": \"\",\r\n" + "        \"hls\": \"\"\r\n"
				+ "    }";
	}
}
