package com.siddhant.usermanagement.Controller;

import com.siddhant.usermanagement.Model.User;
import com.siddhant.usermanagement.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "user created successfully !");
        response.put("data", savedUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/get")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.getUserByEmail(email);

        Map<String, Object> response = new HashMap<>();

        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                response.put("code", 200);
                response.put("message", "success");
                response.put("data", user.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 400);
                response.put("message", "Invalid credentials");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            response.put("code", 400);
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "updated");
        response.put("data", updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "User deleted successfully",
                    "data", Map.of(
                            "id", user.get().getId(),
                            "email", user.get().getEmail()
                    )
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "code", 400,
                "message", "Invalid credentials"
        ));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            userService.updateUser(user.get());  // ✅ Using updateUser() method

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "Password reset successfully"
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "code", 400,
                "message", "User not found"
        ));
    }

}
