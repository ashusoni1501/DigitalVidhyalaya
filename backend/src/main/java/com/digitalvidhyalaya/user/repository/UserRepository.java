package com.digitalvidhyalaya.user.repository;

import com.digitalvidhyalaya.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);
}
