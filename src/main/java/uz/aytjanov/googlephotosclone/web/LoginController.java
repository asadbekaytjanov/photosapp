package uz.aytjanov.googlephotosclone.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.aytjanov.googlephotosclone.model.Photo;
import uz.aytjanov.googlephotosclone.model.User;
import uz.aytjanov.googlephotosclone.service.PhotosService;
import uz.aytjanov.googlephotosclone.service.UsersService;

@Controller
public class LoginController {
    private final UsersService usersService;

    public LoginController(UsersService usersService) {
        this.usersService = usersService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User user = usersService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());
            model.addAttribute("name", user.getUsername());
            return "redirect:/gallery";
        }
        return "login";
    }
}
