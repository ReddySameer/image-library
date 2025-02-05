package com.synchrony.image.library.service;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.image.library.imgur.response.domain.ImgurImageUploadResponse;

@Service
public class ImgurService {
	
	public ImgurImageUploadResponse uploadImage(String imageName, MultipartFile multipartFile) throws IOException {
		final String url = "https://api.imgur.com/3/image";
		
		RestTemplate restTemplate = new RestTemplate();
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("image", new ByteArrayResource(multipartFile.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("type", "file");
		headers.add("title", imageName);
		headers.add("description", "some file uploaded");
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String,Object>>(parts, headers);
		ResponseEntity<ImgurImageUploadResponse> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				requestEntity,
				ImgurImageUploadResponse.class
				);
		if(response.getStatusCode().equals(HttpStatus.OK))
			System.out.println(response.getBody());
		else {
			System.out.println(response.getStatusCode());
		}
		return response.getBody();
	}
	
	/*
	 * @Bean protected OAuth2ProtectedResourceDetails resource() {
	 * 
	 * ResourceOwnerPasswordResourceDetails resource = new
	 * ResourceOwnerPasswordResourceDetails();
	 * 
	 * resource.setAccessTokenUri(tokenUrl); resource.setClientId(clientId);
	 * resource.setClientSecret(clientSecret);
	 * resource.setClientAuthenticationScheme(AuthenticationScheme.form);
	 * resource.setUsername(username); resource.setPassword(password +
	 * passwordToken);
	 * 
	 * return resource; }
	 * 
	 * @Bean public OAuth2RestOperations restTemplate() { return new
	 * OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(new
	 * DefaultAccessTokenRequest())); } }
	 */
	
}
