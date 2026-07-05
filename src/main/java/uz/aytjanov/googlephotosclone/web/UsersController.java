package uz.aytjanov.googlephotosclone.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.aytjanov.googlephotosclone.service.UsersService;

@RestController
public class UsersController {
   private final UsersService usersService;

   public UsersController(UsersService usersService) {
        this.usersService = usersService;
   }
   @PostMapping("/register")
   public String createUser(@RequestParam String username, @RequestParam String password, Model model) {
       if (usersService.isUserExist(username)) {
           model.addAttribute("msg", "The username is taken. Try another one");
           return "register";
       } else {
           usersService.createUser(username, password);
           return "redirect:/login";
       }
   }
}