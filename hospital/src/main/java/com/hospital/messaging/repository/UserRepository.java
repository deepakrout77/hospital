package com.hospital.messaging.repository;

import com.hospital.messaging.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
