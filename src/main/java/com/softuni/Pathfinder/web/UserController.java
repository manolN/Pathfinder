package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.binding.UserRegisterBindingModel;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.service.UserRegisterServiceModel;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserController(UserService userService, ModelMapper modelMapper,
                          UserRepository userRepository) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    // LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
                                RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "login";
    }

    // REGISTER
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("isTrue", false);

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult
                    .addError(new FieldError("userRegisterBindingModel",
                            "confirmPassword", "confirmPassword must match password"));

            bindingResult
                    .addError(new FieldError("userRegisterBindingModel",
                            "password", "password must match confirmPassword"));
        }

        UserEntity optUser = userRepository.findByUsername(userRegisterBindingModel.getUsername()).orElse(null);

        if (optUser != null) {
            bindingResult
                    .addError(new FieldError("userRegisterBindingModel",
                            "username", "Username occupied"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                    bindingResult);
            redirectAttributes
                    .addFlashAttribute("occupiedUsername", false);

            return "register";
        }

        UserRegisterServiceModel serviceModel = modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class);

        boolean registerSuccessful = userService.register(serviceModel);

        if (registerSuccessful) {
            return "redirect:/login";
        }

        redirectAttributes
                .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

        return "redirect:/register";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserInfo(principal.getName()));
        return "profile";
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }
}