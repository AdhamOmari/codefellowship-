package com.example.codefellowship.controllers;

import com.example.codefellowship.models.ApplicationUser;
import com.example.codefellowship.models.Post;
import com.example.codefellowship.repo.PostRepository;
import com.example.codefellowship.repo.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller

public class ApplicationUserController {
    @Autowired
    UserRep applicationUserRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup.html";
    }

//    @GetMapping("/login")
//    public String getSignInPage() {
//        return "signin.html";
//    }

    @PostMapping("/signup")
    public RedirectView signUp(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName") String lastName, @RequestParam(value = "dateOfBirth") String dateOfBirth, @RequestParam(value = "bio") String bio) {
        ApplicationUser newUser = new ApplicationUser(username, bCryptPasswordEncoder.encode(password), firstName, lastName, dateOfBirth, bio);
        applicationUserRepository.save(newUser);
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String getSignInPage(@AuthenticationPrincipal ApplicationUser user, Model model) {
        if (user != null)
            model.addAttribute("username", applicationUserRepository.findByUsername(user.getUsername()).getUsername());
        return "signin.html";
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal ApplicationUser user, Model model) {
        if (user != null) {
            ApplicationUser currentUser = applicationUserRepository.findByUsername(user.getUsername());
            model.addAttribute("user", currentUser.getId());
        }
        return "home.html";
    }

    //    @GetMapping("/profile")
//    public String profile(@RequestParam Integer id, Model model) {
//        Optional<ApplicationUser> user = applicationUserRepository.findById(id);
//        model.addAttribute("username", user.get().getUsername());
//        model.addAttribute("firstName", user.get().getFirstName());
//        model.addAttribute("lastName", user.get().getLastName());
//        model.addAttribute("dateOfBirth", user.get().getDateOfBirth());
//        model.addAttribute("bio", user.get().getBio());
//        return "profile.html";
//    }
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal ApplicationUser user, Model model) {
        if (user != null) {
            Optional<ApplicationUser> currentUser = Optional.ofNullable(applicationUserRepository.findByUsername(user.getUsername()));
            model.addAttribute("userId", currentUser.get().getId());
            model.addAttribute("username", currentUser.get().getUsername());
            model.addAttribute("firstName", currentUser.get().getFirstName());
            model.addAttribute("lastName", currentUser.get().getLastName());
            model.addAttribute("dateOfBirth", currentUser.get().getDateOfBirth());
            model.addAttribute("bio", currentUser.get().getBio());

            List<Post> postList = postRepository.findAllByUser(currentUser);
            model.addAttribute("postList", postList);
        }
        return "profile";
    }

    @Autowired
    PostRepository postRepository;

    @PostMapping("/addpost")
    public RedirectView addPost(@AuthenticationPrincipal ApplicationUser user, @RequestParam String body) {
        ApplicationUser currentUser = applicationUserRepository.findByUsername(user.getUsername());
        Post addNewPost = new Post(body, currentUser);
        postRepository.save(addNewPost);
        return new RedirectView("/profile");
    }


}
