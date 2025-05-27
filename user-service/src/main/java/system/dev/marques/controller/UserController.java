package system.dev.marques.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.dev.marques.domain.dto.requests.DeleteForm;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {


    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userRequest, "formlogin"), HttpStatus.CREATED);
    }

    @PostMapping("/admin/create-admin")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
    }


    @GetMapping("/user/history")
    public ResponseEntity<Page<ProposalHistoryResponse>> getUserHistory(Principal principal, Pageable pageable) {
        Long userId = Long.valueOf(principal.getName());
        return ResponseEntity.ok(userService.fetchHistory(userId, pageable));
    }

    @GetMapping("/user/history/{id}")
    public String getUserProposalReceipt(@PathVariable long id, Principal principal) {
        return userService.sendUserReceipt(id, principal);
    }

    @PostMapping("/user/delete")
    public ResponseEntity<String> userDeleteForm(@RequestBody @Valid DeleteForm form, Principal principal) {
        return ResponseEntity.ok(userService.submitDeleteRequest(form, principal));
    }

}
