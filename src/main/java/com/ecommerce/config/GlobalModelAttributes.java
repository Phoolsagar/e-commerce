package com.ecommerce.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User currentUser() {
        return userService.getLoggedInUser();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        User user = userService.getLoggedInUser();
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
}
