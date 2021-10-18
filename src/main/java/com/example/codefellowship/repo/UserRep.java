package com.example.codefellowship.repo;

import com.example.codefellowship.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRep extends JpaRepository<ApplicationUser, Integer> {
    public ApplicationUser findByUsername(String username);

}
