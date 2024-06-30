package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for TEnmo users.
 * Comes with the default methods that JPA provides.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.username FROM User u JOIN Account a ON u.id = a.user.id WHERE a.accountId = :accountId")
    String getUsernameByAccountId(@Param("accountId")Integer accountId);
}
