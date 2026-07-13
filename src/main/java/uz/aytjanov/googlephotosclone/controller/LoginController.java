package uz.aytjanov.googlephotosclone.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uz.aytjanov.googlephotosclone.entity.User;
import uz.aytjanov.googlephotosclone.service.UsersService;

import java.util.Map;

@RestController
public class LoginController {
    private final UsersService usersService;

    public LoginController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = usersService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(Map.of("username", user.getUsername(), "userId", user.getId()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
}
