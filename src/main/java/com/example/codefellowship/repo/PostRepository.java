package com.example.codefellowship.repo;

import com.example.codefellowship.models.ApplicationUser;
import com.example.codefellowship.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(Optional<ApplicationUser> applicationUser);
}
