package com.synchrony.image.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.image.library.domain.UserImage;

@Repository
public interface ImageRepository extends JpaRepository<UserImage, Long> {

}
