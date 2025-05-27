package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.User;
import system.dev.marques.service.AdminService;

@RestController
@RequestMapping("/home/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/list")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.findAll(pageable));
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
