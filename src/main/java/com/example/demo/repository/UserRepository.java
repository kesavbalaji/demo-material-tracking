package com.example.demo.repository;


import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.username = :name")
    User findUserByEmail(@Param("name") String email);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<User> getUser(String name);

    @Modifying
    @Query("UPDATE User SET username = ?1, password = ?2, accessRights = ?3 where id = ?4 ")
    int updateUserModifying(String username, String Password, String accessRights, Integer id);

    @Modifying
    @Query("DELETE FROM User where id = ?1")
    int deleteUser(Integer id);

}
