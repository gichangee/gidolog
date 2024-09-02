package com.gildong.api.repository;

import com.gildong.api.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
