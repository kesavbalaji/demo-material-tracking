package com.example.demo.repository;


import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findUserByEmail(@Param("name") String email);

    @Query(value = "select * from user where name = ?1", nativeQuery = true)
    Optional<User> getUser(String name);
}
