package com.gyt.managementservice.dataAccess.abstracts;

import com.gyt.managementservice.entities.concretes.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.authorities r WHERE u.id = :userId AND r.name = :roleName")
    Optional<User> findByIdAndRoleName(@Param("userId") Long userId, @Param("roleName") String roleName);
}