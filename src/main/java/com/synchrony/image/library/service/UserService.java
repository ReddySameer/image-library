package com.synchrony.image.library.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.image.library.domain.User;
import com.synchrony.image.library.domain.UserImage;
import com.synchrony.image.library.imgur.response.domain.ImgurImageUploadResponse;
import com.synchrony.image.library.kinesis.KinesisProducer;
import com.synchrony.image.library.repository.UserRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ImgurService imgurService;
	
	@Autowired
	KinesisProducer kinesisProducer;
	
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
	
	public boolean addImage(User user, MultipartFile imagefile, String imageName) throws IOException {
		if(user.getImages() == null) {
			user.setImages(new ArrayList<UserImage>());
		}
		UserImage userImage = new UserImage();
		userImage.setUser(user);
		userImage.setImageName(imageName);
		user.getImages().add(userImage);
		userRepository.save(user);
		ImgurImageUploadResponse imgurResponse = imgurService.uploadImage(imageName, imagefile);
		userImage.setImageHash(imgurResponse.getData().getDeletehash());
		kinesisProducer.sendData(imageName, user.getUserName());
		System.out.println(imgurResponse.getData().getDeletehash());
		return true;
	}
	
	public boolean deleteImage(String userName, String imageName) {
		User user = findUserByUserName(userName);
		String imageHash = null;
		 if(user != null) {
			imageHash = findImageHash(user, imageName);
			if(!StringUtils.isBlank(imageHash)) {
				deleteImage(imageHash);
				return true;
			}			
		 }
		return false;
	}
	
	public boolean deleteImage(String imageHash) {
		 imgurService.deleteImage(imageHash);
		 return true;
	}
	

	private String findImageHash(User user, String imageName) {
		Optional<UserImage> userImageOpt = user.getImages().stream()
				.filter(userImage -> userImage.getImageName().equals(imageName)).findAny();
		if (userImageOpt.isPresent()) {
			return userImageOpt.get().getImageHash();

		}
		return null;
	}
	
}
