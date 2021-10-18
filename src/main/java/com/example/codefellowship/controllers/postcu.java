package com.example.codefellowship.controllers;

import com.example.codefellowship.models.ApplicationUser;
import com.example.codefellowship.models.Post;
import com.example.codefellowship.repo.PostRepository;
import com.example.codefellowship.repo.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public class postcu {
    @Autowired
    UserRep applicationUserRepository;
    @Autowired
    PostRepository postRepository;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal ApplicationUser user, Model model, @RequestParam Integer id) {
        if (user != null) {
            Optional<ApplicationUser> currentUser = applicationUserRepository.findById(id);
            model.addAttribute("userId", currentUser.get().getId());
            model.addAttribute("username", currentUser.get().getUsername());
            model.addAttribute("firstName", currentUser.get().getFirstName());
            model.addAttribute("lastName", currentUser.get().getLastName());
            model.addAttribute("dateOfBirth", currentUser.get().getDateOfBirth());
            model.addAttribute("bio", currentUser.get().getBio());

            List<Post> postList = postRepository.findAllByUser(currentUser);
            model.addAttribute("postList", postList);
        }
        return "profile.html";
    }

}
