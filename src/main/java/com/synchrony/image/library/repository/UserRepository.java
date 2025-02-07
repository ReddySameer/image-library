package com.synchrony.image.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.image.library.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByUserName(String userName);
}
