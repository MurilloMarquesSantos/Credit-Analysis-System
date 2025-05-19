package system.dev.marques.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserResponse;
import system.dev.marques.service.UserService;

import java.security.Principal;
import java.util.List;

//todo make the user history returns a pageable
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {

    private final WebClient webClient;

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userRequest, "formlogin"), HttpStatus.CREATED);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
    }


    @GetMapping("/user/history")
    public ResponseEntity<List<ProposalHistoryResponse>> getUserHistory(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        return ResponseEntity.ok(userService.fetchHistory(userId));
    }

}
