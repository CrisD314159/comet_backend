package com.crisd.comet.repositories;

import com.crisd.comet.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);

    boolean existsUserByEmail(String email);

    Page<User> findByNameIsContainingIgnoreCase(String name, Pageable pageable);
}
