package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model){
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "/users";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model){
        List<Role> roleList = userService.getAllRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile){
        System.out.println(user);
        System.out.println(multipartFile.getOriginalFilename());
//        userService.save(user);
//        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer userId, RedirectAttributes redirectAttributes, Model model){
        try {
            List<Role> roleList = userService.getAllRoles();
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            model.addAttribute("roleList", roleList);
            model.addAttribute("pageTitle", "Edit User (ID: "+ userId + ")");
            return "user_form";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer userId, RedirectAttributes redirectAttributes, Model model){
        try {
            userService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("message", "The user ID " + userId + " has been deleted successfully");
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") Boolean enabled, RedirectAttributes redirectAttributes){
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "Enabled" : "Disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }
}
