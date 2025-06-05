package system.dev.marques.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "Operations available only for administrators")
public class UserController {


    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Create user", description = "Retrieve created user.",
            tags = {"User"})
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User have invalid fields")))
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userRequest, "formlogin"), HttpStatus.CREATED);
    }

    @PostMapping("/admin/create-admin")
    @Operation(summary = "Create Admin", description = "Retrieve created Admin.",
            tags = {"User"})
    @ApiResponse(responseCode = "201", description = "Admin created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient permissions (Admin role required)",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User do not have permission")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Admin have invalid fields")))
    public ResponseEntity<UserResponse> createAdmin(@RequestBody @Valid UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/user/history")
    @Operation(summary = "Get History", description = "Retrieve user proposal history.",
            tags = {"User"})
    @ApiResponse(responseCode = "200", description = "History retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "503", description = "Service Unavailable - Service is temporality unavailable",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Service is unavailable. Try again later")))
    public ResponseEntity<Page<ProposalHistoryResponse>> getUserHistory(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(userService.fetchHistory(principal, pageable));
    }

    @GetMapping("/user/history/{id}")
    @Operation(summary = "Request Receipt", description = "Sends receipt requisition.",
            tags = {"User"})
    @ApiResponse(responseCode = "200", description = "Request processed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    public ResponseEntity<String> getUserProposalReceipt(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(userService.sendUserReceipt(id, principal));
    }

    @PostMapping("/user/delete")
    @Operation(summary = "Request User Deletion", description = "Sends receipt requisition.",
            tags = {"User"})
    @ApiResponse(responseCode = "200", description = "Request processed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User not found")))
    public ResponseEntity<String> userDeleteForm(@RequestBody @Valid DeleteForm form, Principal principal) {
        return ResponseEntity.ok(userService.submitDeleteRequest(form, principal));
    }

}
