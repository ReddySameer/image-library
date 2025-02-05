package com.synchrony.image.library.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.image.library.domain.User;
import com.synchrony.image.library.domain.UserImage;
import com.synchrony.image.library.imgur.response.domain.ImgurImageUploadResponse;
import com.synchrony.image.library.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ImgurService imgurService;
	
	public User addUser(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	
	public User getUser(Long id) {
		Optional<User> optUser = userRepository.findById(id);
		return optUser.isPresent()? optUser.get(): null;
	}
	
	public User findUserByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		return user;
	}
	public ImgurImageUploadResponse addImage(User user, MultipartFile imagefile, String imageName) throws IOException {
		if(user.getImages() == null) {
			user.setImages(new HashSet<UserImage>());
		}
		UserImage userImage = new UserImage();
		userImage.setUser(user);
		userImage.setImage(imageName);
		user.getImages().add(userImage);
		userRepository.save(user);
		ImgurImageUploadResponse imgurResponse = imgurService.uploadImage(imageName, imagefile);
		return imgurResponse;
	}
}
