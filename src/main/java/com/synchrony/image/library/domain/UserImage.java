package com.synchrony.image.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_image")
public class UserImage {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="image_Id")
	Long imageId;
		
	@ManyToOne
	@JoinColumn(name="id", nullable=false)
	User user;
	
	@Column
	String imageName;
	
	@Column
	String imageHash;


	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageHash() {
		return imageHash;
	}

	public void setImageHash(String imageHash) {
		this.imageHash = imageHash;
	}
	
	
}
