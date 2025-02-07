package com.synchrony.image.library.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.image.library.domain.User;
import com.synchrony.image.library.service.UserService;

@RestController
@RequestMapping("/image")
public class ImageController {

	private static String URI_CONTEXT = "/image";
	@Autowired
	UserService userService;
	
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		User savedUser =  userService.addUser(user);
		
		if(savedUser == null) {
			return ResponseEntity.internalServerError().body("Could not create/Add User");
		}
		URI uri = URI.create(URI_CONTEXT + "/" + user.getId());
		return ResponseEntity.created(uri).body(savedUser);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		User foundUser = userService.getUser(id);
		if(foundUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with Id:" + id + "could not be found");
		}
		return ResponseEntity.ok(foundUser);
	}
	
	@GetMapping("/user/username/{userName}")
	public ResponseEntity<?> getByUserName(@PathVariable String userName) {
		User foundUser = userService.findUserByUserName(userName);
		if(foundUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with user_name:" + userName + "could not be found");
		}
		return ResponseEntity.ok(foundUser);
	}
	
	@PostMapping(value="/user/image", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImage(
			@RequestHeader("ImageName") String imageName,
			@RequestHeader("UserName") String userName,
			@RequestHeader("Password") String password,
			@RequestParam("image") MultipartFile imageFile) {
		if(!StringUtils.hasLength(imageName) ||
				!StringUtils.hasLength(password) ||
				!StringUtils.hasLength(userName) ||
				imageFile == null) {
			ResponseEntity.badRequest().body("ImageName, UserName, Password, and ImageFile are Mandatory");
		}
		User user = userService.findUserByUserName(userName);
		if(user==null) {
			return ResponseEntity.badRequest().body("User could not be found for the given userName");
		}
			
		if(user != null && !user.getPassword().equals(password)) {
			return ResponseEntity.badRequest().body("Invalid Password");
		}
		try {
			userService.addImage(user, imageFile, imageName);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body("Failed to upload the image to Imgur");
		}
		return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Image Loaded successfully");
	}
}
